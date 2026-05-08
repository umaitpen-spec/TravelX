package com.umaitpen.travelx.features.user;

import com.umaitpen.travelx.data.dto.Booking;
import com.umaitpen.travelx.data.dto.Cancellation;
import com.umaitpen.travelx.data.dto.User;

import java.util.List;

public class UserView {
    public void printUsers(List<User> users) {
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        users.forEach(System.out::println);
    }

    public void printBookings(List<Booking> bookings) {
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }
        bookings.forEach(System.out::println);
    }

    public void printCancellations(List<Cancellation> cancellations) {
        if (cancellations.isEmpty()) {
            System.out.println("No cancellation requests found.");
            return;
        }
        cancellations.forEach(System.out::println);
    }
}
