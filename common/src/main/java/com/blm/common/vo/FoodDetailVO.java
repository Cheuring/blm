package com.blm.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "商品详细信息视图对象")
public class FoodDetailVO extends FoodVO {

    @Schema(description = "商品评价列表")
    private List<ReviewVO> reviews; // Changed from List<Object>

    // Consider adding other details like applicable promotions
    // @Schema(description = "适用于该商品的促销活动")
    // private List<PromotionVO> applicablePromotions;
}