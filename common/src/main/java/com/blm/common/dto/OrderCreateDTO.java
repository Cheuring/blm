package com.blm.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "创建订单数据传输对象")
public class OrderCreateDTO {
    
    @Schema(description = "店铺ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "店铺ID不能为空")
    private Long storeId;
    
    @Schema(description = "收货地址ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "收货地址ID不能为空")
    private Long addressId;
    
    @Schema(description = "订单备注")
    private String remark;
}
