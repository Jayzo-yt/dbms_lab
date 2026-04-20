package com.gym.gym_backend.modules.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Returned only on admin signup.
 * Login returns AuthResponseDTO (with token).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminSignupResponseDTO {
    private UUID userId;
}
