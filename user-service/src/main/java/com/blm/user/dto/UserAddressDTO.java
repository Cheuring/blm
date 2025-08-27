package com.blm.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户地址DTO
 */
@Data
@Schema(description = "用户地址数据传输对象")
public class UserAddressDTO {

    @Schema(description = "收货人姓名", example = "张三")
    @NotBlank(message = "收货人姓名不能为空")
    private String receiver;

    @Schema(description = "联系电话", example = "13812345678")
    @NotBlank(message = "联系电话不能为空")
    private String phone;

    @Schema(description = "省份", example = "北京市")
    @NotBlank(message = "省份不能为空")
    private String province;

    @Schema(description = "城市", example = "北京市")
    @NotBlank(message = "城市不能为空")
    private String city;

    @Schema(description = "区县", example = "海淀区")
    @NotBlank(message = "区县不能为空")
    private String district;

    @Schema(description = "详细地址", example = "中关村大街1号")
    @NotBlank(message = "详细地址不能为空")
    private String detailAddress;

    @Schema(description = "是否设为默认地址: 0-否, 1-是", example = "0")
    private Integer isDefault;
}
