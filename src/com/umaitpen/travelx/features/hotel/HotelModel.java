package com.umaitpen.travelx.features.hotel;

import com.umaitpen.travelx.data.dto.Booking;
import com.umaitpen.travelx.data.dto.Hotel;
import com.umaitpen.travelx.data.repository.TravelXDB;

import java.util.List;

public class HotelModel {
    private final TravelXDB db;

    public HotelModel(TravelXDB db) {
        this.db = db;
    }

    public Hotel addHotel(String name, String location, String description, double pricePerNight, int totalRooms, Long providerId) {
        return db.addHotel(name, location, description, pricePerNight, totalRooms, providerId);
    }

    public List<Hotel> searchHotels(String location, double maxPrice, int rooms) {
        return db.searchHotels(location, maxPrice, rooms);
    }

    public List<Hotel> getProviderHotels(Long providerId) {
        return db.getProviderHotels(providerId);
    }

    public Booking bookHotel(Long userId, Long hotelId, int rooms, long travelDate) {
        return db.bookHotel(userId, hotelId, rooms, travelDate);
    }
}
