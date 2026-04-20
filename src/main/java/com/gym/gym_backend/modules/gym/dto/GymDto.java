package com.gym.gym_backend.modules.gym.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class GymDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank
        private String gymName;

        private String gymCode;

        private String gstNumber;

        @NotBlank
        private String address;

        private String locationCoordinates;

        @NotBlank
        private String phoneNumber;

        @NotBlank
        private String email;

        private String adminEmail;

        private String socialLinks;

        private String gymProfilePhoto;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String gymName;
        private String gymCode;
        private String gstNumber;
        private String address;
        private String locationCoordinates;
        private String phoneNumber;
        private String email;
        private String adminEmail;
        private String socialLinks;
        private String gymProfilePhoto;
        private String status;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private UUID id;
        private UUID tenantId;
        private String gymName;
        private String gymCode;
        private String gstNumber;
        private String address;
        private String locationCoordinates;
        private String phoneNumber;
        private String email;
        private String adminEmail;
        private String socialLinks;
        private String gymProfilePhoto;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
