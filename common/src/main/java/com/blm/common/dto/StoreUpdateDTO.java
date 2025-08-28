package com.blm.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "更新店铺数据传输对象")
public class StoreUpdateDTO {
    @Schema(description = "店铺名称")
    private String name;
    @Schema(description = "店铺Logo URL")
    private String logo;
    @Schema(description = "店铺描述")
    private String description;
    @Schema(description = "店铺联系电话")
    private String phone;
    @Schema(description = "店铺详细地址")
    private String address;
    @Schema(description = "经度")
    private BigDecimal longitude;
    @Schema(description = "纬度")
    private BigDecimal latitude;
    @Schema(description = "营业时间", example = "08:00-22:00")
    private String businessHours;
    @Schema(description = "配送费")
    private BigDecimal deliveryFee;
    @Schema(description = "起送金额")
    private BigDecimal minOrderAmount;
    @Schema(description = "平均配送时间(分钟)")
    private Integer averageDeliveryTime;
    @Schema(description = "店铺分类ID")
    private Long categoryId;
    @Schema(description = "营业执照图片URL")
    private String licenseImg;
    @Schema(description = "许可证图片URL")
    private String permitImg;
}