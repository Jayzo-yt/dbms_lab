package com.gym.gym_backend.modules.profile.service;

import com.gym.gym_backend.modules.auth.entity.User;
import com.gym.gym_backend.modules.auth.repository.UserRepository;
import com.gym.gym_backend.common.exception.UserNotFoundException;
import com.gym.gym_backend.common.service.AuditLogService;
import com.gym.gym_backend.modules.profile.dto.ProfileDTO;
import com.gym.gym_backend.modules.profile.entity.Profile;
import com.gym.gym_backend.modules.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public ProfileDTO getProfile(UUID userId) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElse(null);

        if (profile == null) {
            // Return empty profile shell - never 404 for authenticated user
            return ProfileDTO.builder()
                    .userId(userId)
                    .build();
        }

        return mapToDTO(profile);
    }

    @Transactional
    public ProfileDTO updateProfile(UUID userId, ProfileDTO dto) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new UserNotFoundException("User not found"));
                    Profile newProfile = new Profile();
                    newProfile.setUser(user);
                    return newProfile;
                });

        // Partial update — only update non-null fields
        if (dto.getProfilePhoto() != null) profile.setProfilePhoto(dto.getProfilePhoto());
        if (dto.getHeight() != null) profile.setHeight(dto.getHeight());
        if (dto.getWeight() != null) profile.setWeight(dto.getWeight());
        if (dto.getGender() != null) profile.setGender(dto.getGender());
        if (dto.getAge() != null) profile.setAge(dto.getAge());
        if (dto.getAddress() != null) profile.setAddress(dto.getAddress());

        profile = profileRepository.save(profile);

        auditLogService.log(
                "PROFILE_UPDATED",
                userId,
                profile.getId(),
                "Profile updated for user " + userId
        );

        return mapToDTO(profile);
    }

    private ProfileDTO mapToDTO(Profile profile) {
        return ProfileDTO.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .profilePhoto(profile.getProfilePhoto())
                .height(profile.getHeight())
                .weight(profile.getWeight())
                .gender(profile.getGender())
                .age(profile.getAge())
                .address(profile.getAddress())
                .build();
    }
}
