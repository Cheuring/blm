package com.blm.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 骑手统计数据视图对象
 */
@Data
@Schema(description = "骑手统计数据")
public class RiderStatsVO {

    @Schema(description = "统计日期")
    private LocalDate date;

    @Schema(description = "订单数量")
    private Integer ordersCount;

    @Schema(description = "完成的订单数")
    private Integer completedOrders;

    @Schema(description = "取消的订单数")
    private Integer canceledOrders;

    @Schema(description = "总收入")
    private BigDecimal totalIncome;

    @Schema(description = "完成率")
    private BigDecimal completionRate;
}
