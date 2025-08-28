package com.blm.common.vo;

import com.blm.common.entity.Rider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 骑手视图对象
 */
@Data
@Schema(description = "骑手信息")
public class RiderVO {

    @Schema(description = "骑手ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "车辆类型")
    private Rider.VehicleType vehicleType;

    @Schema(description = "车辆号码")
    private String vehicleNumber;

    @Schema(description = "工作状态")
    private Rider.RiderStatus status;

    @Schema(description = "经度")
    private BigDecimal longitude;

    @Schema(description = "纬度")
    private BigDecimal latitude;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}
