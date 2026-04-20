package com.gym.gym_backend.modules.pkg.repository;

import com.gym.gym_backend.modules.pkg.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PackageRepository extends JpaRepository<Package, UUID> {
    List<Package> findByTenantIdAndGymId(UUID tenantId, UUID gymId);
    List<Package> findByGymId(UUID gymId);
    List<Package> findByTenantId(UUID tenantId);
    List<Package> findByGymIdAndActiveTrue(UUID gymId);
}
