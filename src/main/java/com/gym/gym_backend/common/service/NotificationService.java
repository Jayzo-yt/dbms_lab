package com.gym.gym_backend.common.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationService {

    public void sendNotification(UUID userId, String message) {

        System.out.println("Sending notification to user: " + userId);
        System.out.println("Message: " + message);

        // Future Implementation:
        // WhatsApp API
        // SMS Gateway
        // Firebase push notification
    }
}