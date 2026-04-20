package com.gym.gym_backend.modules.tenant.repository;

import com.gym.gym_backend.modules.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findByTenantCode(String tenantCode);
    List<Tenant> findByAdminId(java.util.UUID adminId);
    boolean existsByEmail(String email);
    boolean existsByTenantCode(String tenantCode);
}