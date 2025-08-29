package com.blm.store.repository;

import com.blm.common.entity.StoreCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StoreCategoryRepository {


    @Select("SELECT * FROM store_category")
    List<StoreCategory> findAll();

    @Insert("INSERT INTO store_category (name, icon, sort, created_at) VALUES (#{name}, #{icon}, #{sort}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(StoreCategory category);

    @Select("SELECT * FROM store_category WHERE id = #{id}")
    StoreCategory findById(Long id);

    @Update("UPDATE store_category SET name = #{name}, icon = #{icon}, sort = #{sort}, created_at = #{createdAt} WHERE id = #{id}")
    void update(StoreCategory category);

    @Delete("DELETE FROM store_category WHERE id = #{id}")
    void deleteById(Long id);
}