package com.blm.store.repository;

import com.blm.common.entity.Store;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Optional;

@Mapper
public interface StoreRepository {

    @Select("SELECT * FROM store WHERE id = #{id}")
    Optional<Store> findById(Long id);

    @Select("SELECT * FROM store WHERE merchant_id = #{merchantId}")
    List<Store> findByMerchantId(Long merchantId);

    @Insert("INSERT INTO store(merchant_id, name, logo, description, phone, address, longitude, latitude, " +
            "business_hours, delivery_fee, min_order_amount, category_id, status, rating, monthly_sales, " +
            "license_img, permit_img, is_featured, created_at, updated_at) " +
            "VALUES(#{merchantId}, #{name}, #{logo}, #{description}, #{phone}, #{address}, #{longitude}, #{latitude}, " +
            "#{businessHours}, #{deliveryFee}, #{minOrderAmount}, #{categoryId}, #{status}, #{rating}, #{monthlySales}, " +
            "#{licenseImg}, #{permitImg}, #{isFeatured}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Store store);

    @Update("UPDATE store SET name=#{name}, logo=#{logo}, description=#{description}, phone=#{phone}, " +
            "address=#{address}, longitude=#{longitude}, latitude=#{latitude}, business_hours=#{businessHours}, " +
            "delivery_fee=#{deliveryFee}, min_order_amount=#{minOrderAmount}, category_id=#{categoryId}, " +
            "status=#{status}, rating=#{rating}, monthly_sales=#{monthlySales}, license_img=#{licenseImg}, " +
            "permit_img=#{permitImg}, reject_reason=#{rejectReason}, is_featured=#{isFeatured}, " +
            "updated_at=#{updatedAt} WHERE id=#{id}")
    int update(Store store);

    @Delete("DELETE FROM store WHERE id = #{id}")
    int deleteById(Long id);

    @Select("SELECT * FROM store WHERE status = #{status}")
    List<Store> findByStatus(@Param("status") String status);

    @Select("SELECT * FROM store WHERE category_id = #{categoryId} AND status = 'OPEN'")
    List<Store> findByCategoryId(Long categoryId);

    @Select("SELECT merchant_id FROM store WHERE id = #{storeId}")
    Long findMerchantIdByStoreId(Long storeId);
}
