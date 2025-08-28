package com.blm.common.exception;

import com.blm.common.result.ExceptionConstant;
import com.blm.common.result.Result;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(CommonException.class)
    public Result<Void> handleCommonException(CommonException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(TypeMismatchException.class)
    public Result<Void> handleTypeMismatchException(TypeMismatchException e) {
        return Result.error(ExceptionConstant.REQ_PARAM_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Result.error(ExceptionConstant.REQ_PARAM_ERROR.getCode(), e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public Result<Void> handleHttpMessageConversionException(HttpMessageConversionException e) {
        return Result.error(ExceptionConstant.REQ_PARAM_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return Result.error(ExceptionConstant.REQ_PARAM_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        return Result.error(ExceptionConstant.SYS_ERROR.getCode(), e.getMessage());
    }
}