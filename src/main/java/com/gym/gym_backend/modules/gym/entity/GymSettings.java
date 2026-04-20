package com.gym.gym_backend.modules.gym.entity;

import com.gym.gym_backend.common.base.BaseTenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "gym_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GymSettings extends BaseTenantEntity {

    @Column(name = "crowd_limit")
    private Integer crowdLimit;

    @Column(name = "session_duration_minutes")
    private Integer sessionDurationMinutes;

    @Column(name = "minimum_valid_minutes")
    private Integer minimumValidMinutes;

    @Column(name = "expiry_alert_days")
    private Integer expiryAlertDays;

    @Column(name = "absent_alert_days")
    private Integer absentAlertDays;
}