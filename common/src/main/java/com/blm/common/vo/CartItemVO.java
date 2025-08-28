package com.blm.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "购物车项目视图对象")
public class CartItemVO {
    
    @Schema(description = "购物车项目ID")
    private Long id;
    
    @Schema(description = "商品ID")
    private Long foodId;
    
    @Schema(description = "商品名称")
    private String foodName;
    
    @Schema(description = "商品图片URL")
    private String foodImage;
    
    @Schema(description = "商品单价")
    private BigDecimal price;
    
    @Schema(description = "数量")
    private Integer quantity;
    
    @Schema(description = "该项总金额")
    private BigDecimal amount;
}
