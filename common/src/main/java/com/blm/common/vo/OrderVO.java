package com.blm.common.vo;

import com.blm.common.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "订单基本信息视图对象 (用于列表展示)")
public class OrderVO {
    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "店铺ID")
    private Long storeId;

    @Schema(description = "店铺名称") // Added for better display in lists
    private String storeName;

    @Schema(description = "店铺图片URL") // Added for better display in lists
    private String storeImage;

    @Schema(description = "订单总金额 (商品金额)")
    private BigDecimal totalAmount;

    @Schema(description = "配送费")
    private BigDecimal deliveryFee;

    @Schema(description = "优惠金额")
    private BigDecimal discountAmount;

    @Schema(description = "实际支付金额")
    private BigDecimal paymentAmount;

    @Schema(description = "订单状态", example = "DELIVERING")
    private Order.OrderStatus status; // e.g., PENDING_PAYMENT, CONFIRMED, PREPARING, DELIVERING, DELIVERED, CANCELLED

    @Schema(description = "支付状态: 0-未支付, 1-已支付, 2-退款中, 3-已退款")
    private Integer paymentStatus;

    @Schema(description = "下单时间")
    private LocalDateTime createdAt;

    // Consider adding a preview of items if needed for list view
    // @Schema(description = "订单商品预览 (例如第一个商品名称)")
    // private String itemPreview;
}