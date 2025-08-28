package com.blm.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true) // Ensure fields from StoreVO are included in equals/hashCode
@Schema(description = "店铺详细信息视图对象")
public class StoreDetailVO extends StoreVO {

    @Schema(description = "店铺的商品分类列表")
    private List<FoodCategoryVO> categories;

    @Schema(description = "店铺的热销/特色商品列表")
    private List<FoodVO> featuredFoods; // Renamed from hotFoods for clarity, assuming featured

    // Consider adding other details like reviews, promotions applicable to the store
    // @Schema(description = "店铺评价摘要")
    // private ReviewSummaryVO reviewSummary;
    // @Schema(description = "当前可用的促销活动")
    // private List<PromotionVO> activePromotions;
}