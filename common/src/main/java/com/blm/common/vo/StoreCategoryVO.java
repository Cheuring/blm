package com.blm.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "店铺分类视图对象")
public class StoreCategoryVO {
    
    @Schema(description = "分类ID")
    private Long id;
    
    @Schema(description = "分类名称")
    private String name;
    
    @Schema(description = "分类图标")
    private String icon;
    
    @Schema(description = "排序")
    private Integer sort;
}
