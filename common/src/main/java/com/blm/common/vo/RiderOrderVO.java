package com.blm.common.vo;

import com.blm.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "骑手订单视图对象 (用于骑手端订单管理)")
public class RiderOrderVO {
    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "订单状态")
    private Order.OrderStatus orderStatus;

    @Schema(description = "店铺ID")
    private Long storeId;

    @Schema(description = "店铺名称")
    private String storeName;

    @Schema(description = "店铺图片URL")
    private String storeImage;

    @Schema(description = "商家地址")
    private String storeAddress;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "用户地址")
    private String userAddress;

    @Schema(description = "配送费")
    private BigDecimal deliveryFee;

    @Schema(description = "实际支付金额")
    private BigDecimal paymentAmount;

    @Schema(description = "下单时间")
    private LocalDateTime createdAt;

    @Data
    static class Address { // todo
        private BigDecimal latitude;
        private BigDecimal longitude;
    }
}