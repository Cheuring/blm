package com.blm.common.vo;

import com.blm.common.entity.Food;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "商品基本信息视图对象")
public class FoodVO {
    @Schema(description = "商品ID(当在历史板块时为历史记录ID)")
    private Long id;
    @Schema(description = "所属店铺ID")
    private Long storeId;
    @Schema(description = "所属分类ID")
    private Long categoryId;
    @Schema(description = "商品名称")
    private String name;
    @Schema(description = "商品售价")
    private BigDecimal price;
    // Consider adding originalPrice if needed in responses
    // @Schema(description = "商品原价")
    // private BigDecimal originalPrice;
    @Schema(description = "商品描述")
    private String description;
    @Schema(description = "商品图片URL")
    private String image;
    @Schema(description = "销量")
    private Integer sales;
    @Schema(description = "状态：OFF_SHELF/ON_SHELF/SUSPENDED/PENDING")
    private Food.FoodStatus status;

    @Schema(description = "访问时间，用于历史记录", example = "2023-10-01T12:00:00")
    private LocalDateTime visitedAt; // 访问时间，用于历史记录
    // Consider adding isFeatured if needed in responses
    // @Schema(description = "是否特色商品: 0-否, 1-是")
    // private Integer isFeatured;
}