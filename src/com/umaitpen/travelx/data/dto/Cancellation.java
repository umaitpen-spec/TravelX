package com.umaitpen.travelx.data.dto;

public class Cancellation {
    private Long id;
    private Long bookingId;
    private String reason;
    private Double refundAmount;
    private CancellationStatus status;
    private Long processedDate;

    public enum CancellationStatus {
        REQUESTED, APPROVED, REJECTED, REFUNDED
    }

    public Cancellation() {
    }

    public Cancellation(Long id, Long bookingId, String reason, Double refundAmount, CancellationStatus status, Long processedDate) {
        this.id = id;
        this.bookingId = bookingId;
        this.reason = reason;
        this.refundAmount = refundAmount;
        this.status = status;
        this.processedDate = processedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public CancellationStatus getStatus() {
        return status;
    }

    public void setStatus(CancellationStatus status) {
        this.status = status;
    }

    public Long getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(Long processedDate) {
        this.processedDate = processedDate;
    }

    @Override
    public String toString() {
        return id + " | booking " + bookingId + " | refund Rs." + refundAmount + " | " + status + " | " + reason;
    }
}
