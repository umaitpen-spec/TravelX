package com.umaitpen.travelx;

import com.umaitpen.travelx.data.dto.Booking;
import com.umaitpen.travelx.data.dto.Cancellation;
import com.umaitpen.travelx.data.dto.Flight;
import com.umaitpen.travelx.data.dto.Hotel;
import com.umaitpen.travelx.data.dto.Payment;
import com.umaitpen.travelx.data.dto.User;
import com.umaitpen.travelx.data.repository.TravelXDB;
import com.umaitpen.travelx.features.flight.FlightModel;
import com.umaitpen.travelx.features.flight.FlightView;
import com.umaitpen.travelx.features.hotel.HotelModel;
import com.umaitpen.travelx.features.hotel.HotelView;
import com.umaitpen.travelx.features.notification.NotificationModel;
import com.umaitpen.travelx.features.notification.NotificationView;
import com.umaitpen.travelx.features.payment.PaymentModel;
import com.umaitpen.travelx.features.payment.PaymentView;
import com.umaitpen.travelx.features.report.ReportModel;
import com.umaitpen.travelx.features.report.ReportView;
import com.umaitpen.travelx.features.signin.SignInModel;
import com.umaitpen.travelx.features.signup.SignUpModel;
import com.umaitpen.travelx.features.signup.SignUpView;
import com.umaitpen.travelx.features.user.UserModel;
import com.umaitpen.travelx.features.user.UserView;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static final int VERSION_NO = 1;
    public static final String VERSION_NAME = "0.0.1";

    private final Scanner scanner = new Scanner(System.in);
    private final TravelXDB db = new TravelXDB();

    private final SignInModel signInModel = new SignInModel(db);
    private final SignUpModel signUpModel = new SignUpModel(db);
    private final SignUpView signUpView = new SignUpView();
    private final HotelModel hotelModel = new HotelModel(db);
    private final HotelView hotelView = new HotelView();
    private final FlightModel flightModel = new FlightModel(db);
    private final FlightView flightView = new FlightView();
    private final PaymentModel paymentModel = new PaymentModel(db);
    private final PaymentView paymentView = new PaymentView();
    private final UserModel userModel = new UserModel(db);
    private final UserView userView = new UserView();
    private final NotificationModel notificationModel = new NotificationModel(db);
    private final NotificationView notificationView = new NotificationView();
    private final ReportModel reportModel = new ReportModel(db);
    private final ReportView reportView = new ReportView();

    public static void main(String[] args) {
        new Main().start();
    }

    private void start() {
        System.out.println("TravelX - Travel Management System v" + VERSION_NAME);
        System.out.println("Demo logins: customer@travelx.com/customer123, provider@travelx.com/provider123, admin@travelx.com/admin123");
        boolean running = true;
        while (running) {
            printHeader("Main Menu");
            System.out.println("1. Sign up");
            System.out.println("2. Sign in");
            System.out.println("3. View notifications");
            System.out.println("0. Exit");
            switch (readInt("Choose option: ")) {
                case 1:
                    signUp();
                    break;
                case 2:
                    signIn();
                    break;
                case 3:
                    notificationView.printNotifications(notificationModel.getNotifications());
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
        System.out.println("Thank you for using TravelX.");
    }

    private void signUp() {
        printHeader("Create Account");
        String name = readLine("Name: ");
        String email = readLine("Email: ");
        String password = readLine("Password: ");
        String mobileNo = readLine("Mobile number: ");
        System.out.println("1. Customer");
        System.out.println("2. Service Provider");
        User.Role role = readInt("Role: ") == 2 ? User.Role.PROVIDER : User.Role.CUSTOMER;
        try {
            User user = signUpModel.register(name, email, password, mobileNo, role);
            signUpView.showSuccess(user.getName());
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void signIn() {
        printHeader("Sign In");
        String email = readLine("Email: ");
        String password = readLine("Password: ");
        Optional<User> loggedInUser = signInModel.signIn(email, password);
        if (loggedInUser.isEmpty()) {
            signInModel.getSignInView().showInvalidLogin();
            return;
        }
        User user = loggedInUser.get();
        System.out.println("Welcome, " + user.getName() + " (" + user.getRole() + ")");
        if (user.getRole() == User.Role.CUSTOMER) {
            customerMenu(user);
        } else if (user.getRole() == User.Role.PROVIDER) {
            providerMenu(user);
        } else {
            adminMenu();
        }
    }

    private void customerMenu(User user) {
        boolean loggedIn = true;
        while (loggedIn) {
            printHeader("Customer Menu");
            System.out.println("1. Search hotels");
            System.out.println("2. Search flights");
            System.out.println("3. Book hotel");
            System.out.println("4. Book flight");
            System.out.println("5. Make payment");
            System.out.println("6. View booking history");
            System.out.println("7. View payments");
            System.out.println("8. Cancel booking / request refund");
            System.out.println("0. Logout");
            try {
                switch (readInt("Choose option: ")) {
                    case 1:
                        searchHotels();
                        break;
                    case 2:
                        searchFlights();
                        break;
                    case 3:
                        bookHotel(user);
                        break;
                    case 4:
                        bookFlight(user);
                        break;
                    case 5:
                        makePayment();
                        break;
                    case 6:
                        userView.printBookings(userModel.getBookingsForUser(user.getId()));
                        break;
                    case 7:
                        paymentView.printPayments(paymentModel.getPaymentsForUser(user.getId()));
                        break;
                    case 8:
                        cancelBooking(user);
                        break;
                    case 0:
                        loggedIn = false;
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    private void providerMenu(User provider) {
        boolean loggedIn = true;
        while (loggedIn) {
            printHeader("Provider Menu");
            System.out.println("1. Add hotel");
            System.out.println("2. Add flight");
            System.out.println("3. View my services");
            System.out.println("4. View bookings");
            System.out.println("5. Track payments");
            System.out.println("6. Approve / reject refunds");
            System.out.println("7. Provider report");
            System.out.println("0. Logout");
            try {
                switch (readInt("Choose option: ")) {
                    case 1:
                        addHotel(provider);
                        break;
                    case 2:
                        addFlight(provider);
                        break;
                    case 3:
                        hotelView.printHotels(hotelModel.getProviderHotels(provider.getId()));
                        flightView.printFlights(flightModel.getProviderFlights(provider.getId()));
                        break;
                    case 4:
                        userView.printBookings(userModel.getBookingsForProvider(provider.getId()));
                        break;
                    case 5:
                        paymentView.printPayments(paymentModel.getPaymentsForProvider(provider.getId()));
                        break;
                    case 6:
                        processRefund(provider);
                        break;
                    case 7:
                        reportView.printProviderReport(reportModel.getProviderBookingCount(provider.getId()), reportModel.getProviderRevenue(provider.getId()));
                        break;
                    case 0:
                        loggedIn = false;
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    private void adminMenu() {
        boolean loggedIn = true;
        while (loggedIn) {
            printHeader("Admin Menu");
            System.out.println("1. View users");
            System.out.println("2. View notifications");
            System.out.println("0. Logout");
            switch (readInt("Choose option: ")) {
                case 1:
                    userView.printUsers(reportModel.getAllUsers());
                    break;
                case 2:
                    notificationView.printNotifications(notificationModel.getNotifications());
                    break;
                case 0:
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void searchHotels() {
        printHeader("Search Hotels");
        String location = readLine("Location (blank for all): ");
        double maxPrice = readDouble("Max price, 0 for no limit: ");
        int rooms = readInt("Required rooms, 0 for all: ");
        hotelView.printHotels(hotelModel.searchHotels(location, maxPrice, rooms));
    }

    private void searchFlights() {
        printHeader("Search Flights");
        String source = readLine("Source (blank for all): ");
        String destination = readLine("Destination (blank for all): ");
        double maxPrice = readDouble("Max price, 0 for no limit: ");
        int seats = readInt("Required seats, 0 for all: ");
        flightView.printFlights(flightModel.searchFlights(source, destination, maxPrice, seats));
    }

    private void bookHotel(User user) {
        searchHotels();
        Long hotelId = readLong("Hotel ID: ");
        int rooms = readInt("Rooms: ");
        long travelDate = readDate("Travel date (yyyy-mm-dd): ");
        Booking booking = hotelModel.bookHotel(user.getId(), hotelId, rooms, travelDate);
        System.out.println("Booking created: " + booking);
        System.out.println("Please use Make payment with Booking ID " + booking.getId() + " to confirm it.");
    }

    private void bookFlight(User user) {
        searchFlights();
        Long flightId = readLong("Flight ID: ");
        int seats = readInt("Seats: ");
        long travelDate = readDate("Travel date (yyyy-mm-dd): ");
        Booking booking = flightModel.bookFlight(user.getId(), flightId, seats, travelDate);
        System.out.println("Booking created: " + booking);
        System.out.println("Please use Make payment with Booking ID " + booking.getId() + " to confirm it.");
    }

    private void makePayment() {
        Long bookingId = readLong("Booking ID: ");
        System.out.println("1. Card");
        System.out.println("2. UPI");
        System.out.println("3. NetBanking");
        int option = readInt("Payment method: ");
        Payment.PaymentMethod method = option == 2 ? Payment.PaymentMethod.UPI : option == 3 ? Payment.PaymentMethod.NETBANKING : Payment.PaymentMethod.CARD;
        Payment payment = paymentModel.pay(bookingId, method);
        System.out.println("Payment saved: " + payment);
    }

    private void cancelBooking(User user) {
        userView.printBookings(userModel.getBookingsForUser(user.getId()));
        Long bookingId = readLong("Booking ID to cancel: ");
        String reason = readLine("Reason: ");
        Cancellation cancellation = userModel.cancelBooking(user.getId(), bookingId, reason);
        System.out.println("Cancellation request created: " + cancellation);
    }

    private void addHotel(User provider) {
        printHeader("Add Hotel");
        String name = readLine("Hotel name: ");
        String location = readLine("Location: ");
        String description = readLine("Description: ");
        double price = readDouble("Price per night: ");
        int rooms = readInt("Total rooms: ");
        Hotel hotel = hotelModel.addHotel(name, location, description, price, rooms, provider.getId());
        System.out.println("Hotel added: " + hotel);
    }

    private void addFlight(User provider) {
        printHeader("Add Flight");
        String flightNumber = readLine("Flight number: ");
        String source = readLine("Source: ");
        String destination = readLine("Destination: ");
        long departure = readDate("Departure date (yyyy-mm-dd): ");
        long arrival = readDate("Arrival date (yyyy-mm-dd): ");
        double price = readDouble("Price: ");
        int seats = readInt("Total seats: ");
        Flight flight = flightModel.addFlight(flightNumber, source, destination, departure, arrival, price, seats, provider.getId());
        System.out.println("Flight added: " + flight);
    }

    private void processRefund(User provider) {
        List<Cancellation> requests = userModel.getCancellationsForProvider(provider.getId());
        userView.printCancellations(requests);
        if (requests.isEmpty()) {
            return;
        }
        Long cancellationId = readLong("Cancellation ID: ");
        boolean approve = readLine("Approve refund? (yes/no): ").equalsIgnoreCase("yes");
        Cancellation cancellation = userModel.processRefund(cancellationId, approve);
        System.out.println("Refund updated: " + cancellation);
    }

    private long readDate(String prompt) {
        while (true) {
            try {
                return TravelXDB.dateToEpoch(readLine(prompt));
            } catch (RuntimeException exception) {
                System.out.println("Enter date in yyyy-mm-dd format.");
            }
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException exception) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    private Long readLong(String prompt) {
        while (true) {
            try {
                return Long.parseLong(readLine(prompt));
            } catch (NumberFormatException exception) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readLine(prompt));
            } catch (NumberFormatException exception) {
                System.out.println("Enter a valid amount.");
            }
        }
    }

    private void printHeader(String title) {
        System.out.println();
        System.out.println("==== " + title + " ====");
    }
}
