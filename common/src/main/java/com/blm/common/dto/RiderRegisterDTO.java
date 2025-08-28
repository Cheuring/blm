package com.blm.common.dto;

import com.blm.common.entity.Rider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 骑手注册DTO
 */
@Data
@Schema(description = "骑手注册信息")
public class RiderRegisterDTO {

    @Schema(description = "真实姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    @Schema(description = "身份证号码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456789012345678")
    @NotBlank(message = "身份证号码不能为空")
    private String idCard;

    @Schema(description = "身份证正面照片URL", example = "https://example.com/idcard_front.jpg")
    private String idCardFront;

    @Schema(description = "身份证反面照片URL", example = "https://example.com/idcard_back.jpg")
    private String idCardBack;

    @Schema(description = "车辆类型", example = "ELECTRIC")
    @NotNull(message = "车辆类型不能为空")
    private Rider.VehicleType vehicleType;

    @Schema(description = "车辆号码", example = "粤B12345")
    private String vehicleNumber;
}
