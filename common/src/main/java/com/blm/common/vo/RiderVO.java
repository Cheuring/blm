package com.blm.common.vo;

import com.blm.common.entity.Rider;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RiderVO {
    private Long id;
    private Long userId;
    private String realName;
    private String vehicleType;
    private String vehicleNumber;
    private Rider.RiderStatus status;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private LocalDateTime createdAt;
}