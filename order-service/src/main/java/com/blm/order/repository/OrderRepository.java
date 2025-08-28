package com.blm.order.repository;

import com.blm.common.entity.Order;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface OrderRepository {
    
    @Insert("INSERT INTO orders(order_no, user_id, store_id, address_id, total_amount, delivery_fee, " +
            "discount_amount, coupon_discount, user_coupon_id, payment_amount, status, dispatch_type, " +
            "remark, expected_time, created_at, updated_at) " +
            "VALUES(#{orderNo}, #{userId}, #{storeId}, #{addressId}, #{totalAmount}, #{deliveryFee}, " +
            "#{discountAmount}, #{couponDiscount}, #{userCouponId}, #{paymentAmount}, #{status}, " +
            "#{dispatchType}, #{remark}, #{expectedTime}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);
    
    @Select("SELECT * FROM orders WHERE id = #{id}")
    Optional<Order> findById(Long id);
    
    @Select("SELECT * FROM orders WHERE id = #{id} AND user_id = #{userId}")
    Optional<Order> findByIdAndUserId(Long id, Long userId);
    
    @Select("SELECT * FROM orders WHERE id = #{id} AND store_id = #{storeId}")
    Optional<Order> findByIdAndStoreId(Long id, Long storeId);
    
    @Select("SELECT * FROM orders WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{size} OFFSET #{offset}")
    List<Order> findByUserIdWithPagination(@Param("userId") Long userId, 
                                          @Param("size") Integer size, 
                                          @Param("offset") Integer offset);
    
    @Select("SELECT * FROM orders WHERE store_id = #{storeId} " +
            "#{status != null ? 'AND status = #{status}' : ''} " +
            "ORDER BY created_at DESC LIMIT #{size} OFFSET #{offset}")
    List<Order> findByStoreIdWithPagination(@Param("storeId") Long storeId,
                                           @Param("status") String status,
                                           @Param("size") Integer size,
                                           @Param("offset") Integer offset);
    
    @Update("UPDATE orders SET status = #{status}, updated_at = #{now} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Order.OrderStatus status, @Param("now") LocalDateTime now);
    
    @Update("UPDATE orders SET rider_id = #{riderId}, status = #{status}, updated_at = #{now} WHERE id = #{id}")
    int updateRiderAndStatus(@Param("id") Long id, 
                           @Param("riderId") Long riderId, 
                           @Param("status") Order.OrderStatus status, 
                           @Param("now") LocalDateTime now);
    
    @Update("UPDATE orders SET payment_type = #{paymentType}, status = #{status}, updated_at = #{now} WHERE id = #{id} AND user_id = #{userId}")
    int updatePayment(@Param("id") Long id, 
                     @Param("userId") Long userId,
                     @Param("paymentType") Order.PaymentType paymentType, 
                     @Param("status") Order.OrderStatus status, 
                     @Param("now") LocalDateTime now);
    
    @Update("UPDATE orders SET status = #{status}, actual_time = #{actualTime}, updated_at = #{now} WHERE id = #{id}")
    int updateStatusAndActualTime(@Param("id") Long id, 
                                @Param("status") Order.OrderStatus status, 
                                @Param("actualTime") LocalDateTime actualTime, 
                                @Param("now") LocalDateTime now);
    
    @Select("SELECT * FROM orders WHERE status = #{status} ORDER BY created_at ASC")
    List<Order> findByStatus(@Param("status") Order.OrderStatus status);
    
    @Select("SELECT * FROM orders WHERE rider_id = #{riderId} ORDER BY created_at DESC")
    List<Order> findByRiderId(@Param("riderId") Long riderId);
    
    @Delete("DELETE FROM orders WHERE id = #{id}")
    void deleteById(Long id);
}
