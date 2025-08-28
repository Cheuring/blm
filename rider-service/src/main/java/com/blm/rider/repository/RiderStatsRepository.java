package com.blm.rider.repository;

import com.blm.common.entity.RiderStats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface RiderStatsRepository {

    /**
     * 根据骑手ID和日期范围查询统计数据
     */
    List<RiderStats> findByRiderIdAndDateRange(
            @Param("riderId") Long riderId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * 根据骑手ID和日期查询统计数据
     */
    RiderStats findByRiderIdAndDate(
            @Param("riderId") Long riderId,
            @Param("date") LocalDate date);

    /**
     * 插入或更新统计数据
     */
    int insertOrUpdate(RiderStats stats);

    /**
     * 查询骑手指定日期范围内的订单总数
     */
    Integer countOrdersByRiderIdAndDateRange(
            @Param("riderId") Long riderId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * 查询骑手指定日期范围内的已完成订单总数
     */
    Integer countCompletedOrdersByRiderIdAndDateRange(
            @Param("riderId") Long riderId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * 查询骑手指定日期范围内的已取消订单总数
     */
    Integer countCanceledOrdersByRiderIdAndDateRange(
            @Param("riderId") Long riderId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * 查询骑手指定日期范围内的总收入
     */
    BigDecimal sumIncomeByRiderIdAndDateRange(
            @Param("riderId") Long riderId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}