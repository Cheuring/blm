package com.blm.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "创建或更新商品数据传输对象") // Updated description as it's used for both create and update
public class FoodCreateDTO {
    @Schema(description = "商品分类ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long categoryId;
    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "商品售价", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;
    @Schema(description = "商品原价 (可选)")
    private BigDecimal originalPrice;
    @Schema(description = "商品描述")
    private String description;
    @Schema(description = "商品图片URL")
    private String image;
    //这里删除了原来的状态设置，因为初次记录时要设置其为不可售状态，同时修改了数据库status的类型
    @Schema(description = "是否特色商品: 0-否, 1-是", defaultValue = "0")
    private Integer isFeatured; // 0 否, 1 是

    private Integer status = 0; // 商品状态，0-下架, 1-上架
}