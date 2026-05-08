package com.umaitpen.travelx.data.repository;

import com.umaitpen.travelx.data.dto.Booking;
import com.umaitpen.travelx.data.dto.Cancellation;
import com.umaitpen.travelx.data.dto.Flight;
import com.umaitpen.travelx.data.dto.Hotel;
import com.umaitpen.travelx.data.dto.Payment;
import com.umaitpen.travelx.data.dto.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TravelXDB {
    private final List<User> users = new ArrayList<>();
    private final List<Hotel> hotels = new ArrayList<>();
    private final List<Flight> flights = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();
    private final List<Payment> payments = new ArrayList<>();
    private final List<Cancellation> cancellations = new ArrayList<>();
    private final List<String> notifications = new ArrayList<>();

    private long userSeq = 1;
    private long hotelSeq = 1;
    private long flightSeq = 1;
    private long bookingSeq = 1;
    private long paymentSeq = 1;
    private long cancellationSeq = 1;

    public TravelXDB() {
        seedData();
    }

    public User createUser(String name, String email, String password, String mobileNo, User.Role role) {
        if (findUserByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already registered.");
        }
        User user = new User(userSeq++, name, email.toLowerCase(), hashPassword(password), mobileNo, role, User.Status.ACTIVE, now());
        users.add(user);
        notifyUser(user.getId(), "Account created successfully for " + user.getName());
        return user;
    }

    public Optional<User> login(String email, String password) {
        String passwordHash = hashPassword(password);
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .filter(user -> user.getPassword().equals(passwordHash))
                .filter(user -> user.getStatus() == User.Status.ACTIVE)
                .findFirst();
    }

    public Optional<User> findUserByEmail(String email) {
        return users.stream().filter(user -> user.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public Hotel addHotel(String name, String location, String description, double pricePerNight, int totalRooms, Long providerId) {
        validateProvider(providerId);
        Hotel hotel = new Hotel(hotelSeq++, name, location, description, pricePerNight, totalRooms, totalRooms, providerId);
        hotels.add(hotel);
        notifyUser(providerId, "Hotel added: " + hotel.getName());
        return hotel;
    }

    public Flight addFlight(String flightNumber, String source, String destination, long departureTime, long arrivalTime, double price, int totalSeats, Long providerId) {
        validateProvider(providerId);
        Flight flight = new Flight(flightSeq++, flightNumber, source, destination, departureTime, arrivalTime, price, totalSeats, totalSeats, providerId);
        flights.add(flight);
        notifyUser(providerId, "Flight added: " + flight.getFlightNumber());
        return flight;
    }

    public List<Hotel> searchHotels(String location, double maxPrice, int rooms) {
        return hotels.stream()
                .filter(hotel -> isBlank(location) || hotel.getLocation().toLowerCase().contains(location.toLowerCase()))
                .filter(hotel -> maxPrice <= 0 || hotel.getPricePerNight() <= maxPrice)
                .filter(hotel -> rooms <= 0 || hotel.getAvailableRooms() >= rooms)
                .sorted(Comparator.comparing(Hotel::getPricePerNight))
                .collect(Collectors.toList());
    }

    public List<Flight> searchFlights(String source, String destination, double maxPrice, int seats) {
        return flights.stream()
                .filter(flight -> isBlank(source) || flight.getSource().equalsIgnoreCase(source))
                .filter(flight -> isBlank(destination) || flight.getDestination().equalsIgnoreCase(destination))
                .filter(flight -> maxPrice <= 0 || flight.getPrice() <= maxPrice)
                .filter(flight -> seats <= 0 || flight.getAvailableSeats() >= seats)
                .sorted(Comparator.comparing(Flight::getPrice))
                .collect(Collectors.toList());
    }

    public List<Hotel> getProviderHotels(Long providerId) {
        return hotels.stream().filter(hotel -> hotel.getProviderId().equals(providerId)).collect(Collectors.toList());
    }

    public List<Flight> getProviderFlights(Long providerId) {
        return flights.stream().filter(flight -> flight.getProviderId().equals(providerId)).collect(Collectors.toList());
    }

    public Optional<Hotel> findHotel(Long id) {
        return hotels.stream().filter(hotel -> hotel.getId().equals(id)).findFirst();
    }

    public Optional<Flight> findFlight(Long id) {
        return flights.stream().filter(flight -> flight.getId().equals(id)).findFirst();
    }

    public Booking bookHotel(Long userId, Long hotelId, int rooms, long travelDate) {
        Hotel hotel = findHotel(hotelId).orElseThrow(() -> new IllegalArgumentException("Hotel not found."));
        if (hotel.getAvailableRooms() < rooms) {
            throw new IllegalArgumentException("Not enough rooms available.");
        }
        hotel.setAvailableRooms(hotel.getAvailableRooms() - rooms);
        Booking booking = new Booking(bookingSeq++, userId, Booking.ServiceType.HOTEL, hotelId, now(), travelDate, rooms, rooms * hotel.getPricePerNight(), Booking.BookingStatus.PENDING_PAYMENT);
        bookings.add(booking);
        notifyUser(userId, "Hotel booking created and waiting for payment. Booking ID: " + booking.getId());
        notifyUser(hotel.getProviderId(), "New hotel booking is waiting for payment. Booking ID: " + booking.getId());
        return booking;
    }

    public Booking bookFlight(Long userId, Long flightId, int seats, long travelDate) {
        Flight flight = findFlight(flightId).orElseThrow(() -> new IllegalArgumentException("Flight not found."));
        if (flight.getAvailableSeats() < seats) {
            throw new IllegalArgumentException("Not enough seats available.");
        }
        flight.setAvailableSeats(flight.getAvailableSeats() - seats);
        Booking booking = new Booking(bookingSeq++, userId, Booking.ServiceType.FLIGHT, flightId, now(), travelDate, seats, seats * flight.getPrice(), Booking.BookingStatus.PENDING_PAYMENT);
        bookings.add(booking);
        notifyUser(userId, "Flight booking created and waiting for payment. Booking ID: " + booking.getId());
        notifyUser(flight.getProviderId(), "New flight booking is waiting for payment. Booking ID: " + booking.getId());
        return booking;
    }

    public Payment makePayment(Long bookingId, Payment.PaymentMethod method) {
        Booking booking = findBooking(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking not found."));
        if (booking.getStatus() != Booking.BookingStatus.PENDING_PAYMENT) {
            throw new IllegalArgumentException("Payment is allowed only for pending bookings.");
        }
        Payment payment = new Payment(paymentSeq++, bookingId, booking.getTotalAmount(), method, Payment.PaymentStatus.SUCCESS, "TXN" + System.currentTimeMillis(), now());
        payments.add(payment);
        booking.setStatus(Booking.BookingStatus.BOOKED);
        notifyUser(booking.getUserId(), "Payment successful for booking " + bookingId + ". Transaction: " + payment.getTransactionId());
        notifyProviderForBooking(booking, "Booking " + bookingId + " confirmed after successful payment.");
        return payment;
    }

    public Cancellation cancelBooking(Long userId, Long bookingId, String reason) {
        Booking booking = findBooking(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking not found."));
        if (!booking.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You can cancel only your own booking.");
        }
        if (booking.getStatus() != Booking.BookingStatus.BOOKED) {
            throw new IllegalArgumentException("Only booked orders can be cancelled.");
        }
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        releaseAvailability(booking);
        double refundAmount = booking.getTotalAmount() * 0.90;
        Cancellation cancellation = new Cancellation(cancellationSeq++, bookingId, reason, refundAmount, Cancellation.CancellationStatus.REQUESTED, null);
        cancellations.add(cancellation);
        notifyUser(userId, "Cancellation requested for booking " + bookingId + ". Refund amount: Rs." + refundAmount);
        notifyProviderForBooking(booking, "Refund request received for booking " + bookingId);
        return cancellation;
    }

    public Cancellation processRefund(Long cancellationId, boolean approve) {
        Cancellation cancellation = cancellations.stream()
                .filter(item -> item.getId().equals(cancellationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cancellation request not found."));
        cancellation.setStatus(approve ? Cancellation.CancellationStatus.REFUNDED : Cancellation.CancellationStatus.REJECTED);
        cancellation.setProcessedDate(now());
        findBooking(cancellation.getBookingId()).ifPresent(booking -> notifyUser(booking.getUserId(), "Refund " + cancellation.getStatus() + " for booking " + booking.getId()));
        return cancellation;
    }

    public Optional<Booking> findBooking(Long id) {
        return bookings.stream().filter(booking -> booking.getId().equals(id)).findFirst();
    }

    public List<Booking> getBookingsForUser(Long userId) {
        return bookings.stream().filter(booking -> booking.getUserId().equals(userId)).collect(Collectors.toList());
    }

    public List<Booking> getBookingsForProvider(Long providerId) {
        return bookings.stream().filter(booking -> isProviderBooking(booking, providerId)).collect(Collectors.toList());
    }

    public List<Payment> getPaymentsForProvider(Long providerId) {
        return payments.stream()
                .filter(payment -> findBooking(payment.getBookingId()).map(booking -> isProviderBooking(booking, providerId)).orElse(false))
                .collect(Collectors.toList());
    }

    public List<Payment> getPaymentsForUser(Long userId) {
        return payments.stream()
                .filter(payment -> findBooking(payment.getBookingId()).map(booking -> booking.getUserId().equals(userId)).orElse(false))
                .collect(Collectors.toList());
    }

    public List<Cancellation> getCancellationsForProvider(Long providerId) {
        return cancellations.stream()
                .filter(cancellation -> findBooking(cancellation.getBookingId()).map(booking -> isProviderBooking(booking, providerId)).orElse(false))
                .collect(Collectors.toList());
    }

    public List<String> getNotifications() {
        return new ArrayList<>(notifications);
    }

    public static long dateToEpoch(String isoDate) {
        return LocalDate.parse(isoDate).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte value : hash) {
                builder.append(String.format("%02x", value));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 hashing is not available.", exception);
        }
    }

    private void seedData() {
        User admin = createUser("Admin", "admin@travelx.com", "admin123", "9000000000", User.Role.ADMIN);
        User provider = createUser("Demo Provider", "provider@travelx.com", "provider123", "9111111111", User.Role.PROVIDER);
        createUser("Demo Customer", "customer@travelx.com", "customer123", "9222222222", User.Role.CUSTOMER);

        addHotel("Marina Bay Comfort", "Chennai", "City hotel near beach and metro.", 2500.0, 20, provider.getId());
        addHotel("Western Ghats Stay", "Coimbatore", "Quiet hill-facing rooms for family travel.", 3200.0, 12, provider.getId());
        addFlight("TX101", "Chennai", "Delhi", dateToEpoch("2026-06-10"), dateToEpoch("2026-06-10") + 10800000L, 6200.0, 60, provider.getId());
        addFlight("TX202", "Chennai", "Mumbai", dateToEpoch("2026-06-12"), dateToEpoch("2026-06-12") + 7200000L, 4800.0, 45, provider.getId());
        notifyUser(admin.getId(), "System seeded with demo users and services.");
    }

    private void validateProvider(Long providerId) {
        User provider = users.stream()
                .filter(user -> user.getId().equals(providerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Provider not found."));
        if (provider.getRole() != User.Role.PROVIDER && provider.getRole() != User.Role.ADMIN) {
            throw new IllegalArgumentException("Only providers can add services.");
        }
    }

    private void releaseAvailability(Booking booking) {
        if (booking.getServiceType() == Booking.ServiceType.HOTEL) {
            findHotel(booking.getServiceId()).ifPresent(hotel -> hotel.setAvailableRooms(hotel.getAvailableRooms() + booking.getQuantity()));
        } else {
            findFlight(booking.getServiceId()).ifPresent(flight -> flight.setAvailableSeats(flight.getAvailableSeats() + booking.getQuantity()));
        }
    }

    private boolean isProviderBooking(Booking booking, Long providerId) {
        if (booking.getServiceType() == Booking.ServiceType.HOTEL) {
            return findHotel(booking.getServiceId()).map(hotel -> hotel.getProviderId().equals(providerId)).orElse(false);
        }
        return findFlight(booking.getServiceId()).map(flight -> flight.getProviderId().equals(providerId)).orElse(false);
    }

    private void notifyProviderForBooking(Booking booking, String message) {
        if (booking.getServiceType() == Booking.ServiceType.HOTEL) {
            findHotel(booking.getServiceId()).ifPresent(hotel -> notifyUser(hotel.getProviderId(), message));
        } else {
            findFlight(booking.getServiceId()).ifPresent(flight -> notifyUser(flight.getProviderId(), message));
        }
    }

    private void notifyUser(Long userId, String message) {
        notifications.add("User " + userId + ": " + message);
    }

    private long now() {
        return System.currentTimeMillis();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
