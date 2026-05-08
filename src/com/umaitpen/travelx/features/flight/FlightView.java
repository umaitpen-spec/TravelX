package com.umaitpen.travelx.features.flight;

import com.umaitpen.travelx.data.dto.Flight;

import java.util.List;

public class FlightView {
    public void printFlights(List<Flight> flights) {
        if (flights.isEmpty()) {
            System.out.println("No flights found.");
            return;
        }
        flights.forEach(System.out::println);
    }
}
