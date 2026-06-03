package com.umaitpen.travelx.data.dto;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Booking {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Long id;
    private Long userId;
    private String userName;
    private ServiceType serviceType;
    private Long serviceId;
    private String serviceName;
    private Long bookingDate;
    private Long travelDate;
    private Integer quantity; // seats or rooms
    private Double totalAmount;
    private BookingStatus status;

    public enum ServiceType {
        HOTEL, FLIGHT
    }

    public enum BookingStatus {
        PENDING_PAYMENT, BOOKED, CANCELLED, COMPLETED
    }

    public Booking() {
    }

    public Booking(Long id, Long userId, ServiceType serviceType, Long serviceId, Long bookingDate, Long travelDate, Integer quantity, Double totalAmount, BookingStatus status) {
        this(id, userId, null, serviceType, serviceId, null, bookingDate, travelDate, quantity, totalAmount, status);
    }

    public Booking(Long id, Long userId, ServiceType serviceType, Long serviceId, String serviceName, Long bookingDate, Long travelDate, Integer quantity, Double totalAmount, BookingStatus status) {
        this(id, userId, null, serviceType, serviceId, serviceName, bookingDate, travelDate, quantity, totalAmount, status);
    }

    public Booking(Long id, Long userId, String userName, ServiceType serviceType, Long serviceId, String serviceName, Long bookingDate, Long travelDate, Integer quantity, Double totalAmount, BookingStatus status) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.serviceType = serviceType;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.bookingDate = bookingDate;
        this.travelDate = travelDate;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Long getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Long bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Long getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Long travelDate) {
        this.travelDate = travelDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String user = userName == null || userName.trim().isEmpty() ? "user " + userId : userName;
        String service = serviceName == null || serviceName.trim().isEmpty() ? serviceType + " #" + serviceId : serviceName;
        return id + " | " + user + " | " + serviceType + " | " + service + " | booked " + formatDate(bookingDate) + " | travel " + formatDate(travelDate) + " | qty " + quantity + " | Rs." + totalAmount + " | " + status;
    }

    private String formatDate(Long epochMillis) {
        if (epochMillis == null) {
            return "-";
        }
        return Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()).format(DATE_FORMATTER);
    }
}
