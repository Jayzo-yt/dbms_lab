package com.gym.gym_backend.common.exception;

import com.gym.gym_backend.common.dto.GlobalResponseDTO;
import com.gym.gym_backend.common.exception.GymNotActiveException;
import com.gym.gym_backend.common.exception.GymNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GlobalResponseDTO<?>> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(GlobalResponseDTO.error(ex.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<GlobalResponseDTO<?>> handleEmailExists(EmailAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(GlobalResponseDTO.error(ex.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<GlobalResponseDTO<?>> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(GlobalResponseDTO.error(ex.getMessage()));
    }

    @ExceptionHandler(TenantNotAssignedException.class)
    public ResponseEntity<GlobalResponseDTO<?>> handleTenantNotAssigned(TenantNotAssignedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(GlobalResponseDTO.error(ex.getMessage()));
    }

    @ExceptionHandler(GymNotFoundException.class)
    public ResponseEntity<GlobalResponseDTO<?>> handleGymNotFound(GymNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(GlobalResponseDTO.error(ex.getMessage()));
    }

    @ExceptionHandler(GymNotActiveException.class)
    public ResponseEntity<GlobalResponseDTO<?>> handleGymNotActive(GymNotActiveException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(GlobalResponseDTO.error(ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<GlobalResponseDTO<?>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(GlobalResponseDTO.error("You do not have permission to access this resource"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponseDTO<?>> handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(GlobalResponseDTO.error(errors));
    }

    @ExceptionHandler(org.springframework.web.bind.MissingRequestValueException.class)
    public ResponseEntity<GlobalResponseDTO<?>> handleMissingRequestValue(org.springframework.web.bind.MissingRequestValueException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(GlobalResponseDTO.error("Missing request value: " + ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GlobalResponseDTO<?>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(GlobalResponseDTO.error(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalResponseDTO<?>> handleException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(GlobalResponseDTO.error("Internal server error: " + ex.getMessage()));
    }
}
