package com.blm.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "异常常量类")
public enum ExceptionConstant {
    REQ_PARAM_ERROR("请求参数错误", 400), // Renamed from REQUEST_PARMA_ERROR
    REQ_UNAUTHORIZED("未认证", 401), // Renamed from UNAUTHORIZED
    REQ_FORBIDDEN("无权限", 403), // Renamed from FORBIDDEN
    REQ_NOT_FOUND("资源不存在", 404), // Renamed from NOT_FOUND
    REQ_CONFLICT("资源冲突", 409), // Renamed from CONFLICT
    REQ_SERVER_ERROR("服务器错误", 500), // Renamed from SERVER_ERROR
    
    USER_NOT_FOUND("用户不存在", 1000),
    USER_ALREADY_EXISTS("用户已存在", 1001),
    USER_INVALID_CREDENTIALS("凭证错误(用户名或密码错误)", 1002), // Renamed from INVALID_CREDENTIALS
    USER_ACCOUNT_DISABLED("账号被禁用", 1003), // Renamed from ACCOUNT_DISABLED
    USER_INVALID_TOKEN("无效的令牌", 1004), // Renamed from INVALID_TOKEN
    USER_TOKEN_EXPIRED("令牌已过期", 1005), // Renamed from TOKEN_EXPIRED
    USER_PHONE_ALREADY_REGISTERED("手机号已经被注册", 1006),
    USER_ADDRESS_NOT_FOUND("地址不存在", 1100), // Renamed from ADDRESS_NOT_FOUND

    FOOD_NOT_FOUND("商品不存在", 2000),
    FOOD_CATEGORY_NOT_FOUND("分类不存在", 2001),
    FOOD_OUT_OF_STOCK("商品库存不足", 2002),
    FOOD_ALREADY_EXISTS("商品已存在", 2003),
    FOOD_STATUS_ERROR("商品状态错误", 2004),
    FOOD_ALREADY_FAV("商品已收藏", 2005),
    FOOD_UNAUTHORIZED("无权修改该商品", 2006),
    FOOD_CATEGORY_UNAUTHORIZED("无权修改该商品分类", 2006),

    ORDER_NOT_FOUND("订单不存在", 3000),
    ORDER_STATUS_ERROR("订单状态错误", 3001),
    ORDER_CART_EMPTY("购物车为空", 3002), // Renamed from CART_EMPTY
    ORDER_CART_ITEM_NOT_FOUND("购物车项不存在", 3003), // Renamed from CART_ITEM_NOT_FOUND
    ORDER_PAYMENT_FAILED("支付失败", 3004), // Renamed from PAYMENT_FAILED
    ORDER_ALREADY_PAID("订单已支付", 3005),
    ORDER_ALREADY_CANCELED("订单已取消", 3006),
    ORDER_REVIEW_EXISTS("订单已评价", 3007),
    ORDER_AMOUNT_BELOW("订单金额不足", 3008),
    ORDER_UNAUTHORIZED("无权操作该订单", 3009),

    STORE_NOT_FOUND("店铺不存在", 4000),
    STORE_STATUS_ERROR("店铺状态错误", 4001),
    STORE_CLOSED("店铺已关闭", 4002),
    STORE_UNAUTHORIZED("无权访问该店铺", 4003),
    STORE_ALREADY_FAV("店铺已收藏", 4004),
    STORE_CREATE_FAILED("店铺创建失败", 4005),
    STORE_UPDATE_FAILED("店铺更新失败", 4006),
    STORE_CATEGORY_NOT_FOUND("店铺分类不存在", 4007),

    RIDER_NOT_FOUND("骑手不存在", 5000),
    RIDER_STATUS_ERROR("骑手状态错误", 5001),
    RIDER_NOT_AVAILABLE("骑手不可用", 5002),
    RIDER_DELIVERY_STATUS_ERROR("配送状态错误", 5003), // Renamed from DELIVERY_STATUS_ERROR
    RIDER_ALREADY_EXISTS("骑手已存在", 5004),
    RIDER_ASSIGN_FAILED("骑手接单失败", 5005),

    REVIEW_NOT_FOUND("评价不存在", 6000),
    REVIEW_ALREADY_EXISTS("评价已存在", 6001),

    HISTORY_NOT_FOUND("历史记录不存在", 7000),

    PROMOTION_NOT_FOUND("促销活动不存在", 8000),

    SYS_ERROR("系统错误", 9000),
    SYS_SERVICE_UNAVAILABLE("服务不可用", 9001),
    SYS_DATABASE_ERROR("数据库错误", 9002),
    SYS_THIRD_PARTY_SERVICE_ERROR("第三方服务错误", 9003),
    SYS_FILE_UPLOAD_FAILED("文件上传失败", 9004),
    SYS_FILE_REMOVE_FAILED("文件删除失败", 9005),
    ;

    private final String message;
    private final int code;

    ExceptionConstant(String message, int code) {
        this.message = message;
        this.code = code;
    }

}
