package com.blm.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建订单数据传输对象")
public class OrderCreateDTO {
    @Schema(description = "店铺ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long storeId;

    @Schema(description = "收货地址ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long addressId;

    @Schema(description = "订单备注")
    private String remark;

    // Cart items are usually implicitly taken from the user's current cart for the store
    // Alternatively, could include List<CartItemDTO> if creating order directly without cart
}