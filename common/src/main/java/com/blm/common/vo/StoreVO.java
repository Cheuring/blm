package com.blm.common.vo;

import com.blm.common.entity.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "店铺视图对象")
public class StoreVO {
    
    @Schema(description = "店铺ID")
    private Long id;
    
    @Schema(description = "店铺名称")
    private String name;
    
    @Schema(description = "店铺描述")
    private String description;
    
    @Schema(description = "店铺图片")
    private String image;
    
    @Schema(description = "店铺地址")
    private String address;
    
    @Schema(description = "联系电话")
    private String phone;
    
    @Schema(description = "配送费")
    private BigDecimal deliveryFee;
    
    @Schema(description = "起送金额")
    private BigDecimal minDeliveryAmount;
    
    @Schema(description = "店铺状态")
    private Store.StoreStatus status;
}
