package com.blm.common.exception;

import com.blm.common.result.ExceptionConstant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 表示请求的资源未找到的异常
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(ExceptionConstant exceptionConstant) {
        super(exceptionConstant.getMessage());
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}