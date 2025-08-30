package com.blm.store.repository;

import com.blm.common.entity.Food;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FoodRepository {

    @Select("SELECT * FROM food WHERE id = #{id}")
    Optional<Food> findById(@Param("id") Long id); // todo: 调用的方法涉及商品下架问题

    @Select("SELECT * FROM food WHERE id = #{id} AND status = 'ON_SHELF'")
    Optional<Food> findONById(@Param("id") Long id);

    @Select("SELECT * FROM food WHERE id = #{id} AND status = #{status}")
    Optional<Food> findByIdAndStatus(@Param("id") Long id, @Param("status") Food.FoodStatus status);

    @Select("SELECT * FROM food WHERE store_id = #{storeId} AND status = 'ON_SHELF'")
    List<Food> findONByStoreId(Long storeId);

    @Select("SELECT * FROM food WHERE store_id = #{storeId} AND is_featured = 1 AND status = 'ON_SHELF'")
    List<Food> findONFeaturedByStoreId(Long storeId);

    @Insert("INSERT INTO food(store_id, category_id, name, price, original_price, description, image, sales, status, is_featured, created_at, updated_at) " +
            "VALUES(#{storeId}, #{categoryId}, #{name}, #{price}, #{originalPrice}, #{description}, #{image}, #{sales}, #{status}, #{isFeatured}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Food food);

//    @Update("UPDATE food SET category_id=#{categoryId}, name=#{name}, price=#{price}, original_price=#{originalPrice}, description=#{description}, image=#{image}, status=#{status}, is_featured=#{isFeatured}, updated_at=#{updatedAt} WHERE id=#{id} AND store_id=#{storeId}")
    int update(Food food);

    @Delete("DELETE FROM food WHERE id=#{id} AND store_id=#{storeId}")
    int deleteByIdAndStoreId(@Param("id") Long id, @Param("storeId") Long storeId);

    @Update("UPDATE food SET status=#{status}, updated_at=#{updatedAt} WHERE id=#{id} AND store_id=#{storeId}")
    int updateStatus(@Param("id") Long id, @Param("storeId") Long storeId, @Param("status") Food.FoodStatus status, @Param("updatedAt") java.time.LocalDateTime updatedAt);

    /**
     * 根据状态、关键字和店铺ID查询商品列表（支持PageHelper）
     */
    List<Food> findByStatusAndKeywordAndStoreId(
            @Param("status") Food.FoodStatus status,
            @Param("keyword") String keyword,
            @Param("storeId") Long storeId);

    /**
     * 保存商品信息（插入或更新）
     */
    int save(Food food);

    @Select("SELECT * FROM food WHERE store_id = #{storeId} AND category_id = #{categoryId}")
    List<Food> findByStoreIdAndCategoryId(@Param("storeId") Long storeId, @Param("categoryId") Long categoryId);

    @Select("SELECT * FROM food WHERE store_id = #{storeId} AND category_id = #{categoryId} AND status = 'ON_SHELF'")
    List<Food> findONByStoreIdAndCategoryId(@Param("storeId") Long storeId, @Param("categoryId") Long categoryId);

    @Select("SELECT * FROM food WHERE store_id = #{storeId} AND status = #{status}")
    List<Food> findByStoreIdAndStatus(@Param("storeId") Long storeId, @Param("status") Food.FoodStatus status);

    @Select("SELECT * FROM food WHERE store_id = #{storeId} AND category_id = #{categoryId} AND status = #{status}")
    List<Food> findByStoreIdAndCategoryIdAndStatus(@Param("storeId") Long storeId, @Param("categoryId") Long categoryId, @Param("status") Food.FoodStatus status);
}