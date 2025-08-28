package com.blm.common.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Promotion {
    private Long id;
    private Long storeId;
    private String name;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String discountType;
    private BigDecimal discountValue;
    private BigDecimal minOrderAmount;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}