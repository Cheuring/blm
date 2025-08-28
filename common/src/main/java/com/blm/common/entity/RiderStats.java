package com.blm.common.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 骑手统计数据实体类
 */
@Data
public class RiderStats {
    private Long id;
    private Long riderId;
    private LocalDate date;
    private Integer ordersCount;
    private Integer completedOrders;
    private Integer canceledOrders;
    private BigDecimal totalIncome;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
