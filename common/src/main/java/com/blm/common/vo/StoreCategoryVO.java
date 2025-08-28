package com.blm.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StoreCategoryVO {
    @Schema(description = "分类ID", example = "1")
    private Long id;

    @Schema(description = "分类名称", example = "快餐简餐")
    private String name;

    @Schema(description = "排序字段", example = "1")
    private Integer sort;

    @Schema(description = "分类图标 URL", example = "https://example.com/icon.png")
    private String icon;
}
