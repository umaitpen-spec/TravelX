package com.umaitpen.travelx.features.user;

import com.umaitpen.travelx.data.dto.Booking;
import com.umaitpen.travelx.data.dto.Cancellation;
import com.umaitpen.travelx.data.dto.User;
import com.umaitpen.travelx.data.repository.TravelXDB;

import java.util.List;

public class UserModel {
    private final TravelXDB db;

    public UserModel(TravelXDB db) {
        this.db = db;
    }

    public List<User> getUsers() {
        return db.getUsers();
    }

    public List<Booking> getBookingsForUser(Long userId) {
        return db.getBookingsForUser(userId);
    }

    public List<Booking> getBookingsForProvider(Long providerId) {
        return db.getBookingsForProvider(providerId);
    }

    public Cancellation cancelBooking(Long userId, Long bookingId, String reason) {
        return db.cancelBooking(userId, bookingId, reason);
    }

    public Cancellation processRefund(Long cancellationId, boolean approve) {
        return db.processRefund(cancellationId, approve);
    }

    public List<Cancellation> getCancellationsForProvider(Long providerId) {
        return db.getCancellationsForProvider(providerId);
    }
}
