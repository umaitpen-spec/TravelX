package com.umaitpen.travelx.features.report;

import com.umaitpen.travelx.data.dto.Booking;
import com.umaitpen.travelx.data.dto.Payment;
import com.umaitpen.travelx.data.dto.User;
import com.umaitpen.travelx.data.repository.TravelXDB;

import java.util.List;

public class ReportModel {
    private final TravelXDB db;

    public ReportModel(TravelXDB db) {
        this.db = db;
    }

    public double getProviderRevenue(Long providerId) {
        return db.getPaymentsForProvider(providerId).stream()
                .filter(payment -> payment.getPaymentStatus() == Payment.PaymentStatus.SUCCESS)
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    public int getProviderBookingCount(Long providerId) {
        return db.getBookingsForProvider(providerId).size();
    }

    public List<User> getAllUsers() {
        return db.getUsers();
    }

    public List<Booking> getProviderBookings(Long providerId) {
        return db.getBookingsForProvider(providerId);
    }
}
