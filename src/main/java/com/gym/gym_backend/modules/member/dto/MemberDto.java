package com.gym.gym_backend.modules.member.dto;

import com.gym.gym_backend.modules.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

public class MemberDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String address;
        private LocalDate dateOfBirth;
        private String gender;
        private UUID packageId;
        private String emergencyContactName;
        private String emergencyContactPhone;
        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private UUID id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String address;
        private LocalDate dateOfBirth;
        private String gender;
        private LocalDate membershipStartDate;
        private LocalDate membershipEndDate;
        private UUID packageId;
        private String status;
        private String emergencyContactName;
        private String emergencyContactPhone;
        private String profilePhotoUrl;
        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String firstName;
        private String lastName;
        private String phone;
        private String address;
        private String gender;
        private UUID packageId;
        private String status;
        private String emergencyContactName;
        private String emergencyContactPhone;
        private String notes;
    }
}
