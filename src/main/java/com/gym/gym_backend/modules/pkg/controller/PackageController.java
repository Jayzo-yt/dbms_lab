package com.gym.gym_backend.modules.pkg.controller;

import com.gym.gym_backend.common.dto.GlobalResponseDTO;
import com.gym.gym_backend.modules.pkg.dto.PackageDto;
import com.gym.gym_backend.modules.pkg.service.PackageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/staff/gym/{gymId}/packages")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    @PostMapping
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<GlobalResponseDTO<PackageDto.Response>> createPackage(
            @PathVariable UUID gymId,
            @Valid @RequestBody PackageDto.CreateRequest request,
            @RequestAttribute("tenantId") UUID tenantId
    ) {
        PackageDto.Response response = packageService.createPackage(tenantId, gymId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponseDTO.success(response, "Package created successfully"));
    }

    @GetMapping
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<GlobalResponseDTO<List<PackageDto.Response>>> getGymPackages(
            @PathVariable UUID gymId,
            @RequestAttribute("tenantId") UUID tenantId
    ) {
        List<PackageDto.Response> response = packageService.getGymPackages(tenantId, gymId);
        return ResponseEntity.ok(GlobalResponseDTO.success(response, "Packages retrieved successfully"));
    }

    @GetMapping("/{packageId}")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<GlobalResponseDTO<PackageDto.Response>> getPackage(
            @PathVariable UUID gymId,
            @PathVariable UUID packageId,
            @RequestAttribute("tenantId") UUID tenantId
    ) {
        PackageDto.Response response = packageService.getPackage(tenantId, packageId);
        return ResponseEntity.ok(GlobalResponseDTO.success(response, "Package details retrieved"));
    }

    @PutMapping("/{packageId}")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<GlobalResponseDTO<PackageDto.Response>> updatePackage(
            @PathVariable UUID gymId,
            @PathVariable UUID packageId,
            @Valid @RequestBody PackageDto.UpdateRequest request,
            @RequestAttribute("tenantId") UUID tenantId
    ) {
        PackageDto.Response response = packageService.updatePackage(tenantId, packageId, request);
        return ResponseEntity.ok(GlobalResponseDTO.success(response, "Package updated successfully"));
    }

    @DeleteMapping("/{packageId}")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<GlobalResponseDTO<Void>> deletePackage(
            @PathVariable UUID gymId,
            @PathVariable UUID packageId,
            @RequestAttribute("tenantId") UUID tenantId
    ) {
        packageService.deletePackage(tenantId, packageId);
        return ResponseEntity.ok(GlobalResponseDTO.success(null, "Package deleted successfully"));
    }
}
