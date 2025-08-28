package com.blm.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "添加/删除历史请求体")
public class HistoryDTO {
    @NotNull(message = "目标ID不能为空")
    @Schema(description = "要添加或删除历史的店铺/商品ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long targetId;
}