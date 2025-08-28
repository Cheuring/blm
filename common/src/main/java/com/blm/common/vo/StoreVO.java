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
    
    @Schema(description = "商家ID")
    private Long merchantId;
    
    @Schema(description = "店铺名称")
    private String name;
    
    @Schema(description = "店铺logo")
    private String logo;
    
    @Schema(description = "店铺描述")
    private String description;
    
    @Schema(description = "联系电话")
    private String phone;
    
    @Schema(description = "店铺地址")
    private String address;
    
    @Schema(description = "经度")
    private BigDecimal longitude;
    
    @Schema(description = "纬度")
    private BigDecimal latitude;
    
    @Schema(description = "营业时间")
    private String businessHours;
    
    @Schema(description = "配送费")
    private BigDecimal deliveryFee;
    
    @Schema(description = "起送金额")
    private BigDecimal minOrderAmount;
    
    @Schema(description = "平均配送时间(分钟)")
    private Integer averageDeliveryTime;
    
    @Schema(description = "店铺分类ID")
    private Long categoryId;
    
    @Schema(description = "店铺状态")
    private Store.StoreStatus status;
    
    @Schema(description = "评分")
    private BigDecimal rating;
    
    @Schema(description = "月销量")
    private Integer monthlySales;
    
    @Schema(description = "是否推荐")
    private Boolean isFeatured;
}
