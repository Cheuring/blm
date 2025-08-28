package com.blm.store.repository;

import com.blm.common.entity.FoodCategory;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Optional;

@Mapper
public interface FoodCategoryRepository {

    @Select("SELECT * FROM food_category WHERE store_id = #{storeId} ORDER BY sort ASC")
    List<FoodCategory> findByStoreId(Long storeId);

    @Select("SELECT * FROM food_category WHERE id = #{id}")
    Optional<FoodCategory> findById(Long id);

    @Select("SELECT * FROM food_category WHERE id = #{id} AND store_id = #{storeId}")
    Optional<FoodCategory> findByIdAndStoreId(@Param("id") Long id, @Param("storeId") Long storeId);

    @Insert("INSERT INTO food_category(store_id, name, sort, created_at, updated_at) " +
            "VALUES(#{storeId}, #{name}, #{sort}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FoodCategory category);

    @Update("UPDATE food_category SET name=#{name}, sort=#{sort}, updated_at=#{updatedAt} " +
            "WHERE id=#{id} AND store_id=#{storeId}")
    int update(FoodCategory category);

    @Delete("DELETE FROM food_category WHERE id = #{id} AND store_id = #{storeId}")
    int delete(@Param("id") Long id, @Param("storeId") Long storeId);
}
