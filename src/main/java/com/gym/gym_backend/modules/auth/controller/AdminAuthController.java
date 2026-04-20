package com.gym.gym_backend.modules.auth.controller;

import com.gym.gym_backend.common.constants.Role;
import com.gym.gym_backend.common.dto.GlobalResponseDTO;
import com.gym.gym_backend.modules.auth.dto.AdminCreateUserRequestDTO;
import com.gym.gym_backend.modules.auth.dto.AdminSignupResponseDTO;
import com.gym.gym_backend.modules.auth.dto.AuthResponseDTO;
import com.gym.gym_backend.modules.auth.dto.LoginRequestDTO;
import com.gym.gym_backend.modules.auth.dto.SignupRequestDTO;
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
@RequestMapping("/auth/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<GlobalResponseDTO<AdminSignupResponseDTO>> signup(@Valid @RequestBody SignupRequestDTO request) {
        AdminSignupResponseDTO response = authService.signup(request, Role.ADMIN);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(GlobalResponseDTO.success(response, "Admin registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<GlobalResponseDTO<AuthResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO response = authService.login(request, Role.ADMIN);
        return ResponseEntity.ok(GlobalResponseDTO.success(response, "Admin login successful"));
    }

    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponseDTO<AdminSignupResponseDTO>> createUser(@Valid @RequestBody AdminCreateUserRequestDTO request) {
        AdminSignupResponseDTO response = authService.createRoleUserByAdmin(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(GlobalResponseDTO.success(response, "User credentials created successfully"));
    }
}
