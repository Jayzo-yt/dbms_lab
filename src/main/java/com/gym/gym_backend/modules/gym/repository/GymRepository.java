package com.gym.gym_backend.modules.gym.repository;

import com.gym.gym_backend.modules.gym.entity.Gym;
import com.gym.gym_backend.modules.gym.entity.GymStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GymRepository extends JpaRepository<Gym, UUID> {

    List<Gym> findByTenantId(UUID tenantId);

    List<Gym> findByTenantIdAndStatus(UUID tenantId, GymStatus status);

    long countByTenantId(UUID tenantId);
}
