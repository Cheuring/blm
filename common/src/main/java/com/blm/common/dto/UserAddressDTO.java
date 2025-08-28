package com.blm.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户地址数据传输对象")
public class UserAddressDTO {
    @Schema(description = "收货人姓名", example = "张三", required = true)
    private String receiver;
    @Schema(description = "收货人手机号", example = "13800138000", required = true)
    private String phone;
    @Schema(description = "省份", example = "广东省", required = true)
    private String province;
    @Schema(description = "城市", example = "广州市", required = true)
    private String city;
    @Schema(description = "区县", example = "天河区", required = true)
    private String district;
    @Schema(description = "详细地址", example = "天汇大厦101", required = true)
    private String detailAddress;
    @Schema(description = "是否默认地址", example = "0", allowableValues = "0, 1")
    private Integer isDefault; // 0 or 1
}