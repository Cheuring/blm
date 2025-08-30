package com.blm.store.repository;

import com.blm.common.entity.Promotion;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PromotionRepository {

    @Select("SELECT * FROM promotion WHERE store_id = #{storeId}")
    List<Promotion> findByStoreId(@Param("storeId") Long storeId);

    @Select("SELECT * FROM promotion WHERE id = #{id} AND store_id = #{storeId}")
    Optional<Promotion> findByIdAndStoreId(@Param("id") Long id, @Param("storeId") Long storeId);

    @Insert("INSERT INTO promotion(store_id, name, description, start_time, end_time, discount_type, discount_value, min_order_amount, status, created_at) " +
            "VALUES(#{storeId}, #{name}, #{description}, #{startTime}, #{endTime}, #{discountType}, #{discountValue}, #{minOrderAmount}, #{status}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Promotion promotion);

//    @Update("UPDATE promotion SET name=#{name}, description=#{description}, start_time=#{startTime}, end_time=#{endTime}, discount_type=#{discountType}, discount_value=#{discountValue}, min_order_amount=#{minOrderAmount}, status=#{status} " +
//            "WHERE id=#{id} AND store_id=#{storeId}")
    int update(Promotion promotion);

    @Delete("DELETE FROM promotion WHERE id = #{id} AND store_id = #{storeId}")
    int delete(@Param("id") Long id, @Param("storeId") Long storeId);

    @Select("SELECT * FROM promotion WHERE status = 1")
    List<Promotion> findAll(); // 查询指定店铺的所有活动
}