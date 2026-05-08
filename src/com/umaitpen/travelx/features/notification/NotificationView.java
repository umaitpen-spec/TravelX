package com.umaitpen.travelx.features.notification;

import java.util.List;

public class NotificationView {
    public void printNotifications(List<String> notifications) {
        if (notifications.isEmpty()) {
            System.out.println("No notifications found.");
            return;
        }
        notifications.forEach(System.out::println);
    }
}
