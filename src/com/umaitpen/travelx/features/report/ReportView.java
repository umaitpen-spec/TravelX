package com.umaitpen.travelx.features.report;

public class ReportView {
    public void printProviderReport(int bookingCount, double revenue) {
        System.out.println("Bookings handled: " + bookingCount);
        System.out.println("Successful payment revenue: Rs." + revenue);
    }
}
