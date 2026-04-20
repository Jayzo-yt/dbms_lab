package com.gym.gym_backend.modules.profile.entity;

import com.gym.gym_backend.common.base.BaseEntity;
import com.gym.gym_backend.modules.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "profile_photo", length = 500)
    private String profilePhoto;

    @Column(precision = 5, scale = 2)
    private BigDecimal height;

    @Column(precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(length = 20)
    private String gender;

    private Integer age;

    @Column(columnDefinition = "TEXT")
    private String address;
}
