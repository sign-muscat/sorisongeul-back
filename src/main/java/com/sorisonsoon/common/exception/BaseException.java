package com.sorisonsoon.common.exception;

import com.sorisonsoon.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final int code;

    private final String message;

    public BaseException(final ExceptionCode exceptionCode) {
        this(exceptionCode.getCode(), exceptionCode.getMessage());
    }

    public BaseException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
