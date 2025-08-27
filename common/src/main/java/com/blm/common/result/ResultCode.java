package com.blm.common.result;

/**
 * 返回状态码枚举
 */
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(400, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    NOT_FOUND(404, "资源未找到"),

    // 用户相关
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    USER_DISABLED(1003, "用户已被禁用"),
    INVALID_CREDENTIALS(1004, "用户名或密码错误"),
    PASSWORD_NOT_MATCH(1005, "密码不匹配"),

    // 商家相关
    STORE_NOT_FOUND(2001, "店铺不存在"),
    STORE_DISABLED(2002, "店铺已关闭"),

    // 订单相关
    ORDER_NOT_FOUND(3001, "订单不存在"),
    ORDER_STATUS_ERROR(3002, "订单状态错误"),

    // 骑手相关
    RIDER_NOT_FOUND(4001, "骑手不存在"),
    RIDER_BUSY(4002, "骑手忙碌中"),

    // Token相关
    TOKEN_EXPIRED(5001, "Token已过期"),
    TOKEN_INVALID(5002, "Token无效"),
    REFRESH_TOKEN_EXPIRED(5003, "刷新Token已过期");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
