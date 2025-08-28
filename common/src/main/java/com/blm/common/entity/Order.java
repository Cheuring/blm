package com.blm.common.entity;

import com.blm.dto.PaymentDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {
    public static final Integer UNPAID = 0;
    public static final Integer PAID = 1;
    public enum OrderStatus {
        // 按照顺序排列
        ORDER_CREATED,//用户下单
        PENDING,//支付完成，等待商家接单
        MERCHANT_CONFIRMED,//商家已接单
        COOKING,//商家开始制作
        READY_WAITING_RIDER,//商家已出餐，等待骑手取餐
        RIDER_ASSIGNED,//骑手已接单
        FOOD_PICKED,//骑手已取餐
        DELIVERING,//骑手配送中
        DELIVERED,//订单已送达
        COMPLETED,//订单已完成
        CANCELLED,//订单已取消
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
    private BigDecimal paymentAmount;
    private PaymentDTO.PaymentType paymentType;
    private OrderStatus status;
    private String remark;
    private LocalDateTime expectedTime;
    private LocalDateTime actualTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}