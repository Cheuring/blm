package com.blm.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户地址视图对象
 */
@Data
@Schema(description = "用户地址视图对象")
public class UserAddressVO {
    @Schema(description = "地址ID")
    private Long id;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "收货人")
    private String receiver;
    
    @Schema(description = "联系电话")
    private String phone;
    
    @Schema(description = "省份")
    private String province;
    
    @Schema(description = "城市")
    private String city;
    
    @Schema(description = "区县")
    private String district;
    
    @Schema(description = "详细地址")
    private String detailAddress;
    
    @Schema(description = "是否默认地址: 0-否, 1-是")
    private Integer isDefault;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}
