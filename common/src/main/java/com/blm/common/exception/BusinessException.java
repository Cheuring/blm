package com.blm.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private final Integer code;
    private final String message;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public static BusinessException of(int code, String message) {
        return new BusinessException(code, message);
    }
}
