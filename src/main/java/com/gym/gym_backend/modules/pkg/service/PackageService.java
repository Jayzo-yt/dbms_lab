package com.gym.gym_backend.modules.pkg.service;

import com.gym.gym_backend.common.exception.ResourceNotFoundException;
import com.gym.gym_backend.modules.pkg.dto.PackageDto;
import com.gym.gym_backend.modules.pkg.entity.Package;
import com.gym.gym_backend.modules.pkg.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;

    @Transactional
    public PackageDto.Response createPackage(UUID tenantId, UUID gymId, PackageDto.CreateRequest request) {
        Package pkg = new Package();
        pkg.setTenantId(tenantId);
        pkg.setGymId(gymId);
        pkg.setName(request.getName());
        pkg.setDescription(request.getDescription());
        pkg.setPrice(request.getPrice());
        pkg.setDurationDays(request.getDurationDays());
        pkg.setSessions(request.getSessions());
        pkg.setType(request.getType());
        pkg.setActive(true);
        pkg.setFreezeAllowed(request.getFreezeAllowed() != null ? request.getFreezeAllowed() : false);
        pkg.setMaxFreezeDays(request.getMaxFreezeDays());
        pkg.setFeatures(request.getFeatures());

        pkg = packageRepository.save(pkg);
        return mapToResponse(pkg);
    }

    public PackageDto.Response getPackage(UUID tenantId, UUID packageId) {
        Package pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));

        if (!pkg.getTenantId().equals(tenantId)) {
            throw new ResourceNotFoundException("Unauthorized");
        }

        return mapToResponse(pkg);
    }

    public List<PackageDto.Response> getGymPackages(UUID tenantId, UUID gymId) {
        return packageRepository.findByTenantIdAndGymId(tenantId, gymId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PackageDto.Response> getActiveGymPackages(UUID gymId) {
        return packageRepository.findByGymIdAndActiveTrue(gymId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PackageDto.Response> getTenantPackages(UUID tenantId) {
        return packageRepository.findByTenantId(tenantId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PackageDto.Response updatePackage(UUID tenantId, UUID packageId, PackageDto.UpdateRequest request) {
        Package pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));

        if (!pkg.getTenantId().equals(tenantId)) {
            throw new ResourceNotFoundException("Unauthorized");
        }

        if (request.getName() != null) pkg.setName(request.getName());
        if (request.getDescription() != null) pkg.setDescription(request.getDescription());
        if (request.getPrice() != null) pkg.setPrice(request.getPrice());
        if (request.getDurationDays() != null) pkg.setDurationDays(request.getDurationDays());
        if (request.getSessions() != null) pkg.setSessions(request.getSessions());
        if (request.getActive() != null) pkg.setActive(request.getActive());
        if (request.getFreezeAllowed() != null) pkg.setFreezeAllowed(request.getFreezeAllowed());
        if (request.getMaxFreezeDays() != null) pkg.setMaxFreezeDays(request.getMaxFreezeDays());
        if (request.getFeatures() != null) pkg.setFeatures(request.getFeatures());

        pkg = packageRepository.save(pkg);
        return mapToResponse(pkg);
    }

    @Transactional
    public void deletePackage(UUID tenantId, UUID packageId) {
        Package pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));

        if (!pkg.getTenantId().equals(tenantId)) {
            throw new ResourceNotFoundException("Unauthorized");
        }

        packageRepository.delete(pkg);
    }

    private PackageDto.Response mapToResponse(Package pkg) {
        return PackageDto.Response.builder()
                .id(pkg.getId())
                .name(pkg.getName())
                .description(pkg.getDescription())
                .price(pkg.getPrice())
                .durationDays(pkg.getDurationDays())
                .sessions(pkg.getSessions())
                .type(pkg.getType().toString())
                .active(pkg.getActive())
                .freezeAllowed(pkg.getFreezeAllowed())
                .maxFreezeDays(pkg.getMaxFreezeDays())
                .features(pkg.getFeatures())
                .createdAt(pkg.getCreatedAt().toString())
                .build();
    }
}
