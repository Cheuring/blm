package com.blm.common.dto;

import com.blm.entity.Food;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新店铺商品信息(通常由店铺使用)")
public class FoodStatusUpdateDTO {
    @Schema(description = "商品状态", requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"OFF_SHELF", "ON_SHELF", "SUSPENDED", "PENDING"},
            example = "PENDING")
    private Food.FoodStatus foodStatus;
}
