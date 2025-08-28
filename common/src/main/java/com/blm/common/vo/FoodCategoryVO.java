package com.blm.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商品分类视图对象")
public class FoodCategoryVO {
    @Schema(description = "分类ID")
    private Long id;

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "排序字段")
    private Integer sort;

    // Consider adding a list of FoodVO if needed for nested display
    // private List<FoodVO> foods;
}