package com.gym.gym_backend.modules.staff.repository;

import com.gym.gym_backend.modules.staff.entity.StaffAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StaffAssignmentRepository extends JpaRepository<StaffAssignment, UUID> {
    List<StaffAssignment> findByTenantIdAndGymId(UUID tenantId, UUID gymId);
    List<StaffAssignment> findByTenantId(UUID tenantId);
    List<StaffAssignment> findByStaffId(UUID staffId);
    Optional<StaffAssignment> findByStaffIdAndGymId(UUID staffId, UUID gymId);
    List<StaffAssignment> findByGymId(UUID gymId);
}
