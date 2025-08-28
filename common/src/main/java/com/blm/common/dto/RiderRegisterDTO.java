package com.blm.common.dto;

import com.blm.common.entity.Rider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class RiderRegisterDTO {

    @Schema(description = "真实姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String realName;
    @Schema(description = "身份证号码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456789012345678")
    private String idCard;
    @Schema(description = "身份证正面照片URL", example = "https://example.com/idcard_front.jpg")
    private String idCardFront;
    @Schema(description = "身份证反面照片URL", example = "https://example.com/idcard_back.jpg")
    private String idCardBack;
    @Schema(description = "车辆类型", example = "BIKE")
    private Rider.VehicleType vehicleType;
    @Schema(description = "车辆号码", example = "粤B12345")
    private String vehicleNumber;
}