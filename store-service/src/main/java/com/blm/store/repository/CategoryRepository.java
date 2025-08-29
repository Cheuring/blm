package com.blm.store.repository;

import com.blm.common.entity.FoodCategory;
import com.blm.common.entity.StoreCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryRepository {

    @Select("SELECT * FROM food_category WHERE store_id = #{storeId}")
    List<FoodCategory> findByStoreId(Long storeId);

    @Insert("INSERT INTO food_category(store_id, name, sort, created_at, updated_at) VALUES(#{storeId}, #{name}, #{sort}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FoodCategory category);

    @Update("UPDATE food_category SET name=#{name}, sort=#{sort}, updated_at=#{updatedAt} WHERE id=#{id} AND store_id=#{storeId}")
    int update(FoodCategory category);

    @Delete("DELETE FROM food_category WHERE id = #{id} AND store_id = #{storeId}")
    int delete(@Param("id") Long id, @Param("storeId") Long storeId);

    @Select("SELECT * FROM food_category WHERE id = #{categoryId}")
    FoodCategory findById(Long categoryId);

    @Select("SELECT * FROM store_category")
    List<StoreCategory> getStoreCategories();
}