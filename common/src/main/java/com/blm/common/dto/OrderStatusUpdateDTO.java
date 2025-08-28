package com.blm.common.dto;

import com.blm.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新订单状态数据传输对象 (通常由骑手或系统使用)")
public class OrderStatusUpdateDTO {
    @Schema(description = "新的订单状态", requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"CONFIRMED", "PREPARING", "READY_FOR_PICKUP", "PICKED_UP", "DELIVERING", "DELIVERED", "CANCELLED", "REFUNDED"},
            example = "PICKED_UP")
    private Order.OrderStatus orderStatus; // e.g., CONFIRMED, PREPARING, READY_FOR_PICKUP, PICKED_UP, DELIVERING, DELIVERED, CANCELLED, REFUNDED
}