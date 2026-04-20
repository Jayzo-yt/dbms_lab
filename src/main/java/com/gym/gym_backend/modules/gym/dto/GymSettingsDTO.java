package com.gym.gym_backend.modules.gym.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class GymSettingsDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private Integer crowdLimit;
        private Integer sessionDurationMinutes;
        private Integer minimumValidMinutes;
        private Integer expiryAlertDays;
        private Integer absentAlertDays;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private UUID id;
        private UUID tenantId;
        private UUID gymId;
        private Integer crowdLimit;
        private Integer sessionDurationMinutes;
        private Integer minimumValidMinutes;
        private Integer expiryAlertDays;
        private Integer absentAlertDays;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}