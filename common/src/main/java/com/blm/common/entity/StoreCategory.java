package com.blm.common.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StoreCategory {
    private Long id;
    private String name;
    private String icon;
    private Integer sort;
    private LocalDateTime createdAt;
}
