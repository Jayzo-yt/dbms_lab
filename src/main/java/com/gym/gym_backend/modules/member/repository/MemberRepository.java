package com.gym.gym_backend.modules.member.repository;

import com.gym.gym_backend.modules.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    List<Member> findByTenantIdAndGymId(UUID tenantId, UUID gymId);
    List<Member> findByGymId(UUID gymId);
    Optional<Member> findByEmail(String email);
    List<Member> findByTenantId(UUID tenantId);
    
    @Query("SELECT m FROM Member m WHERE m.tenantId = ?1 AND m.gymId = ?2 AND (m.status = 'ACTIVE' OR m.status = 'INACTIVE')")
    List<Member> findActiveAndInactiveMembers(UUID tenantId, UUID gymId);
}
