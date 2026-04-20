package com.gym.gym_backend.modules.pkg.entity;

import com.gym.gym_backend.common.base.BaseTenantEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Package extends BaseTenantEntity {

    @Column(name = "gym_id", nullable = false)
    private java.util.UUID gymId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(name = "duration_days", nullable = false)
    private Integer durationDays; // Number of days the package is valid

    @Column(nullable = false)
    private Integer sessions; // Number of sessions included

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PackageType type; // MONTHLY, QUARTERLY, YEARLY, CUSTOM

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "freeze_allowed")
    private Boolean freezeAllowed = false;

    @Column(name = "max_freeze_days")
    private Integer maxFreezeDays;

    private String features; // JSON string of features

    public enum PackageType {
        MONTHLY, QUARTERLY, YEARLY, CUSTOM
    }
}
