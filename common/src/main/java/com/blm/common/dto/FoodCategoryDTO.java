package com.blm.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Data
@Schema(description = "商品分类DTO")
public class FoodCategoryDTO {
    
    @Schema(description = "分类名称", required = true)
    @NotBlank(message = "分类名称不能为空")
    private String name;
    
    @Schema(description = "排序", example = "1")
    @NotNull(message = "排序不能为空")
    @Min(value = 0, message = "排序不能小于0")
    private Integer sort;
}
