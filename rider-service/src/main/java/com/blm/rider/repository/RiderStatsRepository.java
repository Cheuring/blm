package com.blm.rider.repository;

import com.blm.common.entity.RiderStats;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 骑手统计数据访问层
 */
@Mapper
public interface RiderStatsRepository {

    @Insert("INSERT INTO rider_stats(rider_id, date, orders_count, completed_orders, canceled_orders, " +
            "total_income, created_at, updated_at) " +
            "VALUES(#{riderId}, #{date}, #{ordersCount}, #{completedOrders}, #{canceledOrders}, " +
            "#{totalIncome}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(RiderStats stats);

    @Update("UPDATE rider_stats SET orders_count = #{ordersCount}, completed_orders = #{completedOrders}, " +
            "canceled_orders = #{canceledOrders}, total_income = #{totalIncome}, updated_at = #{updatedAt} " +
            "WHERE id = #{id}")
    int update(RiderStats stats);

    @Select("SELECT * FROM rider_stats WHERE rider_id = #{riderId} AND date = #{date}")
    Optional<RiderStats> findByRiderIdAndDate(@Param("riderId") Long riderId, @Param("date") LocalDate date);

    @Select("SELECT * FROM rider_stats WHERE rider_id = #{riderId} AND date BETWEEN #{startDate} AND #{endDate} ORDER BY date")
    List<RiderStats> findByRiderIdAndDateRange(@Param("riderId") Long riderId, 
                                               @Param("startDate") LocalDate startDate, 
                                               @Param("endDate") LocalDate endDate);

    @Select("SELECT * FROM rider_stats WHERE rider_id = #{riderId} ORDER BY date DESC")
    List<RiderStats> findByRiderId(@Param("riderId") Long riderId);

    int save(RiderStats stats);
}
