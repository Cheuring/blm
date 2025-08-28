package com.blm.common.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Store {
    public enum StoreStatus {
        OPEN,
        CLOSED,
        SUSPENDED, // 审核被拒，改动信息后应改为PENDING
        PENDING, // 待审核
    }

    private Long id;
    private Long merchantId;
    private String name;
    private String logo;
    private String description;
    private String phone;
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String businessHours;
    private BigDecimal deliveryFee;
    private BigDecimal minOrderAmount;
    private Integer averageDeliveryTime;
    private Long categoryId;
    private StoreStatus status;
    private BigDecimal rating;
    private String licenseImg;
    private String permitImg;
    private String rejectReason; // 添加拒绝原因字段
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}