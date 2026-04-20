package com.gym.gym_backend.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
public class GlobalResponseDTO<T> {

    private String status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static <T> GlobalResponseDTO<T> success(T data, String message) {
        return GlobalResponseDTO.<T>builder()
                .status("SUCCESS")
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> GlobalResponseDTO<T> error(String message) {
        return GlobalResponseDTO.<T>builder()
                .status("ERROR")
                .message(message)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
}