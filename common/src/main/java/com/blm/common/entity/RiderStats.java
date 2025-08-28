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
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 骑手ID
     */
    private Long riderId;
    
    /**
     * 统计日期
     */
    private LocalDate date;
    
    /**
     * 订单数量
     */
    private Integer ordersCount;
    
    /**
     * 完成的订单数
     */
    private Integer completedOrders;
    
    /**
     * 取消的订单数
     */
    private Integer canceledOrders;
    
    /**
     * 总收入
     */
    private BigDecimal totalIncome;
    
    /**
     * 平均配送时间(分钟)
     */
//    private BigDecimal avgDeliveryTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}