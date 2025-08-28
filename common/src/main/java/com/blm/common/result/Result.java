package com.blm.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "统一返回结果")
public class Result<T> {
    @Schema(description = "状态码", example = "200")
    private Integer status;
    @Schema(description = "提示信息", example = "操作成功")
    private String message;
    @Schema(description = "返回数据")
    private T data;
    
    private Result(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
    
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }
    
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }
    
    public static <T> Result<T> error(Integer status, String message) {
        return new Result<>(status, message, null);
    }
}