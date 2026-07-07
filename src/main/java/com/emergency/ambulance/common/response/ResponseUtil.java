package com.emergency.ambulance.common.response;

public class ResponseUtil {

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                true,
                message,
                data,
                null
        );
    }

    public static ApiResponse<?> failure(String message, Object errors) {
        return new ApiResponse<>(
                false,
                message,
                null,
                errors
        );
    }
}
