package com.gym.gym_backend.security;

import com.gym.gym_backend.common.constants.Role;
import com.gym.gym_backend.common.context.TenantContext;
import com.gym.gym_backend.modules.auth.entity.User;
import com.gym.gym_backend.modules.auth.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String jwt = authHeader.substring(7);
            String userEmail;
            try {
                userEmail = jwtService.extractEmail(jwt);
            } catch (Exception ex) {
                // Invalid token format/signature/expiry should not block public endpoints.
                filterChain.doFilter(request, response);
                return;
            }

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepository.findByEmail(userEmail).orElse(null);

                if (user != null) {
                    // Create UserDetails-compatible authentication
                    String role = jwtService.extractRole(jwt);
                    org.springframework.security.core.userdetails.User userDetails =
                            new org.springframework.security.core.userdetails.User(
                                    user.getEmail(),
                                    user.getPassword(),
                                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                            );

                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);

                        // Set tenant context from JWT — both tenantId and gymId
                        java.util.UUID tenantId = jwtService.extractTenantId(jwt);
                        java.util.UUID gymId = jwtService.extractGymId(jwt);

                        // fallback to DB user role when token lacks tenant/gym claims
                        if ((tenantId == null || gymId == null) && user.getRoles() != null) {
                            var matchedRole = user.getRoles().stream()
                                    .filter(r -> r.getRole() != null && r.getRole().name().equalsIgnoreCase(role))
                                    .findFirst();

                            if (matchedRole.isPresent()) {
                                var r = matchedRole.get();
                                if (tenantId == null) {
                                    tenantId = r.getTenantId();
                                }
                                if (gymId == null) {
                                    gymId = r.getGymId();
                                }
                            }
                        }

                        if (tenantId != null) {
                            TenantContext.setTenantId(tenantId);
                            request.setAttribute("tenantId", tenantId);
                        }
                        if (gymId != null) {
                            TenantContext.setGymId(gymId);
                            request.setAttribute("gymId", gymId);
                        }

                        // Set userId for @RequestAttribute in controllers
                        request.setAttribute("userId", user.getId());
                    }
                }
            }

            filterChain.doFilter(request, response);
        } finally {
            // Always clear to prevent thread-local leaks
            TenantContext.clear();
        }
    }
}
