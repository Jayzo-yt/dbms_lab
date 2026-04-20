package com.gym.gym_backend.modules.tenant.service;

import com.gym.gym_backend.common.constants.Role;
import com.gym.gym_backend.modules.auth.entity.User;
import com.gym.gym_backend.modules.auth.entity.UserRole;
import com.gym.gym_backend.modules.auth.repository.UserRepository;
import com.gym.gym_backend.modules.tenant.dto.TenantDto;
import com.gym.gym_backend.modules.tenant.entity.Tenant;
import com.gym.gym_backend.modules.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    @Transactional
    public TenantDto.Response createTenant(TenantDto.CreateRequest request, java.util.UUID adminId) {
        if (tenantRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Tenant with this email already exists");
        }

        Tenant tenant = Tenant.builder()
                .tenantCode(generateTenantCode())
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .adminId(adminId)
                .build();

        tenant = tenantRepository.save(tenant);

        // assign tenant to admin role for downstream operations like gym creation
        assignTenantToAdmin(adminId, tenant.getId());

        return toResponse(tenant);
    }

    private void assignTenantToAdmin(java.util.UUID adminId, java.util.UUID tenantId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin user not found: " + adminId));

        UserRole adminRole = admin.getRoles().stream()
                .filter(r -> r.getRole() == Role.ADMIN)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Admin role not found for user: " + adminId));

        adminRole.setTenantId(tenantId);
        // Some DB schemas may define user_roles.gym_id as UUID; clear now to avoid bigint->uuid cast failure.
        adminRole.setGymId(null);

        userRepository.save(admin);
    }

    @Transactional(readOnly = true)
    public TenantDto.Response getTenantById(Long id) {
        return toResponse(tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant not found: " + id)));
    }

    @Transactional(readOnly = true)
    public List<TenantDto.Response> getMyTenants(java.util.UUID adminId) {
        return tenantRepository.findByAdminId(adminId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public TenantDto.Response updateTenant(Long id, TenantDto.UpdateRequest request, java.util.UUID adminId) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant not found: " + id));

        // Only the admin who owns this tenant can update it
        if (!tenant.getAdminId().equals(adminId)) {
            throw new RuntimeException("Access denied: not your tenant");
        }

        if (request.getName() != null)    tenant.setName(request.getName());
        if (request.getPhone() != null)   tenant.setPhone(request.getPhone());
        if (request.getAddress() != null) tenant.setAddress(request.getAddress());

        return toResponse(tenantRepository.save(tenant));
    }

    // ---- helpers ----

    private String generateTenantCode() {
        String code;
        do {
            code = "TEN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (tenantRepository.existsByTenantCode(code));
        return code;
    }

    private TenantDto.Response toResponse(Tenant t) {
        return TenantDto.Response.builder()
                .id(t.getId()).tenantCode(t.getTenantCode())
                .name(t.getName()).email(t.getEmail())
                .phone(t.getPhone()).address(t.getAddress())
                .status(t.getStatus().name()).adminId(t.getAdminId())
                .build();
    }
}