package com.blm.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
@Schema(description = "店铺创建DTO")
public class StoreCreateDTO {
    
    @Schema(description = "店铺名称", required = true)
    @NotBlank(message = "店铺名称不能为空")
    private String name;
    
    @Schema(description = "店铺描述")
    private String description;
    
    @Schema(description = "联系电话", required = true)
    @NotBlank(message = "联系电话不能为空")
    private String phone;
    
    @Schema(description = "店铺地址", required = true)
    @NotBlank(message = "店铺地址不能为空")
    private String address;
    
    @Schema(description = "经度")
    private BigDecimal longitude;
    
    @Schema(description = "纬度")
    private BigDecimal latitude;
    
    @Schema(description = "营业时间")
    private String businessHours;
    
    @Schema(description = "配送费")
    @DecimalMin(value = "0.0", message = "配送费不能小于0")
    private BigDecimal deliveryFee;
    
    @Schema(description = "起送金额")
    @DecimalMin(value = "0.0", message = "起送金额不能小于0")
    private BigDecimal minOrderAmount;
    
    @Schema(description = "店铺分类ID", required = true)
    @NotNull(message = "店铺分类不能为空")
    private Long categoryId;
    
    @Schema(description = "店铺logo")
    private String logo;
    
    @Schema(description = "平均配送时间(分钟)")
    private Integer averageDeliveryTime;
}
