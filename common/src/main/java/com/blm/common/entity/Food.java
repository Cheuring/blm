package com.blm.common.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Food {
    
    public enum FoodStatus {
        ON_SHELF("ON_SHELF", "上架"),
        OFF_SHELF("OFF_SHELF", "下架"),
        SUSPENDED("SUSPENDED", "已封禁"),
        PENDING("PENDING", "待审核");
        
        private final String code;
        private final String description;
        
        FoodStatus(String code, String description) {
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
    private Long storeId;
    private Long categoryId;
    private String name;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String description;
    private String image;
    private Integer sales;
    private Integer stock;
    private FoodStatus status;
    private String rejectReason;
    private Boolean isFeatured;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
