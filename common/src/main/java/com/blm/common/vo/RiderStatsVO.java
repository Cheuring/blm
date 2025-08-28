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
    
    /**
     * 统计周期开始日期
     */
    @Schema(description = "统计周期开始日期")
    private LocalDate startDate;
    
    /**
     * 统计周期结束日期
     */
    @Schema(description = "统计周期结束日期")
    private LocalDate endDate;
    
    /**
     * 总订单数
     */
    @Schema(description = "总订单数")
    private Integer ordersCount;
    
    /**
     * 已完成订单数
     */
    @Schema(description = "已完成订单数")
    private Integer completedOrders;
    
    /**
     * 取消订单数
     */
    @Schema(description = "取消订单数")
    private Integer canceledOrders;
    
    /**
     * 总收入
     */
    @Schema(description = "总收入")
    private BigDecimal totalIncome;
    
    /**
     * 完成率 (%)
     */
    @Schema(description = "完成率(%)")
    private BigDecimal completionRate;
}