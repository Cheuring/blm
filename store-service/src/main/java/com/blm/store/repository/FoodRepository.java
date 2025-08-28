package com.blm.store.repository;

import com.blm.common.entity.Food;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Optional;

@Mapper
public interface FoodRepository {

    @Select("SELECT * FROM food WHERE id = #{id}")
    Optional<Food> findById(Long id);

    @Select("SELECT * FROM food WHERE store_id = #{storeId} AND status = 'ON_SHELF' ORDER BY sales DESC")
    List<Food> findByStoreId(Long storeId);

    @Select("SELECT * FROM food WHERE store_id = #{storeId} AND category_id = #{categoryId} AND status = 'ON_SHELF'")
    List<Food> findByStoreIdAndCategoryId(@Param("storeId") Long storeId, @Param("categoryId") Long categoryId);

    @Select("SELECT * FROM food WHERE store_id = #{storeId} AND is_featured = 1 AND status = 'ON_SHELF' LIMIT 10")
    List<Food> findFeaturedByStoreId(Long storeId);

    @Insert("INSERT INTO food(store_id, category_id, name, price, original_price, description, image, " +
            "sales, stock, status, is_featured, created_at, updated_at) " +
            "VALUES(#{storeId}, #{categoryId}, #{name}, #{price}, #{originalPrice}, #{description}, #{image}, " +
            "#{sales}, #{stock}, #{status}, #{isFeatured}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Food food);

    @Update("UPDATE food SET category_id=#{categoryId}, name=#{name}, price=#{price}, " +
            "original_price=#{originalPrice}, description=#{description}, image=#{image}, " +
            "stock=#{stock}, status=#{status}, is_featured=#{isFeatured}, updated_at=#{updatedAt} " +
            "WHERE id=#{id} AND store_id=#{storeId}")
    int update(Food food);

    @Delete("DELETE FROM food WHERE id = #{id} AND store_id = #{storeId}")
    int delete(@Param("id") Long id, @Param("storeId") Long storeId);

    @Update("UPDATE food SET sales = sales + #{quantity} WHERE id = #{foodId}")
    int updateSales(@Param("foodId") Long foodId, @Param("quantity") Integer quantity);

    @Update("UPDATE food SET stock = stock - #{quantity} WHERE id = #{foodId} AND stock >= #{quantity}")
    int decreaseStock(@Param("foodId") Long foodId, @Param("quantity") Integer quantity);
}
