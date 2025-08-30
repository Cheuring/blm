package com.blm.store.repository;

import com.blm.common.dto.StoreQueryDTO;
import com.blm.common.entity.Store;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface StoreRepository {

    @Select("SELECT * FROM store WHERE id = #{id}")
    Optional<Store> findById(@Param("id") Long id);

    @Select("SELECT * FROM store WHERE id = #{id} AND status = #{status}")
    Optional<Store> findByIdAndStatus(@Param("id") Long id, @Param("status") Store.StoreStatus status);

    @Select("SELECT * FROM store WHERE is_featured = 1 AND status = 'OPEN'")
    List<Store> findFeatured();

    @Select("SELECT * FROM store WHERE merchant_id = #{merchantId}")
    List<Store> findByMerchantId(@Param("merchantId") Long merchantId);

    @Select("SELECT * FROM store")
    List<Store> findAllStores();

    @Insert("INSERT INTO store(merchant_id, name, logo, description, phone, address, longitude, latitude, business_hours, delivery_fee, min_order_amount, average_delivery_time, category_id, status, rating, license_img, permit_img, created_at, updated_at) " +
            "VALUES(#{merchantId}, #{name}, #{logo}, #{description}, #{phone}, #{address}, #{longitude}, #{latitude}, #{businessHours}, #{deliveryFee}, #{minOrderAmount}, #{averageDeliveryTime}, #{categoryId}, #{status}, #{rating}, #{licenseImg}, #{permitImg}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertStore(Store store);

//    @Update("UPDATE store SET name=#{name}, logo=#{logo}, description=#{description}, phone=#{phone}, address=#{address}, longitude=#{longitude}, latitude=#{latitude}, business_hours=#{businessHours}, delivery_fee=#{deliveryFee}, min_order_amount=#{minOrderAmount}, average_delivery_time=#{averageDeliveryTime}, category_id=#{categoryId}, license_img=#{licenseImg}, permit_img=#{permitImg}, updated_at=#{updatedAt} " +
//            "WHERE id=#{id} AND merchant_id=#{merchantId}")
    int updateStore(Store store);

    @Update("UPDATE store SET status = #{status}, updated_at = #{updatedAt} WHERE id = #{id} AND merchant_id = #{merchantId}")
    int updateStatus(@Param("id") Long id, @Param("merchantId") Long merchantId, @Param("status") Store.StoreStatus status, @Param("updatedAt") LocalDateTime updatedAt);

    @Update("UPDATE store SET status = #{status}, updated_at = #{updatedAt} WHERE id = #{id}")
    int updateStatusAdmin(@Param("id") Long id, @Param("status") Integer status, @Param("updatedAt") LocalDateTime updatedAt);
    
    // 添加管理员服务所需的方法
    @Select("SELECT COUNT(*) FROM store")
    long count();
    
    /**
     * 根据条件查询店铺列表（带分页）
     */
    List<Store> findByConditions(
            @Param("status") String status, 
            @Param("keyword") String keyword,
            @Param("offset") int offset, 
            @Param("limit") int limit);
            
    /**
     * 统计符合条件的店铺数量
     */
    long countByConditions(
            @Param("status") String status, 
            @Param("keyword") String keyword);
    
    @Update("UPDATE store SET status = #{status}, updated_at = #{updatedAt} WHERE id = #{id}")
    int updateStoreStatus(@Param("id") Long id, @Param("status") Store.StoreStatus status, @Param("updatedAt") LocalDateTime updatedAt);

    /**
     * 根据状态和关键字查询店铺列表（支持PageHelper）
     */
    List<Store> findByStatusAndKeyword(
            @Param("status") Store.StoreStatus status,
            @Param("keyword") String keyword);
    
    /**
     * 保存商店信息（插入或更新）
     */
    int save(Store store);

    /**
     * 设置拒绝原因
     */
    @Update("UPDATE store SET reject_reason = #{reason}, updated_at = NOW() WHERE id = #{storeId}")
    int setRejectReason(@Param("storeId") Long storeId, @Param("reason") String reason);

    /**
     * 更新店铺评分
     */
    @Update("UPDATE store SET rating = #{rating}, updated_at = #{updatedAt} WHERE id = #{storeId}")
    int updateRating(@Param("storeId") Long storeId, @Param("rating") double rating, @Param("updatedAt") LocalDateTime updatedAt);

    List<Store> findByFilters(StoreQueryDTO queryDTO);

    @Select("SELECT merchant_id FROM store WHERE id = #{storeId}")
    Long findOwnerIdByStoreId(@Param("storeId") Long storeId);

    @Delete("delete from store where id=#{id}")
    void deleteById(@Param("id") Long pendingStoreId);
}