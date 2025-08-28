package com.blm.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 店铺统计数据视图对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "店铺统计数据视图对象")
public class StoreStatisticsVO {
    
    @Schema(description = "店铺ID")
    private Long storeId;
    
    @Schema(description = "店铺名称")
    private String storeName;
    
    @Schema(description = "今日订单数")
    private long todayOrderCount;
    
    @Schema(description = "今日销售额")
    private BigDecimal todaySales;
    
    @Schema(description = "昨日订单数")
    private long yesterdayOrderCount;
    
    @Schema(description = "昨日销售额")
    private BigDecimal yesterdaySales;
    
    @Schema(description = "本月订单数")
    private long monthOrderCount;
    
    @Schema(description = "本月销售额")
    private BigDecimal monthSales;
    
    @Schema(description = "总订单数")
    private long totalOrderCount;
    
    @Schema(description = "总销售额")
    private BigDecimal totalSales;
    
    @Schema(description = "最近30天每日订单数趋势")
    private StoreOrderReportVO dailyOrderTrend;
    
    @Schema(description = "最近30天每日销售额趋势")
    private StoreTurnoverReportVO dailySalesTrend;
    
    @Schema(description = "热销商品Top5")
    private List<HotFoodVO> hotFoods;

    @Schema(description = "评价统计数据")
    private ReviewStatisticsVO reviewStatistics;
    
    /**
     * 热销商品视图对象
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(description = "热销商品视图对象")
    public static class HotFoodVO {
        
        @Schema(description = "商品ID")
        private Long foodId;
        
        @Schema(description = "商品名称")
        private String foodName;
        
        @Schema(description = "商品图片")
        private String foodImage;
        
        @Schema(description = "销售数量")
        private Integer saleCount;
        
        @Schema(description = "销售金额")
        private BigDecimal saleAmount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "评价统计视图对象")
    public static class ReviewStatisticsVO {

        @Schema(description = "店铺ID")
        private Long storeId;

        @Schema(description = "店铺名称")
        private String storeName;

        @Schema(description = "评价总数")
        private Integer totalReviews;

        @Schema(description = "平均评分", example = "4.5")
        private Double averageRating;

        @Schema(description = "各评分数量统计", example = "{\"5\": 10, \"4\": 5, \"3\": 3, \"2\": 1, \"1\": 0}")
        private Map<Integer, Integer> ratingCounts;

        @Schema(description = "好评率(4-5星占比)", example = "0.85")
        private Double goodRatePercentage;
    }
}