package com.blm.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "店铺详细信息视图对象")
public class StoreDetailVO extends StoreVO {
    
    @Schema(description = "店铺的商品分类列表")
    private List<FoodCategoryVO> categories;
    
    @Schema(description = "店铺的特色商品列表")
    private List<FoodVO> featuredFoods;
    
    @Schema(description = "店铺的促销活动列表")
    private List<PromotionVO> promotions;
}
