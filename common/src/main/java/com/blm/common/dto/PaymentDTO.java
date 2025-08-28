package com.blm.common.dto;

import com.blm.common.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "支付数据传输对象")
public class PaymentDTO {
    
    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "订单ID不能为空")
    private Long orderId;
    
    @Schema(description = "支付方式", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "支付方式不能为空")
    private Order.PaymentType paymentType;
}
