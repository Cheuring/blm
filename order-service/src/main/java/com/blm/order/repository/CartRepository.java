package com.blm.order.repository;

import com.blm.common.entity.Cart;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CartRepository {

    @Select("SELECT * FROM cart WHERE user_id = #{userId}")
    List<Cart> findAllByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM cart WHERE id = #{id} AND user_id = #{userId}")
    Optional<Cart> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Select("SELECT * FROM cart WHERE user_id = #{userId} AND store_id = #{storeId}")
    List<Cart> findByUserStore(@Param("userId") Long userId, @Param("storeId") Long storeId);

    @Insert("INSERT INTO cart(user_id, store_id, food_id, quantity, created_at, updated_at) VALUES(#{userId}, #{storeId}, #{foodId}, #{quantity}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Cart cart);

    @Update("UPDATE cart SET quantity = #{quantity}, updated_at = #{updatedAt} WHERE id = #{id} AND user_id = #{userId}")
    int updateQuantity(Cart cart);

    @Delete("DELETE FROM cart WHERE id = #{id} AND user_id = #{userId}")
    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Delete("DELETE FROM cart WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);

    @Select("SELECT * FROM cart WHERE user_id = #{userId} AND store_id = #{storeId}")
    List<Cart> findAllByUserIdAndStoreId(Long userId, Long storeId);

    @Delete("DELETE FROM cart WHERE user_id = #{userId} AND store_id = #{storeId}")
    int deleteByUserIdAndStoreId(Long userId, Long storeId);
}