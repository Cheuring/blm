package com.blm.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "商品分类视图对象")
public class FoodCategoryVO {
    
    @Schema(description = "分类ID")
    private Long id;
    
    @Schema(description = "店铺ID")
    private Long storeId;
    
    @Schema(description = "分类名称")
    private String name;
    
    @Schema(description = "排序")
    private Integer sort;
}
