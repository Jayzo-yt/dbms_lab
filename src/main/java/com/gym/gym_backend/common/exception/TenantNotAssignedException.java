package com.gym.gym_backend.common.exception;

public class TenantNotAssignedException extends RuntimeException {
    public TenantNotAssignedException(String message) {
        super(message);
    }
}
