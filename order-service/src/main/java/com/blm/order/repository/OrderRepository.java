package com.blm.order.repository;

import com.blm.common.dto.GoodsSalesDTO;
import com.blm.common.dto.PaymentDTO;
import com.blm.common.entity.Order;
import com.blm.common.entity.OrderTracking;
import com.blm.common.vo.PlatformStatsVO;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface OrderRepository {

    @Insert("INSERT INTO orders(order_no, user_id, store_id, rider_id, address_id, total_amount, delivery_fee, discount_amount, payment_amount, payment_type, status, remark, expected_time, created_at, updated_at) " +
            "VALUES(#{orderNo}, #{userId}, #{storeId}, #{riderId}, #{addressId}, #{totalAmount}, #{deliveryFee}, #{discountAmount}, #{paymentAmount}, #{paymentType}, #{status}, #{remark}, #{expectedTime}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);

    @Select("SELECT * FROM orders WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Order> findAllByUserId(Long userId);

    @Select("SELECT * FROM orders WHERE store_id = #{storeId} ORDER BY created_at DESC")
    List<Order> findAllByStoreId(Long storeId);

    /**
     * 根据店铺ID和状态查询订单列表(支持PageHelper)
     */
    List<Order> findByStoreIdAndStatus(@Param("storeId") Long storeId, @Param("status") Order.OrderStatus status);
    /**
     * 根据用户ID和状态查询订单列表（支持PageHelper）
     */
    List<Order> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Order.OrderStatus status);

    @Select("SELECT * FROM orders WHERE id = #{id} AND user_id = #{userId}")
    Optional<Order> findByIdAndUserId(@Param("id") Long orderId, @Param("userId") Long userId);
    
    @Select("SELECT * FROM orders WHERE id = #{id}")
    Optional<Order> findById(Long id);

    @Update("UPDATE orders SET status = #{status}, updated_at = #{updatedAt} WHERE id = #{id} AND user_id = #{userId}")
    int updateStatus(@Param("id") Long id, @Param("userId") Long userId, @Param("status") Order.OrderStatus status, @Param("updatedAt") LocalDateTime updatedAt);

    @Update("UPDATE orders SET status = #{status}, updated_at = #{updatedAt} WHERE id = #{id} AND store_id = #{storeId}")
    int updateStatusByStore(@Param("id") Long id, @Param("storeId") Long storeId, @Param("status") Order.OrderStatus status, @Param("updatedAt") LocalDateTime updatedAt);

    @Select("SELECT * FROM orders WHERE status = 'READY_WAITING_RIDER'")
    List<Order> findAvailableOrders();

    @Select("SELECT * FROM orders WHERE rider_id = #{riderId}")
    List<Order> findByRiderId(Long riderId);

    @Update("UPDATE orders SET rider_id = #{riderId}, status = #{status}, updated_at = #{updatedAt} WHERE id = #{orderId} AND rider_id IS NULL")
    int assignOrder(@Param("orderId") Long orderId, @Param("riderId") Long riderId, @Param("status") Order.OrderStatus status, @Param("updatedAt") LocalDateTime updatedAt);

    @Update("UPDATE orders SET status = #{status}, updated_at = #{updatedAt} WHERE id = #{orderId} AND rider_id = #{riderId}")
    int updateOrderStatusByRider(@Param("orderId") Long orderId, @Param("riderId") Long riderId, @Param("status") Order.OrderStatus status, @Param("updatedAt") LocalDateTime updatedAt);

    @Update("UPDATE orders SET status = #{status}, updated_at = #{updatedAt}, actual_time = #{updatedAt} WHERE id = #{orderId} AND rider_id = #{riderId}")
    int deliveredByRider(@Param("orderId") Long orderId, @Param("riderId") Long riderId, @Param("status") Order.OrderStatus status, @Param("updatedAt") LocalDateTime updatedAt);

    @Select("SELECT * FROM orders")
    List<Order> findAllOrders();

    @Update("UPDATE orders SET status = #{status}, updated_at = #{updatedAt} WHERE id = #{id}")
    int updateStatusAdmin(@Param("id") Long id, @Param("status") String status, @Param("updatedAt") LocalDateTime updatedAt);
    
    /**
     * 查询骑手在指定日期完成的订单
     */
    @Select("SELECT * FROM orders WHERE rider_id = #{riderId} AND status = 'DELIVERED' AND DATE(actual_time) = #{date}")
    List<Order> findCompletedOrdersByRiderAndDate(@Param("riderId") Long riderId, @Param("date") LocalDate date);
    
    /**
     * 查询骑手在指定日期取消的订单
     */
    @Select("SELECT * FROM orders WHERE rider_id = #{riderId} AND status = 'CANCELLED' AND DATE(updated_at) = #{date}")
    List<Order> findCanceledOrdersByRiderAndDate(@Param("riderId") Long riderId, @Param("date") LocalDate date);

    /**
     * 管理员根据条件查询订单列表（支持PageHelper）
     */
    List<Order> findByConditions(
            @Param("status") Order.OrderStatus status,
            @Param("userId") Long userId,
            @Param("storeId") Long storeId,
            @Param("riderId") Long riderId);

    /**
     * 计数总数
     */
    @Select("SELECT COUNT(*) FROM orders")
    long count();
    
    /**
     * 根据状态查询订单总金额
     */
    @Select("SELECT COALESCE(SUM(payment_amount), 0) FROM orders WHERE status = #{status}")
    BigDecimal sumTotalByStatus(Order.OrderStatus status);
            
    /**
     * 根据状态和创建时间区间查询订单数量
     */
    long countByStatusAndCreatedAtBetween(
            @Param("status") Order.OrderStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
    
    /**
     * 根据状态和创建时间区间查询订单总金额
     */
    BigDecimal sumTotalByStatusAndCreatedAtBetween(
            @Param("status") Order.OrderStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Select("SELECT * FROM orders WHERE id=#{id} AND store_id=#{storeId}")
    Optional<Order> findByIdAndStoreId(@Param("id")Long orderId, @Param("storeId")Long storeId);

    @Update("INSERT INTO order_tracking (order_id,status,operator_id,operator_type,created_at)"+
    "VALUES (#{orderId},#{status},#{operatorId},#{operatorType},#{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertOrderTraceByMerchant(OrderTracking orderTracking);

    /**
     * 商家根据起止日期查询相应店铺的营业额
     * @param map
     * @return
     */
    @Select("SELECT COALESCE(SUM(payment_amount), 0) FROM orders WHERE store_id = #{storeId} AND updated_at > #{beginTime} AND updated_at < #{endTime} AND status = #{status}")
    Double sumByMapMoney(Map map);

    /**
     * 商家根据起止日期查询相应店铺的订单量
     * @param map
     * @return
     */
    @Select("SELECT COUNT(*) FROM orders WHERE store_id = #{storeId} AND updated_at > #{beginTime} AND updated_at < #{endTime} AND status = #{status}")
    Integer sumByMapOrder(Map map);

    /**
     * 店铺统计指定时间销量前十的商品
     *
     * @param storeId
     * @param begin
     * @param end
     * @param status
     */
    List<GoodsSalesDTO> findTop10ByStore(Long storeId, LocalDateTime begin, LocalDateTime end, Order.OrderStatus status);

    List<PlatformStatsVO.TopStoreItemVO> findTopStores();


    /**
     * 商家根据时间范围获取订单数量
     * @param status
     * @param todayStart
     * @param todayEnd
     * @param storeId
     * @return
     */
    long countStoreByStatusAndCreatedAtBetween(Order.OrderStatus status, @Param("start")LocalDateTime todayStart, @Param("end")LocalDateTime todayEnd, Long storeId);

    /**
     * 商家根据时间范围获取订单总金额
     * @param status
     * @param todayStart
     * @param todayEnd
     * @param storeId
     * @return
     */
    BigDecimal sumStoreTotalByStatusAndCreatedAtBetween(Order.OrderStatus status, @Param("start")LocalDateTime todayStart,  @Param("end")LocalDateTime todayEnd, Long storeId);

    /**
     * 商家根据状态获取总订单数量
     * @param status
     * @param storeId
     * @return
     */
    long countStoreByStatus(Order.OrderStatus status, Long storeId);

    /**
     * 商家根据状态获取总订单金额
     * @param status
     * @param storeId
     * @return
     */
    BigDecimal sumStoreTotalByStatus(Order.OrderStatus status, Long storeId);

    @Update("UPDATE orders SET payment_type = #{paymentType}, updated_at = #{now} WHERE id = #{orderId} AND user_id = #{userId}")
    int updatePaymentType(Long orderId, Long userId, PaymentDTO.PaymentType paymentType, LocalDateTime now);

    @Delete("DELETE FROM orders WHERE id=#{id}")
    void deleteById(Long orderId);

    @Select("SELECT * FROM orders WHERE id=#{id} AND user_id=#{userId} AND store_id=#{storeId}")
    Optional<Order> findByIdAndUserIdAndStoreId(@Param("id")Long orderId, Long userId, Long storeId);
}