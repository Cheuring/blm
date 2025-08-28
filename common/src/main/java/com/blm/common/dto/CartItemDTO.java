package com.blm.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "购物车项目数据传输对象 (用于添加或更新)")
public class CartItemDTO {
    @Schema(description = "店铺ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "店铺ID不能为空")
    private Long storeId; // Need storeId to ensure cart items are from the same store

    @Schema(description = "商品ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品ID不能为空")
    private Long foodId;

    @Schema(description = "数量 (更新时使用, 添加时默认为1, 如果要添加多个则指定)", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1")
    @Min(1) @NotNull
    private Integer quantity;
}