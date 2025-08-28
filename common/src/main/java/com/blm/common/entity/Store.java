package com.blm.common.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Store {
    
    public enum StoreStatus {
        PENDING("PENDING", "待审核"),
        OPEN("OPEN", "营业中"),
        CLOSED("CLOSED", "暂停营业"),
        SUSPENDED("SUSPENDED", "已封禁");
        
        private final String code;
        private final String description;
        
        StoreStatus(String code, String description) {
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
    private Long userId;
    private String name;
    private String description;
    private String image;
    private String address;
    private String phone;
    private BigDecimal deliveryFee;
    private BigDecimal minDeliveryAmount;
    private StoreStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
