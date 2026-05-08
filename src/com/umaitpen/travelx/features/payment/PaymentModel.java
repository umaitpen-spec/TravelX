package com.umaitpen.travelx.features.payment;

import com.umaitpen.travelx.data.dto.Payment;
import com.umaitpen.travelx.data.repository.TravelXDB;

import java.util.List;

public class PaymentModel {
    private final TravelXDB db;

    public PaymentModel(TravelXDB db) {
        this.db = db;
    }

    public Payment pay(Long bookingId, Payment.PaymentMethod method) {
        return db.makePayment(bookingId, method);
    }

    public List<Payment> getPaymentsForUser(Long userId) {
        return db.getPaymentsForUser(userId);
    }

    public List<Payment> getPaymentsForProvider(Long providerId) {
        return db.getPaymentsForProvider(providerId);
    }
}
