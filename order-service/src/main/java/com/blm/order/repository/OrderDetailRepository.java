package com.blm.order.repository;

import com.blm.common.entity.OrderDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderDetailRepository {

    @Insert("INSERT INTO order_detail(order_id, food_id, food_name, food_image, price, quantity, amount, created_at) " +
            "VALUES(#{orderId}, #{foodId}, #{foodName}, #{foodImage}, #{price}, #{quantity}, #{amount}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderDetail detail);

    @Select("SELECT * FROM order_detail WHERE order_id = #{orderId}")
    List<OrderDetail> findByOrderId(Long orderId);

    @Delete("DELETE FROM order_detail WHERE id = #{id}")
    void deleteById(Long id);
}