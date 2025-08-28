package com.blm.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "购物车项目更新数据传输对象")
public class CartItemUpdateDTO {

    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "商品数量不能少于1")
    @Schema(description = "商品数量", required = true, example = "1")
    private Integer quantity;
}
