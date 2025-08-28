package com.blm.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentDTO {
    public enum PaymentType {
        ALIPAY, // 支付宝
        WECHAT, // 微信支付
    }

    @NotNull
    private Long orderId;
    @NotNull
    private PaymentType paymentType;
}
