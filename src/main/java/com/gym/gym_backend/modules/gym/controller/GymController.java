package com.gym.gym_backend.modules.gym.controller;

import com.gym.gym_backend.common.context.TenantContext;
import com.gym.gym_backend.modules.auth.entity.User;
import com.gym.gym_backend.modules.auth.repository.UserRepository;
import com.gym.gym_backend.modules.gym.dto.GymDto;
import com.gym.gym_backend.modules.gym.dto.GymSettingsDTO;
import com.gym.gym_backend.modules.gym.service.GymService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GymController {

    private final GymService gymService;
    private final UserRepository userRepository;

    /**
     * POST /admin/gym
     * tenantId is extracted from the JWT by the JwtAuthFilter.
     */
    @PostMapping("/admin/gym")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GymDto.Response> createGym(
            @Valid @RequestBody GymDto.CreateRequest request,
            @RequestAttribute(name = "tenantId", required = false) UUID tenantId,
            @RequestAttribute(name = "userId", required = false) UUID userId
    ) {
        if (tenantId == null) {
            tenantId = TenantContext.getTenantId();
        }

        if (tenantId == null && userId != null) {
            tenantId = findTenantIdForAdmin(userId);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gymService.createGym(request, tenantId));
    }

    /**
     * GET /admin/gyms
     * Fetches gyms by tenantId from JWT.
     */
    @GetMapping("/admin/gyms")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGEMENT')")
    public ResponseEntity<List<GymDto.Response>> getGyms(
            @RequestAttribute(name = "tenantId", required = false) UUID tenantId,
            @RequestAttribute(name = "userId", required = false) UUID userId
    ) {
        if (tenantId == null) {
            tenantId = TenantContext.getTenantId();
        }

        if (tenantId == null && userId != null) {
            tenantId = findTenantIdForAdmin(userId);
        }

        return ResponseEntity.ok(gymService.getGymsByTenant(tenantId));
    }

    @GetMapping("/admin/gym/{gymId}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','MANAGEMENT')")
    public ResponseEntity<GymDto.Response> getGymById(@PathVariable UUID gymId) {
        return ResponseEntity.ok(gymService.getGymById(gymId));
    }

    /**
     * PUT /admin/gym/{gymId}
     * Update gym profile. Validates tenantId from JWT matches gym's tenant.
     */
    @PutMapping("/admin/gym/{gymId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GymDto.Response> updateGym(
            @PathVariable UUID gymId,
            @RequestBody GymDto.UpdateRequest request,
            @RequestAttribute("tenantId") UUID tenantId
    ) {
        return ResponseEntity.ok(gymService.updateGym(gymId, request, tenantId));
    }

    @DeleteMapping("/admin/gym/{gymId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGym(
            @PathVariable UUID gymId,
            @RequestAttribute(name = "tenantId", required = false) UUID tenantId,
            @RequestAttribute(name = "userId", required = false) UUID userId
    ) {
        if (tenantId == null) {
            tenantId = TenantContext.getTenantId();
        }

        if (tenantId == null && userId != null) {
            tenantId = findTenantIdForAdmin(userId);
        }

        gymService.deleteGym(gymId, tenantId);
        return ResponseEntity.noContent().build();
    }

    /**
     * PUT /admin/gym/{gymId}/settings
     */
    @PutMapping("/admin/gym/{gymId}/settings")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF','MANAGEMENT')")
    public ResponseEntity<GymSettingsDTO.Response> updateGymSettings(
            @PathVariable UUID gymId,
            @RequestBody GymSettingsDTO.UpdateRequest request,
            @RequestAttribute("tenantId") UUID tenantId
    ) {
        return ResponseEntity.ok(gymService.updateGymSettings(gymId, request, tenantId));
    }

    /**
     * GET /admin/gym/{gymId}/settings
     */
    @GetMapping("/admin/gym/{gymId}/settings")
        @PreAuthorize("hasAnyRole('ADMIN','STAFF','MANAGEMENT')")
    public ResponseEntity<GymSettingsDTO.Response> getGymSettings(
            @PathVariable UUID gymId,
            @RequestAttribute("tenantId") UUID tenantId
    ) {
        return ResponseEntity.ok(gymService.getGymSettings(gymId, tenantId));
    }

    private UUID findTenantIdForAdmin(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Admin user not found for tenant resolution: " + userId))
                .getRoles().stream()
                .filter(r -> r.getRole() != null && r.getRole().name().equals("ADMIN"))
                .findFirst()
                .map(r -> r.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant ID not assigned to admin roles for user: " + userId));
    }
}
