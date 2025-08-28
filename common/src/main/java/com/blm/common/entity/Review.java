package com.blm.common.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评价实体类
 */
@Data
public class Review {

    private Long id;
    private Long parentId;
    private Long orderId;
    private Long userId;
    private Long storeId;
    private Long riderId;
    private Integer storeRating;
    private Integer riderRating;
    private String content;
    private String images;
    private String reply;
    private LocalDateTime replyTime;
    private Integer isAnonymous;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}