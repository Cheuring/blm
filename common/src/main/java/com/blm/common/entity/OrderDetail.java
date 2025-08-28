package com.blm.common.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDetail {
    private Long id;
    private Long orderId;
    private Long foodId;
    private String foodName;
    private String foodImage;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}