package com.gym.gym_backend.modules.staff.controller;

import com.gym.gym_backend.common.dto.GlobalResponseDTO;
import com.gym.gym_backend.modules.staff.dto.StaffDto;
import com.gym.gym_backend.modules.staff.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/gym/{gymId}/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponseDTO<StaffDto.Response>> assignStaff(
            @PathVariable UUID gymId,
            @Valid @RequestBody StaffDto.AssignRequest request,
            @RequestAttribute("tenantId") UUID tenantId
    ) {
        StaffDto.Response response = staffService.assignStaff(tenantId, gymId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponseDTO.success(response, "Staff assigned successfully"));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponseDTO<List<StaffDto.Response>>> getGymStaff(
            @PathVariable UUID gymId,
            @RequestAttribute("tenantId") UUID tenantId
    ) {
        List<StaffDto.Response> response = staffService.getGymStaff(tenantId, gymId);
        return ResponseEntity.ok(GlobalResponseDTO.success(response, "Staff retrieved successfully"));
    }

    @GetMapping("/{staffId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponseDTO<StaffDto.Response>> getStaffAssignment(
            @PathVariable UUID gymId,
            @PathVariable UUID staffId,
            @RequestAttribute("tenantId") UUID tenantId
    ) {
        StaffDto.Response response = staffService.getStaffAssignment(tenantId, staffId);
        return ResponseEntity.ok(GlobalResponseDTO.success(response, "Staff details retrieved"));
    }

    @PutMapping("/{staffId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponseDTO<StaffDto.Response>> updateStaff(
            @PathVariable UUID gymId,
            @PathVariable UUID staffId,
            @Valid @RequestBody StaffDto.UpdateRequest request,
            @RequestAttribute("tenantId") UUID tenantId
    ) {
        StaffDto.Response response = staffService.updateStaffAssignment(tenantId, staffId, request);
        return ResponseEntity.ok(GlobalResponseDTO.success(response, "Staff updated successfully"));
    }

    @DeleteMapping("/{staffId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponseDTO<Void>> removeStaff(
            @PathVariable UUID gymId,
            @PathVariable UUID staffId,
            @RequestAttribute("tenantId") UUID tenantId
    ) {
        staffService.removeStaff(tenantId, staffId);
        return ResponseEntity.ok(GlobalResponseDTO.success(null, "Staff removed successfully"));
    }
}
