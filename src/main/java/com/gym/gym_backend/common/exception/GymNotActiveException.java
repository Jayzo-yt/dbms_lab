package com.gym.gym_backend.common.exception;

public class GymNotActiveException extends RuntimeException {
    public GymNotActiveException(String message) {
        super(message);
    }
}
