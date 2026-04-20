package com.gym.gym_backend.modules.gym.repository;

import com.gym.gym_backend.modules.gym.entity.GymSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GymSettingsRepository extends JpaRepository<GymSettings, UUID> {

    Optional<GymSettings> findByGymId(UUID gymId);
}