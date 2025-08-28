package com.blm.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评价视图对象
 */
@Data
@Schema(description = "评价视图对象")
public class ReviewVO {
    /**
     * 评价ID
     */
    @Schema(description = "评价ID")
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 用户名称 (可选，可以在Service中关联查询)
     */
    @Schema(description = "用户名称")
    private String userName;

    /**
     * 用户头像 (可选，可以在Service中关联查询)
     */
    @Schema(description = "用户头像")
    private String userAvatar;

    /**
     * 订单ID
     */
    @Schema(description = "订单ID")
    private Long orderId;

    /**
     * 店铺ID
     */
    @Schema(description = "店铺ID")
    private Long storeId;
    
    /**
     * 店铺名称
     */
    @Schema(description = "店铺名称")
    private String storeName;
    
    /**
     * 评价内容
     */
    @Schema(description = "评价内容")
    private String content;

    /**
     * 评分(1-5)
     */
    @Schema(description = "店铺评分(1-5)")
    private Integer storeRating;
    @Schema(description = "骑手评分(1-5)")
    private Integer riderRating;

    /**
     * 评价图片URLs，以逗号分隔
     */
    @Schema(description = "评价图片URLs，以逗号分隔")
    private String images;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}
