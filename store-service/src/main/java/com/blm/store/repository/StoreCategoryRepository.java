package com.blm.store.repository;

import com.blm.common.entity.StoreCategory;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Optional;

@Mapper
public interface StoreCategoryRepository {

    @Select("SELECT * FROM store_category ORDER BY sort ASC")
    List<StoreCategory> findAll();

    @Select("SELECT * FROM store_category WHERE id = #{id}")
    Optional<StoreCategory> findById(Long id);

    @Insert("INSERT INTO store_category(name, icon, sort, created_at) " +
            "VALUES(#{name}, #{icon}, #{sort}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(StoreCategory category);

    @Update("UPDATE store_category SET name=#{name}, icon=#{icon}, sort=#{sort} WHERE id=#{id}")
    int update(StoreCategory category);

    @Delete("DELETE FROM store_category WHERE id = #{id}")
    int deleteById(Long id);
}
