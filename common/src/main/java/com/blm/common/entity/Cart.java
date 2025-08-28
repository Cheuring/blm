package com.blm.common.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Cart {
    private Long id;
    private Long userId;
    private Long storeId;
    private Long foodId;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}