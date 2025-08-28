package com.blm.common.vo;

import com.blm.common.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "订单基本信息视图对象")
public class OrderVO {
    
    @Schema(description = "订单ID")
    private Long id;
    
    @Schema(description = "订单编号")
    private String orderNo;
    
    @Schema(description = "店铺ID")
    private Long storeId;
    
    @Schema(description = "店铺名称")
    private String storeName;
    
    @Schema(description = "店铺图片URL")
    private String storeImage;
    
    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;
    
    @Schema(description = "配送费")
    private BigDecimal deliveryFee;
    
    @Schema(description = "优惠金额")
    private BigDecimal discountAmount;
    
    @Schema(description = "实际支付金额")
    private BigDecimal paymentAmount;
    
    @Schema(description = "支付方式")
    private Order.PaymentType paymentType;
    
    @Schema(description = "订单状态")
    private Order.OrderStatus status;
    
    @Schema(description = "预计送达时间")
    private LocalDateTime expectedTime;
    
    @Schema(description = "下单时间")
    private LocalDateTime createdAt;
}
