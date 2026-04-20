package com.gym.gym_backend.modules.auth.entity;

import com.gym.gym_backend.common.base.BaseEntity;
import com.gym.gym_backend.common.constants.Role;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "user_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRole extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "tenant_id")
    private java.util.UUID tenantId;

    @Column(name = "gym_id")
    private java.util.UUID gymId;
}
