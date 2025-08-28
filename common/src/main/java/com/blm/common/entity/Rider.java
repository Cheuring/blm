package com.blm.common.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 骑手实体类
 */
@Data
public class Rider {
    
    /**
     * 骑手状态枚举
     */
    public enum RiderStatus {
        ONLINE,     // 在线
        OFFLINE,    // 离线
        SUSPENDED   // 暂停
    }

    /**
     * 车辆类型枚举
     */
    public enum VehicleType {
        BIKE,       // 自行车
        ELECTRIC,   // 电动车
        MOTORCYCLE  // 摩托车
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
