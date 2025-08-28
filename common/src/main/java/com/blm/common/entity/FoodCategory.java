package com.blm.common.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FoodCategory {
    private Long id;
    private Long storeId;
    private String name;
    private Integer sort;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
