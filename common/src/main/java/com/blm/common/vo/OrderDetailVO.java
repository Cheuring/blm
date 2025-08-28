package com.blm.common.vo;

import com.blm.dto.PaymentDTO;
import com.blm.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "订单详细信息视图对象")
public class OrderDetailVO {
    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "用户ID") // Added for completeness
    private Long userId;

    @Schema(description = "店铺ID")
    private Long storeId;

    @Schema(description = "店铺名称") // Added
    private String storeName;

    @Schema(description = "收货地址ID")
    private Long addressId;

    @Schema(description = "收货地址详细信息") // Added
    private UserAddressVO deliveryAddress; // Assuming UserAddressVO exists

    @Schema(description = "订单总金额 (商品金额)")
    private BigDecimal totalAmount;

    @Schema(description = "配送费")
    private BigDecimal deliveryFee;

    @Schema(description = "优惠金额")
    private BigDecimal discountAmount;

    @Schema(description = "实际支付金额")
    private BigDecimal paymentAmount;

    @Schema(description = "支付方式", example = "ALIPAY")
    private PaymentDTO.PaymentType paymentType;

    @Schema(description = "订单状态", example = "DELIVERING")
    private Order.OrderStatus status;

    @Schema(description = "订单备注")
    private String remark;

    @Schema(description = "预计送达时间")
    private LocalDateTime expectedTime;

    @Schema(description = "实际送达时间")
    private LocalDateTime actualTime;

    @Schema(description = "下单时间")
    private LocalDateTime createdAt;

    @Schema(description = "支付时间") // Added
    private LocalDateTime paidAt;

    @Schema(description = "发货时间") // Added
    private LocalDateTime shippedAt;

    @Schema(description = "完成时间") // Added
    private LocalDateTime completedAt;

    @Schema(description = "取消时间") // Added
    private LocalDateTime cancelledAt;

    @Schema(description = "取消原因") // Added
    private String cancelReason;

    @Schema(description = "骑手ID") // Added
    private Long riderId;

    @Schema(description = "骑手姓名") // Added
    private String riderName;

    @Schema(description = "骑手电话") // Added
    private String riderPhone;

    @Schema(description = "订单项列表")
    private List<OrderItemVO> items;
}