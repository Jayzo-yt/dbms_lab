package com.gym.gym_backend.modules.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDTO {
    private UUID id;
    private UUID userId;
    private String profilePhoto;
    private BigDecimal height;
    private BigDecimal weight;
    private String gender;
    private Integer age;
    private String address;
}
