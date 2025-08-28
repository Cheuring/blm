package com.blm.common.exception;

import com.blm.common.result.ExceptionConstant;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException{
    private final Integer code;
    private final String message;

    public CommonException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public CommonException(ExceptionConstant exceptionConstant) {
        super(exceptionConstant.getMessage());
        this.code = exceptionConstant.getCode();
        this.message = exceptionConstant.getMessage();
    }

}
