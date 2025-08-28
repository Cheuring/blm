package com.blm.common.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Promotion {
    
    public enum PromotionStatus {
        NOT_STARTED(0, "未开始"),
        ACTIVE(1, "进行中"),
        ENDED(2, "已结束");
        
        private final Integer code;
        private final String description;
        
        PromotionStatus(Integer code, String description) {
            this.code = code;
            this.description = description;
        }
        
        public Integer getCode() {
            return code;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum DiscountType {
        PERCENT("PERCENT", "百分比折扣"),
        AMOUNT("AMOUNT", "固定金额"),
        SPECIAL("SPECIAL", "特殊优惠");
        
        private final String code;
        private final String description;
        
        DiscountType(String code, String description) {
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
    private String name;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal minOrderAmount;
    private PromotionStatus status;
    private LocalDateTime createdAt;
}
