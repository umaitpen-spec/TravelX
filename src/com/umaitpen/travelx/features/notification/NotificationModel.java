package com.umaitpen.travelx.features.notification;

import com.umaitpen.travelx.data.repository.TravelXDB;

import java.util.List;

public class NotificationModel {
    private final TravelXDB db;

    public NotificationModel(TravelXDB db) {
        this.db = db;
    }

    public List<String> getNotifications() {
        return db.getNotifications();
    }
}
