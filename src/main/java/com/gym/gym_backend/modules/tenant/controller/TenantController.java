package com.gym.gym_backend.modules.tenant.controller;

import com.gym.gym_backend.modules.tenant.dto.TenantDto;
import com.gym.gym_backend.modules.tenant.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/tenant")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    // POST /admin/tenant  — matches the sequence diagram exactly
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TenantDto.Response> createTenant(
            @Valid @RequestBody TenantDto.CreateRequest request,
            @RequestAttribute("userId") java.util.UUID adminId  // from JwtAuthFilter (already set)
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tenantService.createTenant(request, adminId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TenantDto.Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tenantService.getTenantById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TenantDto.Response>> getMyTenants(
            @RequestAttribute("userId") java.util.UUID adminId
    ) {
        return ResponseEntity.ok(tenantService.getMyTenants(adminId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TenantDto.Response> update(
            @PathVariable Long id,
            @RequestBody TenantDto.UpdateRequest request,
            @RequestAttribute("userId") java.util.UUID adminId
    ) {
        return ResponseEntity.ok(tenantService.updateTenant(id, request, adminId));
    }
}