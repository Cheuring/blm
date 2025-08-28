package com.blm.common.dto;

import com.blm.entity.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新店铺状态数据传输对象 (通常由店铺或系统使用)")
public class StoreStatusUpdateDTO {
    @Schema(description = "新的订单状态", requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"OPEN", "CLOSED", "SUSPENDED", "PENDING"},
            example = "PENDING")
    private Store.StoreStatus storeStatus;
}
