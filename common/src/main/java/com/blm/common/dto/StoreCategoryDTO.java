package com.blm.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "店铺分类DTO")
public class StoreCategoryDTO {
    
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 50, message = "分类名称长度不能超过50个字符")
    @Schema(description = "分类名称", example = "热销套餐", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    
    @NotNull(message = "排序顺序不能为空")
    @Schema(description = "排序顺序，值越小越靠前", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sort;

    @NotNull(message = "分类图标不能为空")
    @Schema(description = "合法图片路径", example = "/icons/fruits.png", requiredMode = Schema.RequiredMode.REQUIRED)
    private String icon; // 使用合法的图片路径或URL
}