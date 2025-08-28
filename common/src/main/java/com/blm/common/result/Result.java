package com.blm.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果封装类
 */
@Data
@Schema(description = "统一返回结果")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "状态码")
    private Integer code;

    @Schema(description = "返回消息")
    private String message;

    @Schema(description = "返回数据")
    private T data;

    @Schema(description = "时间戳")
    private Long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = 200;
        result.message = "success";
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.message = "success";
        result.data = data;
        return result;
    }

    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.message = message;
        result.data = data;
        return result;
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.code = 500;
        result.message = message;
        return result;
    }

    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.code = code;
        result.message = message;
        return result;
    }

    public static <T> Result<T> error(ResultCode resultCode) {
        Result<T> result = new Result<>();
        result.code = resultCode.getCode();
        result.message = resultCode.getMessage();
        return result;
    }

    public boolean isSuccess() {
        return this.code != null && this.code.equals(200);
    }
}
