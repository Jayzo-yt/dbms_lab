package com.gym.gym_backend.modules.staff.service;

import com.gym.gym_backend.common.exception.ResourceNotFoundException;
import com.gym.gym_backend.modules.auth.entity.User;
import com.gym.gym_backend.modules.auth.repository.UserRepository;
import com.gym.gym_backend.modules.staff.dto.StaffDto;
import com.gym.gym_backend.modules.staff.entity.StaffAssignment;
import com.gym.gym_backend.modules.staff.repository.StaffAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffAssignmentRepository staffAssignmentRepository;
    private final UserRepository userRepository;

    @Transactional
    public StaffDto.Response assignStaff(UUID tenantId, UUID gymId, StaffDto.AssignRequest request) {
        User staff = userRepository.findById(request.getStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        StaffAssignment assignment = new StaffAssignment();
        assignment.setTenantId(tenantId);
        assignment.setGymId(gymId);
        assignment.setStaffId(request.getStaffId());
        assignment.setStaffName(staff.getName());
        assignment.setStaffEmail(staff.getEmail());
        assignment.setStaffPhone(staff.getPhone());
        assignment.setRole(request.getRole());
        assignment.setActive(true);

        assignment = staffAssignmentRepository.save(assignment);
        return mapToResponse(assignment);
    }

    public StaffDto.Response getStaffAssignment(UUID tenantId, UUID assignmentId) {
        StaffAssignment assignment = staffAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff assignment not found"));

        if (!assignment.getTenantId().equals(tenantId)) {
            throw new ResourceNotFoundException("Unauthorized");
        }

        return mapToResponse(assignment);
    }

    public List<StaffDto.Response> getGymStaff(UUID tenantId, UUID gymId) {
        return staffAssignmentRepository.findByTenantIdAndGymId(tenantId, gymId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<StaffDto.Response> getTenantStaff(UUID tenantId) {
        return staffAssignmentRepository.findByTenantId(tenantId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public StaffDto.Response updateStaffAssignment(UUID tenantId, UUID assignmentId, StaffDto.UpdateRequest request) {
        StaffAssignment assignment = staffAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff assignment not found"));

        if (!assignment.getTenantId().equals(tenantId)) {
            throw new ResourceNotFoundException("Unauthorized");
        }

        if (request.getRole() != null) {
            assignment.setRole(request.getRole());
        }
        if (request.getActive() != null) {
            assignment.setActive(request.getActive());
        }

        assignment = staffAssignmentRepository.save(assignment);
        return mapToResponse(assignment);
    }

    @Transactional
    public void removeStaff(UUID tenantId, UUID assignmentId) {
        StaffAssignment assignment = staffAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff assignment not found"));

        if (!assignment.getTenantId().equals(tenantId)) {
            throw new ResourceNotFoundException("Unauthorized");
        }

        staffAssignmentRepository.delete(assignment);
    }

    private StaffDto.Response mapToResponse(StaffAssignment assignment) {
        return StaffDto.Response.builder()
                .id(assignment.getId())
                .staffId(assignment.getStaffId())
                .staffName(assignment.getStaffName())
                .staffEmail(assignment.getStaffEmail())
                .staffPhone(assignment.getStaffPhone())
                .gymId(assignment.getGymId())
                .role(assignment.getRole())
                .active(assignment.getActive())
                .createdAt(assignment.getCreatedAt().toString())
                .build();
    }
}
