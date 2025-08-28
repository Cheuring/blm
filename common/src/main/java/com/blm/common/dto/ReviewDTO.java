package com.blm.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 评价数据传输对象
 */
@Data
@Schema(description = "评价数据传输对象")
public class ReviewDTO {
    /**
     * 评价内容
     */
    @Schema(description = "评价内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "评价内容不能为空")
    @Size(min = 1, max = 500, message = "评价内容长度应在1-500个字符之间")
    private String content;
    
    /**
     * 评分(1-5)
     */
    @Schema(description = "商家评分(1-5)", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低为1")
    @Max(value = 5, message = "评分最高为5")
    private Integer storeRating;

    @Schema(description = "骑手评分(1-5)", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低为1")
    @Max(value = 5, message = "评分最高为5")
    private Integer riderRating;
    
    /**
     * 评价图片URLs，以逗号分隔
     */
    @Schema(description = "评价图片URLs，以逗号分隔")
    private String images;
}