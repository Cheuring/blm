package com.blm.common.vo;

import com.blm.entity.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "店铺基本信息视图对象 (用于列表展示)")
public class StoreVO {
    @Schema(description = "店铺ID", example = "1")
    private Long id;
    @Schema(description = "店铺名称", example = "美味快餐店")
    private String name;
    @Schema(description = "店铺Logo URL", example = "https://example.com/logo.jpg")
    private String logo;
    @Schema(description = "店铺描述", example = "提供各种美味快餐")
    private String description;
    @Schema(description = "店铺联系电话", example = "13800138000")
    private String phone;
    @Schema(description = "店铺详细地址", example = "北京市海淀区中关村大街1号")
    private String address;
    @Schema(description = "经度", example = "116.307852")
    private BigDecimal longitude;
    @Schema(description = "纬度", example = "39.983424")
    private BigDecimal latitude;
    @Schema(description = "营业时间", example = "08:00-22:00")
    private String businessHours;
    @Schema(description = "配送费", example = "5.00")
    private BigDecimal deliveryFee;
    @Schema(description = "起送金额", example = "20.00")
    private BigDecimal minOrderAmount;
    @Schema(description = "平均配送时间(分钟)", example = "30")
    private Integer averageDeliveryTime;
    @Schema(description = "店铺分类ID", example = "1")
    private Long categoryId;
    // @Schema(description = "店铺分类名称", example = "快餐简餐") // Consider adding if needed in list view
    // private String categoryName;
    @Schema(description = "状态：PENDING/SUSPENDED/OPEN/CLOSED", example = "OPEN") // Simplified for user view
    private Store.StoreStatus status;
    @Schema(description = "店铺评分", example = "4.5")
    private BigDecimal rating;
    @Schema(description = "月销量", example = "1000")
    private Integer monthlySales;
    @Schema(description = "距离 (米, 仅当提供用户经纬度时计算)", example = "1500")
    private Double distance;
    @Schema(description = "是否有进行中的促销活动", example = "true")
    private Boolean hasPromotion;

    @Schema(description = "在查看历史板块时储存当前历史记录ID，否则为空", example = "2")
    private Long historyId; // 额外字段，用于存储其他信息

    @Schema(description = "访问时间，用于历史记录", example = "2023-10-01T12:00:00")
    private LocalDateTime visitedAt; // 访问时间，用于历史记录
}