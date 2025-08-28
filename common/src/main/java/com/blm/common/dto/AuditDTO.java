package com.blm.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 审核数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "店铺审核DTO")
public class AuditDTO {
    public enum AuditStatus {
        APPROVED,
        SUSPENDED
    }

    @Schema(description = "审核状态 (APPROVED/SUSPENDED)", example = "APPROVED", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private AuditStatus status;

    @Schema(description = "拒绝原因", example = "资质不符合要求")
    private String reason;
}