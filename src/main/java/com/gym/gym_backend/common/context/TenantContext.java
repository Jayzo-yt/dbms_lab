package com.gym.gym_backend.common.context;

import java.util.UUID;

public class TenantContext {

    private static final ThreadLocal<UUID> currentTenant = new ThreadLocal<>();
    private static final ThreadLocal<UUID> currentGym = new ThreadLocal<>();

    public static void setTenantId(UUID tenantId) {
        currentTenant.set(tenantId);
    }

    public static UUID getTenantId() {
        return currentTenant.get();
    }

    public static void setGymId(UUID gymId) {
        currentGym.set(gymId);
    }

    public static UUID getGymId() {
        return currentGym.get();
    }

    public static void clear() {
        currentTenant.remove();
        currentGym.remove();
    }
}