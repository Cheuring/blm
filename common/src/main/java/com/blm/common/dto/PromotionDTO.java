package com.blm.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "创建或更新促销活动数据传输对象")
public class PromotionDTO {
    @Schema(description = "活动名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "活动描述")
    private String description;
    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime startTime;
    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime endTime;
    @Schema(description = "折扣类型", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"PERCENT", "AMOUNT", "SPECIAL"}, example = "AMOUNT")
    private String discountType; // PERCENT (百分比), AMOUNT (固定金额), SPECIAL (特价)
    @Schema(description = "折扣值 (PERCENT时为0-1的小数, AMOUNT时为金额, SPECIAL时可能为特价价格)", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal discountValue;
    @Schema(description = "最低订单金额 (满足此金额才可享受优惠)", defaultValue = "0")
    private BigDecimal minOrderAmount;
    @Schema(description = "状态: 0-未开始, 1-进行中, 2-已结束 (通常由系统根据时间判断, 此处可能用于手动控制)", defaultValue = "0")
    private Integer status;
}