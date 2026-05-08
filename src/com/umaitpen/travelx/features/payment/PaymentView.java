package com.umaitpen.travelx.features.payment;

import com.umaitpen.travelx.data.dto.Payment;

import java.util.List;

public class PaymentView {
    public void printPayments(List<Payment> payments) {
        if (payments.isEmpty()) {
            System.out.println("No payments found.");
            return;
        }
        payments.forEach(System.out::println);
    }
}
