package com.blm.order.repository;

import com.blm.common.entity.Cart;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface CartRepository {
    
    @Insert("INSERT INTO cart(user_id, store_id, food_id, quantity, created_at, updated_at) " +
            "VALUES(#{userId}, #{storeId}, #{foodId}, #{quantity}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Cart cart);
    
    @Select("SELECT * FROM cart WHERE id = #{id}")
    Optional<Cart> findById(Long id);
    
    @Select("SELECT * FROM cart WHERE id = #{id} AND user_id = #{userId}")
    Optional<Cart> findByIdAndUserId(Long id, Long userId);
    
    @Select("SELECT * FROM cart WHERE user_id = #{userId} AND store_id = #{storeId}")
    List<Cart> findByUserIdAndStoreId(Long userId, Long storeId);
    
    @Select("SELECT * FROM cart WHERE user_id = #{userId} AND store_id = #{storeId} AND food_id = #{foodId}")
    Optional<Cart> findByUserIdAndStoreIdAndFoodId(Long userId, Long storeId, Long foodId);
    
    @Update("UPDATE cart SET quantity = #{quantity}, updated_at = #{now} WHERE id = #{id}")
    int updateQuantity(@Param("id") Long id, @Param("quantity") Integer quantity, @Param("now") LocalDateTime now);
    
    @Delete("DELETE FROM cart WHERE id = #{id}")
    void deleteById(Long id);
    
    @Delete("DELETE FROM cart WHERE id = #{id} AND user_id = #{userId}")
    void deleteByIdAndUserId(Long id, Long userId);
    
    @Delete("DELETE FROM cart WHERE user_id = #{userId} AND store_id = #{storeId}")
    void deleteByUserIdAndStoreId(Long userId, Long storeId);
}
