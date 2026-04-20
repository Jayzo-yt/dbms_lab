package com.gym.gym_backend.modules.gym.service;

import com.gym.gym_backend.common.service.AuditLogService;
import com.gym.gym_backend.modules.gym.dto.GymDto;
import com.gym.gym_backend.modules.gym.dto.GymSettingsDTO;
import com.gym.gym_backend.modules.gym.entity.Gym;
import com.gym.gym_backend.modules.gym.entity.GymSettings;
import com.gym.gym_backend.modules.gym.entity.GymStatus;
import com.gym.gym_backend.modules.gym.repository.GymRepository;
import com.gym.gym_backend.modules.gym.repository.GymSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GymService {

    private final GymRepository gymRepository;
    private final GymSettingsRepository gymSettingsRepository;
    private final AuditLogService auditLogService;

    /**
     * POST /admin/gym
     * tenantId comes from the JWT token.
     */
    @Transactional
    public GymDto.Response createGym(GymDto.CreateRequest request, UUID tenantId) {
        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant Id missing in JWT");
        }

        Gym gym = new Gym();
        gym.setTenantId(tenantId);
        gym.setGymName(request.getGymName());
        gym.setGymCode(request.getGymCode());
        gym.setGstNumber(request.getGstNumber());
        gym.setAddress(request.getAddress());
        gym.setLocationCoordinates(request.getLocationCoordinates());
        gym.setPhoneNumber(request.getPhoneNumber());
        gym.setEmail(request.getEmail());
        gym.setAdminEmail(request.getAdminEmail());
        gym.setSocialLinks(request.getSocialLinks());
        gym.setGymProfilePhoto(request.getGymProfilePhoto());
        gym.setStatus(GymStatus.ACTIVE);

        Gym saved = gymRepository.save(gym);

        // Audit log
        auditLogService.log("GYM_CREATED", tenantId, saved.getId(), "Gym " + saved.getGymName() + " created");

        return toResponse(saved);
    }

    /**
     * GET /admin/gyms
     * Returns all gyms belonging to the admin's tenantId (from JWT).
     */
    @Transactional(readOnly = true)
    public List<GymDto.Response> getGymsByTenant(UUID tenantId) {
        return gymRepository.findByTenantId(tenantId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GymDto.Response getGymById(UUID gymId) {
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new RuntimeException("Gym not found: " + gymId));
        return toResponse(gym);
    }

    /**
     * PUT /admin/gym/{gymId}
     * Updates gym profile. Ensures the gym belongs to the requester's tenant.
     */
    @Transactional
    public GymDto.Response updateGym(UUID gymId, GymDto.UpdateRequest request, UUID tenantId) {
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new RuntimeException("Gym not found: " + gymId));

        if (!gym.getTenantId().equals(tenantId)) {
            throw new RuntimeException("Access denied: gym does not belong to your tenant");
        }

        if (request.getGymName() != null) gym.setGymName(request.getGymName());
        if (request.getGymCode() != null) gym.setGymCode(request.getGymCode());
        if (request.getGstNumber() != null) gym.setGstNumber(request.getGstNumber());
        if (request.getAddress() != null) gym.setAddress(request.getAddress());
        if (request.getLocationCoordinates() != null) gym.setLocationCoordinates(request.getLocationCoordinates());
        if (request.getPhoneNumber() != null) gym.setPhoneNumber(request.getPhoneNumber());
        if (request.getEmail() != null) gym.setEmail(request.getEmail());
        if (request.getAdminEmail() != null) gym.setAdminEmail(request.getAdminEmail());
        if (request.getSocialLinks() != null) gym.setSocialLinks(request.getSocialLinks());
        if (request.getGymProfilePhoto() != null) gym.setGymProfilePhoto(request.getGymProfilePhoto());
        if (request.getStatus() != null) {
            gym.setStatus(GymStatus.valueOf(request.getStatus()));
        }

        Gym saved = gymRepository.save(gym);

        // Audit log
        auditLogService.log("GYM_UPDATED", tenantId, saved.getId(), "Gym " + saved.getGymName() + " updated");

        return toResponse(saved);
    }

    @Transactional
    public void deleteGym(UUID gymId, UUID tenantId) {
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new RuntimeException("Gym not found: " + gymId));

        if (!gym.getTenantId().equals(tenantId)) {
            throw new RuntimeException("Access denied: gym does not belong to your tenant");
        }

        gym.setStatus(GymStatus.INACTIVE);
        gymRepository.save(gym);

        // Audit log
        auditLogService.log("GYM_DELETED", tenantId, gym.getId(), "Gym " + gym.getGymName() + " deactivated");
    }

    /**
     * PUT /admin/gym/{gymId}/settings
     */
    @Transactional
    public GymSettingsDTO.Response updateGymSettings(UUID gymId, GymSettingsDTO.UpdateRequest request, UUID tenantId) {
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new RuntimeException("Gym not found: " + gymId));

        if (!gym.getTenantId().equals(tenantId)) {
            throw new RuntimeException("Access denied: gym does not belong to your tenant");
        }

        GymSettings settings = gymSettingsRepository.findByGymId(gymId)
                .orElse(new GymSettings());
        settings.setTenantId(tenantId);
        settings.setGymId(gymId);

        if (request.getCrowdLimit() != null) settings.setCrowdLimit(request.getCrowdLimit());
        if (request.getSessionDurationMinutes() != null) settings.setSessionDurationMinutes(request.getSessionDurationMinutes());
        if (request.getMinimumValidMinutes() != null) settings.setMinimumValidMinutes(request.getMinimumValidMinutes());
        if (request.getExpiryAlertDays() != null) settings.setExpiryAlertDays(request.getExpiryAlertDays());
        if (request.getAbsentAlertDays() != null) settings.setAbsentAlertDays(request.getAbsentAlertDays());

        GymSettings saved = gymSettingsRepository.save(settings);

        // Audit log
        auditLogService.log("GYM_SETTINGS_UPDATED", tenantId, gym.getId(), "Settings updated for gym " + gym.getGymName());

        return toSettingsResponse(saved);
    }

    /**
     * GET /admin/gym/{gymId}/settings
     */
    @Transactional(readOnly = true)
    public GymSettingsDTO.Response getGymSettings(UUID gymId, UUID tenantId) {
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new RuntimeException("Gym not found: " + gymId));

        if (!gym.getTenantId().equals(tenantId)) {
            throw new RuntimeException("Access denied: gym does not belong to your tenant");
        }

        GymSettings settings = gymSettingsRepository.findByGymId(gymId)
                .orElse(new GymSettings());
        settings.setTenantId(tenantId);
        settings.setGymId(gymId);
        settings.setCrowdLimit(50); // default
        settings.setSessionDurationMinutes(60);
        settings.setMinimumValidMinutes(10);
        settings.setExpiryAlertDays(3);
        settings.setAbsentAlertDays(7);

        return toSettingsResponse(settings);
    }

    // ---- helpers ----

    private GymDto.Response toResponse(Gym g) {
        GymDto.Response response = new GymDto.Response();
        response.setId(g.getId());
        response.setTenantId(g.getTenantId());
        response.setGymName(g.getGymName());
        response.setGymCode(g.getGymCode());
        response.setGstNumber(g.getGstNumber());
        response.setAddress(g.getAddress());
        response.setLocationCoordinates(g.getLocationCoordinates());
        response.setPhoneNumber(g.getPhoneNumber());
        response.setEmail(g.getEmail());
        response.setAdminEmail(g.getAdminEmail());
        response.setSocialLinks(g.getSocialLinks());
        response.setGymProfilePhoto(g.getGymProfilePhoto());
        response.setStatus(g.getStatus().name());
        response.setCreatedAt(g.getCreatedAt());
        response.setUpdatedAt(g.getUpdatedAt());
        return response;
    }

    private GymSettingsDTO.Response toSettingsResponse(GymSettings s) {
        GymSettingsDTO.Response response = new GymSettingsDTO.Response();
        response.setId(s.getId());
        response.setTenantId(s.getTenantId());
        response.setGymId(s.getGymId());
        response.setCrowdLimit(s.getCrowdLimit());
        response.setSessionDurationMinutes(s.getSessionDurationMinutes());
        response.setMinimumValidMinutes(s.getMinimumValidMinutes());
        response.setExpiryAlertDays(s.getExpiryAlertDays());
        response.setAbsentAlertDays(s.getAbsentAlertDays());
        response.setCreatedAt(s.getCreatedAt());
        response.setUpdatedAt(s.getUpdatedAt());
        return response;
    }
}
