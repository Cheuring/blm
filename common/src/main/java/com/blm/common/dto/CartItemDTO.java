package com.blm.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "购物车项目数据传输对象")
public class CartItemDTO {
    
    @Schema(description = "店铺ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "店铺ID不能为空")
    private Long storeId;
    
    @Schema(description = "商品ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品ID不能为空")
    private Long foodId;
    
    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1")
    @Min(value = 1, message = "数量不能小于1")
    @NotNull(message = "数量不能为空")
    private Integer quantity;
}
