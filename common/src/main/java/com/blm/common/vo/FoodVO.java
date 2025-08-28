package com.blm.common.vo;

import com.blm.common.entity.Food;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "商品视图对象")
public class FoodVO {
    
    @Schema(description = "商品ID")
    private Long id;
    
    @Schema(description = "店铺ID")
    private Long storeId;
    
    @Schema(description = "分类ID")
    private Long categoryId;
    
    @Schema(description = "商品名称")
    private String name;
    
    @Schema(description = "商品描述")
    private String description;
    
    @Schema(description = "商品图片")
    private String image;
    
    @Schema(description = "商品价格")
    private BigDecimal price;
    
    @Schema(description = "库存数量")
    private Integer stock;
    
    @Schema(description = "商品状态")
    private Food.FoodStatus status;
}
