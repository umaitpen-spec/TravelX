package com.umaitpen.travelx.data.dto;

public class Booking {
    private Long id;
    private Long userId;
    private ServiceType serviceType;
    private Long serviceId;
    private Long bookingDate;
    private Long travelDate;
    private Integer quantity; // seats or rooms
    private Double totalAmount;
    private BookingStatus status;

    public enum ServiceType {
        HOTEL, FLIGHT
    }

    public enum BookingStatus {
        BOOKED, CANCELLED, COMPLETED
    }

    public Booking() {
    }

    public Booking(Long id, Long userId, ServiceType serviceType, Long serviceId, Long bookingDate, Long travelDate, Integer quantity, Double totalAmount, BookingStatus status) {
        this.id = id;
        this.userId = userId;
        this.serviceType = serviceType;
        this.serviceId = serviceId;
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
        return id + " | user " + userId + " | " + serviceType + " #" + serviceId + " | qty " + quantity + " | Rs." + totalAmount + " | " + status;
    }
}
