package com.sorisonsoon.common.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "공통 응답 DTO")
@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    @Schema(description = "성공 여부")
    private final boolean success;
    @Schema(description = "응답 메시지")
    private final String message;
    @Schema(description = "결과 데이터")
    private final T result;

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(true, null, null);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, null, data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> success(T data, int status) {
        return new ApiResponse<>(true, null, data);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public static <T> ApiResponse<T> fail(T errors) {
        return new ApiResponse<>(false, null, errors);
    }

    public static <T> ApiResponse<T> fail(String message, T errors) {
        return new ApiResponse<>(false, message, errors);
    }
}
