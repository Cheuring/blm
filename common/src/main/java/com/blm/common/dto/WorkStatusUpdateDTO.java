package com.blm.common.dto;

import com.blm.common.entity.Rider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 工作状态更新DTO
 */
@Data
@Schema(description = "工作状态更新信息")
public class WorkStatusUpdateDTO {

    @Schema(description = "工作状态", example = "ONLINE")
    @NotNull(message = "工作状态不能为空")
    private Rider.RiderStatus status;
}
