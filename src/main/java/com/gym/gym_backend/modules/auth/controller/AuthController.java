package com.gym.gym_backend.modules.auth.controller;

import com.gym.gym_backend.common.dto.GlobalResponseDTO;
import com.gym.gym_backend.modules.auth.dto.AuthResponseDTO;
import com.gym.gym_backend.modules.auth.dto.ChangePasswordRequestDTO;
import com.gym.gym_backend.modules.auth.dto.LoginRequestDTO;
import com.gym.gym_backend.modules.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<GlobalResponseDTO<AuthResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO response = authService.loginByCredentials(request);
        return ResponseEntity.ok(GlobalResponseDTO.success(response, "Login successful"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<GlobalResponseDTO<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequestDTO request,
            @RequestAttribute("userId") java.util.UUID userId
    ) {
        authService.changePassword(userId, request);
        return ResponseEntity.ok(GlobalResponseDTO.success(null, "Password changed successfully"));
    }
}
