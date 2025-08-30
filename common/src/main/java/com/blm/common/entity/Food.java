package com.blm.common.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Food {
    public enum FoodStatus {
        OFF_SHELF,
        ON_SHELF,
        SUSPENDED,
        PENDING, // rejected 后修改重新提交
    }

    private Long id;
    private Long storeId;
    private Long categoryId;
    private String name = "empty";
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String description;
    private String image;
    private Integer sales;
    private FoodStatus status;
    private Integer isFeatured;
    private String rejectReason; // 添加拒绝原因字段 // todo: add field
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}