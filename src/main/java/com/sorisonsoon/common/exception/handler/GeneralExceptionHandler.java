package com.sorisonsoon.common.exception.handler;


import com.sorisonsoon.common.dto.response.ApiResponse;
import com.sorisonsoon.common.exception.*;
import com.sorisonsoon.common.exception.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionHandler {

    /* 400, BadRequestException */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> badRequestException(BadRequestException e) {

        log.info("BadRequestException : {}", e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(e.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(errorResponse));
    }

    /* 401, AuthException */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse> badAuthException(AuthException e) {

        log.info("AuthException : {}", e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(e.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail(errorResponse));
    }

    /* 404, NotFoundException */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> notFoundException(NotFoundException e) {

        log.info("NotFoundException : {}", e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(e.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(errorResponse));
    }

    /* 409, ConflictException: 논리적으로 수행할 수 없을 경우 처리  */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse> conflictException(ConflictException e) {

        log.info("ConflictException : {}", e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(e.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.fail(errorResponse));
    }

    /* @Valid 에 대한 Exception */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {


        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        log.info("MethodArgumentNotValidException : {}", errors);

        return ResponseEntity.badRequest().body(ApiResponse.fail("Validation Error!", errors));
    }

    /* 500, ServerError */
    @ExceptionHandler(ServerInternalException.class)
    public ResponseEntity<ApiResponse> serverInternalException(ServerInternalException e) {

        log.info("ServerInternalException : {}", e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(e.getCode(), e.getMessage());
        return ResponseEntity.internalServerError().body(ApiResponse.fail(errorResponse));
    }

    /* Global Exception */
    @ExceptionHandler({ Exception.class, RuntimeException.class })
    public ResponseEntity<ApiResponse> exception(Exception e) {

        log.info("Exception : {}", e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(500, e.getMessage());
        return ResponseEntity.internalServerError().body(ApiResponse.fail(errorResponse));
    }
}
