package com.gym.gym_backend.modules.gym.entity;

import com.gym.gym_backend.common.base.BaseTenantEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "gyms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gym extends BaseTenantEntity {

    @Column(name = "gym_name", nullable = false)
    private String gymName;

    @Column(name = "gym_code", unique = true)
    private String gymCode;

    @Column(name = "gst_number")
    private String gstNumber;

    @Column(nullable = false)
    private String address;

    @Column(name = "location_coordinates")
    private String locationCoordinates; // e.g. "12.9716,77.5946"

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(name = "admin_email")
    private String adminEmail;

    @Column(name = "social_links")
    private String socialLinks; // JSON string

    @Column(name = "gym_profile_photo")
    private String gymProfilePhoto; // URL or path

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GymStatus status;
}
