package com.umaitpen.travelx.features.flight;

import com.umaitpen.travelx.data.dto.Booking;
import com.umaitpen.travelx.data.dto.Flight;
import com.umaitpen.travelx.data.repository.TravelXDB;

import java.util.List;

public class FlightModel {
    private final TravelXDB db;

    public FlightModel(TravelXDB db) {
        this.db = db;
    }

    public Flight addFlight(String flightNumber, String source, String destination, long departureTime, long arrivalTime, double price, int totalSeats, Long providerId) {
        return db.addFlight(flightNumber, source, destination, departureTime, arrivalTime, price, totalSeats, providerId);
    }

    public List<Flight> searchFlights(String source, String destination, double maxPrice, int seats) {
        return db.searchFlights(source, destination, maxPrice, seats);
    }

    public List<Flight> getProviderFlights(Long providerId) {
        return db.getProviderFlights(providerId);
    }

    public Booking bookFlight(Long userId, Long flightId, int seats, long travelDate) {
        return db.bookFlight(userId, flightId, seats, travelDate);
    }
}
