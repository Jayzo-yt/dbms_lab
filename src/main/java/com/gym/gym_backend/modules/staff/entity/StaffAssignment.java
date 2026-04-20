package com.gym.gym_backend.modules.staff.entity;

import com.gym.gym_backend.common.base.BaseTenantEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "staff_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffAssignment extends BaseTenantEntity {

    @Column(name = "staff_id", nullable = false)
    private java.util.UUID staffId;

    @Column(name = "gym_id", nullable = false)
    private java.util.UUID gymId;

    @Column(name = "staff_name")
    private String staffName;

    @Column(name = "staff_email")
    private String staffEmail;

    @Column(name = "staff_phone")
    private String staffPhone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StaffRole role; // MANAGER, TRAINER, RECEPTIONIST, etc.

    @Column(nullable = false)
    private Boolean active = true;
}
