package com.blm.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "促销活动信息视图对象")
public class PromotionVO {
    @Schema(description = "促销活动ID")
    private Long id;
    @Schema(description = "活动名称")
    private String name;
    @Schema(description = "活动描述")
    private String description;
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
    @Schema(description = "折扣类型", allowableValues = {"PERCENT", "AMOUNT", "SPECIAL"})
    private String discountType;
    @Schema(description = "折扣值")
    private BigDecimal discountValue;
    @Schema(description = "最低订单金额")
    private BigDecimal minOrderAmount;
    @Schema(description = "状态: 0-未开始, 1-进行中, 2-已结束")
    private Integer status;
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}