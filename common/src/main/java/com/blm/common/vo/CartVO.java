package com.blm.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "购物车视图对象")
public class CartVO {
    
    @Schema(description = "购物车中的商品列表")
    private List<CartItemVO> items;
    
    @Schema(description = "购物车商品总金额")
    private BigDecimal totalAmount;
    
    @Schema(description = "购物车所属店铺ID")
    private Long storeId;
    
    @Schema(description = "购物车所属店铺名称")
    private String storeName;
    
    @Schema(description = "购物车商品总数量")
    private Integer totalItems;
}
