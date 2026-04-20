package com.gym.gym_backend.modules.staff.dto;

import com.gym.gym_backend.modules.staff.entity.StaffRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class StaffDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AssignRequest {
        private UUID staffId;
        private UUID gymId;
        private StaffRole role;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private UUID id;
        private UUID staffId;
        private String staffName;
        private String staffEmail;
        private String staffPhone;
        private UUID gymId;
        private StaffRole role;
        private Boolean active;
        private String createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private StaffRole role;
        private Boolean active;
    }
}
