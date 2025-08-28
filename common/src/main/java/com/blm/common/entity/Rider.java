package com.blm.common.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Rider {
    public enum RiderStatus {
        ONLINE,
        OFFLINE,
        SUSPENDED,
    }

    public enum VehicleType {
        BIKE,
        MOTORBIKE,
        ELECTRIC,
    }

    private Long id;
    private Long userId;
    private String realName;
    private String idCard;
    private String idCardFront;
    private String idCardBack;
    private VehicleType vehicleType;
    private String vehicleNumber;
    private RiderStatus status;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}