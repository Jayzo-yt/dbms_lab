package com.gym.gym_backend.modules.auth.controller;

import com.gym.gym_backend.common.constants.Role;
import com.gym.gym_backend.common.dto.GlobalResponseDTO;
import com.gym.gym_backend.modules.auth.dto.AuthResponseDTO;
import com.gym.gym_backend.modules.auth.dto.LoginRequestDTO;
import com.gym.gym_backend.modules.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management/auth")
@RequiredArgsConstructor
public class ManagementAuthController {

    private final AuthService authService;

    /**
     * Management login only — NO signup endpoint.
     * Management accounts must be created directly in the database.
     */
    @PostMapping("/login")
    public ResponseEntity<GlobalResponseDTO<AuthResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO response = authService.login(request, Role.MANAGEMENT);
        return ResponseEntity.ok(GlobalResponseDTO.success(response, "Management login successful"));
    }
}
