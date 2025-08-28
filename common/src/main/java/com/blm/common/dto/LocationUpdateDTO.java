package com.blm.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 位置更新DTO
 */
@Data
@Schema(description = "位置更新信息")
public class LocationUpdateDTO {

    @Schema(description = "经度", example = "116.404")
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    @Schema(description = "纬度", example = "39.915")
    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;
}
