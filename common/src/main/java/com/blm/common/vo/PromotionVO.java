package com.blm.common.vo;

import com.blm.common.entity.Promotion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "促销活动视图对象")
public class PromotionVO {
    
    @Schema(description = "促销ID")
    private Long id;
    
    @Schema(description = "店铺ID")
    private Long storeId;
    
    @Schema(description = "促销名称")
    private String name;
    
    @Schema(description = "促销描述")
    private String description;
    
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
    
    @Schema(description = "折扣类型")
    private Promotion.DiscountType discountType;
    
    @Schema(description = "折扣值")
    private BigDecimal discountValue;
    
    @Schema(description = "最低订单金额")
    private BigDecimal minOrderAmount;
    
    @Schema(description = "状态")
    private Promotion.PromotionStatus status;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}
