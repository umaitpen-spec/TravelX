package com.umaitpen.travelx.features.hotel;

import com.umaitpen.travelx.data.dto.Hotel;

import java.util.List;

public class HotelView {
    public void printHotels(List<Hotel> hotels) {
        if (hotels.isEmpty()) {
            System.out.println("No hotels found.");
            return;
        }
        hotels.forEach(System.out::println);
    }
}
