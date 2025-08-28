package com.blm.common.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class History {
    public static String TYPE_STORE = "STORE";
    public static String TYPE_FOOD = "FOOD";

    private Long id;
    private Long userId;
    private Long targetId;
    private String type;
    private LocalDateTime createdAt;
}