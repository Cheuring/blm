package com.blm.common.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
// todo: 修改
public class HistoryVO {
    private Long id;
    private String type; // STORE or FOOD
    private Long targetId;
    private LocalDateTime createdAt;
}