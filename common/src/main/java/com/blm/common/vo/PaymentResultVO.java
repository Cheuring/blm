package com.blm.common.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResultVO {
    private Long orderId;
    private String orderNo;
    private Integer paymentStatus;
    private String paymentMessage;
    private BigDecimal paymentAmount;
    private LocalDateTime paymentTime;
}