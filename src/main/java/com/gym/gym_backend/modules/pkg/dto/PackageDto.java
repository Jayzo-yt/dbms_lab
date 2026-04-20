package com.gym.gym_backend.modules.pkg.dto;

import com.gym.gym_backend.modules.pkg.entity.Package;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class PackageDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        private String name;
        private String description;
        private Double price;
        private Integer durationDays;
        private Integer sessions;
        private Package.PackageType type;
        private Boolean freezeAllowed;
        private Integer maxFreezeDays;
        private String features;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private UUID id;
        private String name;
        private String description;
        private Double price;
        private Integer durationDays;
        private Integer sessions;
        private String type;
        private Boolean active;
        private Boolean freezeAllowed;
        private Integer maxFreezeDays;
        private String features;
        private String createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String name;
        private String description;
        private Double price;
        private Integer durationDays;
        private Integer sessions;
        private Boolean active;
        private Boolean freezeAllowed;
        private Integer maxFreezeDays;
        private String features;
    }
}
