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
 * 平台统计数据视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "平台统计数据")
public class PlatformStatsVO {

    @Schema(description = "用户总数")
    private Long totalUsers;
    
    @Schema(description = "商家总数")
    private Long totalMerchants;
    
    @Schema(description = "骑手总数")
    private Long totalRiders;
    
    @Schema(description = "订单总数")
    private Long totalOrders;
    
    @Schema(description = "累计交易额")
    private BigDecimal totalAmount;
    
    @Schema(description = "今日订单数")
    private Long todayOrders;
    
    @Schema(description = "今日交易额")
    private BigDecimal todayAmount;
    
    @Schema(description = "本周订单数")
    private Long weekOrders;
    
    @Schema(description = "本月订单数")
    private Long monthOrders;
    
    @Schema(description = "新增用户数 (按日期分组)")
    private Map<String, Long> newUsersData;
    
    @Schema(description = "订单数量 (按日期分组)")
    private Map<String, Long> orderCountData;
    
    @Schema(description = "交易额 (按日期分组)")
    private Map<String, BigDecimal> orderAmountData;
    
    @Schema(description = "热门商品排行")
    private List<TopFoodItemVO> topFoods;
    
    @Schema(description = "热门商家排行")
    private List<TopStoreItemVO> topStores;
    
    /**
     * 热门商品项
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "热门商品项")
    public static class TopFoodItemVO {
        @Schema(description = "商品ID")
        private Long foodId;
        
        @Schema(description = "商品名称")
        private String foodName;
        
        @Schema(description = "店铺名称")
        private String storeName;
        
        @Schema(description = "销售数量")
        private Long salesCount;
    }
    
    /**
     * 热门商家项
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "热门商家项")
    public static class TopStoreItemVO {
        @Schema(description = "店铺ID")
        private Long storeId;
        
        @Schema(description = "店铺名称")
        private String storeName;
        
        @Schema(description = "订单数量")
        private Long orderCount;
        
        @Schema(description = "销售额")
        private BigDecimal salesAmount;
    }
}