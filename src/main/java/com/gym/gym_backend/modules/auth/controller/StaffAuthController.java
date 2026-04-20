package com.gym.gym_backend.modules.auth.controller;

import com.gym.gym_backend.common.constants.Role;
import com.gym.gym_backend.common.dto.GlobalResponseDTO;
import com.gym.gym_backend.modules.auth.dto.AuthResponseDTO;
import com.gym.gym_backend.modules.auth.dto.LoginRequestDTO;
import com.gym.gym_backend.modules.auth.dto.StaffSignupRequestDTO;
import com.gym.gym_backend.modules.auth.dto.StaffSignupResponseDTO;
import com.gym.gym_backend.modules.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/staff")
@RequiredArgsConstructor
public class StaffAuthController {

    private final AuthService authService;

    /**
     * Staff signup is admin-controlled.
     * Accepts: name, email, password, gymId.
     * tenantId is resolved from gym lookup — never from request body.
     */
    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponseDTO<StaffSignupResponseDTO>> signup(@Valid @RequestBody StaffSignupRequestDTO request) {
        StaffSignupResponseDTO response = authService.staffSignup(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(GlobalResponseDTO.success(response, "Staff registered successfully"));
    }

    /**
     * Staff login — returns token with tenantId + gymId from DB.
     */
    @PostMapping("/login")
    public ResponseEntity<GlobalResponseDTO<AuthResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO response = authService.login(request, Role.STAFF);
        return ResponseEntity.ok(GlobalResponseDTO.success(response, "Staff login successful"));
    }
}
