package com.gym.gym_backend.modules.auth.service;

import com.gym.gym_backend.common.constants.Role;
import com.gym.gym_backend.common.exception.*;
import com.gym.gym_backend.common.service.AuditLogService;
import com.gym.gym_backend.modules.auth.dto.*;
import com.gym.gym_backend.modules.auth.entity.User;
import com.gym.gym_backend.modules.auth.entity.UserRole;
import com.gym.gym_backend.modules.auth.repository.UserRepository;
import java.util.ArrayList;
import java.util.Comparator;
import com.gym.gym_backend.modules.gym.entity.Gym;
import com.gym.gym_backend.modules.gym.entity.GymStatus;
import com.gym.gym_backend.modules.gym.repository.GymRepository;
import com.gym.gym_backend.modules.profile.entity.Profile;
import com.gym.gym_backend.modules.profile.repository.ProfileRepository;
import com.gym.gym_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final GymRepository gymRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuditLogService auditLogService;

    /**
     * Admin Signup — POST /auth/admin/signup
     * Creates admin with role=ADMIN, tenantId=null, gymId=null.
     * Returns only userId (no token) — token comes from login only.
     */
    @Transactional
    public AdminSignupResponseDTO signup(SignupRequestDTO request, Role role) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        // Create user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setRoles(new ArrayList<>());

        // Create user role
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setTenantId(null);
        userRole.setGymId(null);
        user.getRoles().add(userRole);

        user = userRepository.save(user);

        // Create empty profile
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setAge(request.getAge());
        profileRepository.save(profile);

        // Audit log
        auditLogService.log(
                role.name() + "_SIGNUP",
                user.getId(),
                user.getId(),
                role.name() + " account created for " + user.getEmail()
        );

        // No token at signup — token only from login
        return AdminSignupResponseDTO.builder()
                .userId(user.getId())
                .build();
    }

    /**
     * Login — works for ADMIN, STAFF, USER, MANAGEMENT.
     * Loads user from DB, reads stored tenantId and gymId from the matching role.
     * Generates JWT with claims: { userId, role, tenantId, gymId }.
     * Never rejects login if tenantId is null.
     */
    public AuthResponseDTO login(LoginRequestDTO request, Role role) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // Find the matching role
        UserRole userRole = user.getRoles().stream()
                .filter(r -> r.getRole() == role)
                .findFirst()
                .orElseThrow(() -> new InvalidCredentialsException("User does not have " + role.name() + " access"));

        java.util.UUID tenantId = userRole.getTenantId();
        java.util.UUID gymId = userRole.getGymId();

        String token = jwtService.generateToken(user, role, tenantId, gymId);

        auditLogService.log(
                role.name() + "_LOGIN",
                user.getId(),
                user.getId(),
                role.name() + " login for " + user.getEmail()
        );

        return AuthResponseDTO.builder()
                .token(token)
                .userId(user.getId())
                .role(role.name())
                .tenantId(tenantId)
                .gymId(gymId)
                .build();
    }

    /**
     * Generic login for normal login screen.
     * Resolves role from stored user roles and returns token with that role context.
     */
    public AuthResponseDTO loginByCredentials(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        UserRole selectedRole = user.getRoles().stream()
                .min(Comparator.comparingInt(r -> rolePriority(r.getRole())))
                .orElseThrow(() -> new InvalidCredentialsException("No role assigned to this account"));

        Role role = selectedRole.getRole();
        java.util.UUID tenantId = selectedRole.getTenantId();
        java.util.UUID gymId = selectedRole.getGymId();

        String token = jwtService.generateToken(user, role, tenantId, gymId);

        auditLogService.log(
                role.name() + "_LOGIN",
                user.getId(),
                user.getId(),
                "Role-resolved login for " + user.getEmail()
        );

        return AuthResponseDTO.builder()
                .token(token)
                .userId(user.getId())
                .role(role.name())
                .tenantId(tenantId)
                .gymId(gymId)
                .build();
    }

    /**
     * Admin creates login credentials for STAFF or USER(member).
     */
    @Transactional
    public AdminSignupResponseDTO createRoleUserByAdmin(AdminCreateUserRequestDTO request) {
        if (request.getRole() == null || (request.getRole() != Role.STAFF && request.getRole() != Role.USER)) {
            throw new IllegalArgumentException("Only STAFF or USER role can be created by admin");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        java.util.UUID resolvedTenantId = null;
        java.util.UUID resolvedGymId = request.getGymId();

        if (resolvedGymId != null) {
            Gym gym = gymRepository.findById(resolvedGymId)
                    .orElseThrow(() -> new GymNotFoundException("Gym not found with ID: " + resolvedGymId));
            if (!gym.getStatus().equals(GymStatus.ACTIVE)) {
                throw new GymNotActiveException("Gym is not active");
            }
            resolvedTenantId = gym.getTenantId();
        }

        if (request.getRole() == Role.STAFF && resolvedGymId == null) {
            throw new IllegalArgumentException("Gym ID is required for STAFF account creation");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setRoles(new ArrayList<>());

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(request.getRole());
        userRole.setTenantId(resolvedTenantId);
        userRole.setGymId(resolvedGymId);
        user.getRoles().add(userRole);

        user = userRepository.save(user);

        Profile profile = new Profile();
        profile.setUser(user);
        profileRepository.save(profile);

        auditLogService.log(
                "ADMIN_CREATED_" + request.getRole().name(),
                user.getId(),
                user.getId(),
                "Account created by admin for " + user.getEmail() + " as " + request.getRole().name()
        );

        return AdminSignupResponseDTO.builder()
                .userId(user.getId())
                .build();
    }

    @Transactional
    public void changePassword(java.util.UUID userId, ChangePasswordRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private int rolePriority(Role role) {
        if (role == Role.MANAGEMENT) return 0;
        if (role == Role.ADMIN) return 1;
        if (role == Role.STAFF) return 2;
        return 3;
    }

    /**
     * Staff Signup — POST /auth/staff/signup
     * Accepts: name, email, password, gymId.
     * tenantId is NEVER accepted from request — always resolved from gym lookup.
     * Guards against inactive gyms.
     */
    @Transactional
    public StaffSignupResponseDTO staffSignup(StaffSignupRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        // Lookup gym to resolve tenantId — never accept tenantId from request
        java.util.UUID gymUuid = request.getGymId();
        Gym gym = gymRepository.findById(gymUuid)
                .orElseThrow(() -> new GymNotFoundException("Gym not found with ID: " + gymUuid));

        // Guard: only allow signup at active gyms
        if (!gym.getStatus().equals(GymStatus.ACTIVE)) {
            throw new GymNotActiveException("Gym is not active");
        }

        java.util.UUID tenantId = gym.getTenantId();
        if (tenantId == null) {
            throw new IllegalStateException("Gym " + gymUuid + " has no tenant assigned yet");
        }
        java.util.UUID gymId = gymUuid;

        // Create user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRoles(new ArrayList<>());

        // Create staff role with both tenantId and gymId resolved from gym lookup
        UserRole staffRole = new UserRole();
        staffRole.setUser(user);
        staffRole.setRole(Role.STAFF);
        staffRole.setTenantId(tenantId);
        staffRole.setGymId(gymId);
        user.getRoles().add(staffRole);

        user = userRepository.save(user);

        // Create empty profile
        Profile profile = new Profile();
        profile.setUser(user);
        profileRepository.save(profile);

        auditLogService.log(
                "STAFF_SIGNUP",
                user.getId(),
                user.getId(),
                "Staff account created for " + user.getEmail() + " at gym " + gymId + " under tenant " + tenantId
        );

        // No token at signup — token only from login
        return StaffSignupResponseDTO.builder()
                .userId(user.getId())
                .tenantId(tenantId)
                .gymId(gymId)
                .build();
    }
}
