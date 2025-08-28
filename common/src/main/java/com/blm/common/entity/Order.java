package com.blm.common.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {
    
    // 订单状态常量
    public static final Integer UNPAID = 0;
    public static final Integer PAID = 1;
    
    // 订单状态枚举
    public enum OrderStatus {
        ORDER_CREATED("ORDER_CREATED", "订单创建"),
        PENDING_PAYMENT("PENDING_PAYMENT", "待支付"),
        PAID("PAID", "已支付"),
        CONFIRMED("CONFIRMED", "商家已接单"),
        PREPARING("PREPARING", "备餐中"),
        READY_FOR_PICKUP("READY_FOR_PICKUP", "备餐完成"),
        DISPATCHED("DISPATCHED", "已派送"),
        DELIVERING("DELIVERING", "配送中"),
        DELIVERED("DELIVERED", "已送达"),
        COMPLETED("COMPLETED", "已完成"),
        CANCELLED("CANCELLED", "已取消"),
        REFUNDED("REFUNDED", "已退款");
        
        private final String code;
        private final String description;
        
        OrderStatus(String code, String description) {
            this.code = code;
            this.description = description;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    // 支付方式枚举
    public enum PaymentType {
        ALIPAY("ALIPAY", "支付宝"),
        WECHAT("WECHAT", "微信支付"),
        CASH("CASH", "现金支付");
        
        private final String code;
        private final String description;
        
        PaymentType(String code, String description) {
            this.code = code;
            this.description = description;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    private Long id;
    private String orderNo;
    private Long userId;
    private Long storeId;
    private Long riderId;
    private Long addressId;
    private BigDecimal totalAmount;
    private BigDecimal deliveryFee;
    private BigDecimal discountAmount;
    private BigDecimal couponDiscount;
    private Long userCouponId;
    private BigDecimal paymentAmount;
    private PaymentType paymentType;
    private OrderStatus status;
    private String dispatchType;
    private String remark;
    private LocalDateTime expectedTime;
    private LocalDateTime actualTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
