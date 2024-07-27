package com.sorisonsoon.common.exception;

import com.sorisonsoon.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class ServerInternalException extends BaseException {

    private static final int DEFAULT_EXCEPTION_CODE = 500;

    public ServerInternalException(String message) {
        super(DEFAULT_EXCEPTION_CODE, message);
    }

    public ServerInternalException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
