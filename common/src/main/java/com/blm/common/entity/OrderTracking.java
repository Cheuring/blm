package com.blm.common.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderTracking {
    private Long id;
    private Long orderId;
    private Order.OrderStatus status;
    private Long operatorId;
    private String operatorType;
    private LocalDateTime createdAt;
}
