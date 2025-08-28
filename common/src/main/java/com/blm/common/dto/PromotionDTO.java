package com.blm.common.dto;

import com.blm.common.entity.Promotion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "促销活动DTO")
public class PromotionDTO {
    
    @Schema(description = "促销名称", required = true)
    @NotBlank(message = "促销名称不能为空")
    private String name;
    
    @Schema(description = "促销描述")
    private String description;
    
    @Schema(description = "开始时间", required = true)
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;
    
    @Schema(description = "结束时间", required = true)
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;
    
    @Schema(description = "折扣类型", required = true)
    @NotNull(message = "折扣类型不能为空")
    private Promotion.DiscountType discountType;
    
    @Schema(description = "折扣值", required = true)
    @NotNull(message = "折扣值不能为空")
    @DecimalMin(value = "0.0", message = "折扣值不能小于0")
    private BigDecimal discountValue;
    
    @Schema(description = "最低订单金额")
    @DecimalMin(value = "0.0", message = "最低订单金额不能小于0")
    private BigDecimal minOrderAmount;
}
