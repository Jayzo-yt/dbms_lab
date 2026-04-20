package com.gym.gym_backend.modules.profile.controller;

import com.gym.gym_backend.common.dto.GlobalResponseDTO;
import com.gym.gym_backend.modules.profile.dto.ProfileDTO;
import com.gym.gym_backend.modules.profile.service.ProfileService;
import com.gym.gym_backend.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<GlobalResponseDTO<ProfileDTO>> getProfile(HttpServletRequest request) {
        UUID userId = extractUserId(request);
        ProfileDTO profile = profileService.getProfile(userId);
        return ResponseEntity.ok(GlobalResponseDTO.success(profile, "Profile retrieved successfully"));
    }

    @PutMapping
    public ResponseEntity<GlobalResponseDTO<ProfileDTO>> updateProfile(
            @RequestBody ProfileDTO profileDTO,
            HttpServletRequest request
    ) {
        UUID userId = extractUserId(request);
        ProfileDTO updated = profileService.updateProfile(userId, profileDTO);
        return ResponseEntity.ok(GlobalResponseDTO.success(updated, "Profile updated successfully"));
    }

    private UUID extractUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return jwtService.extractUserId(token);
    }
}
