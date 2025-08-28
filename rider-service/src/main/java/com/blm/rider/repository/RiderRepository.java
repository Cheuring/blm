package com.blm.rider.repository;

import com.blm.common.entity.Rider;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface RiderRepository {

    @Select("SELECT * FROM rider WHERE user_id = #{userId}")
    Optional<Rider> findByUserId(Long userId);

    @Insert("INSERT INTO rider(user_id, real_name, id_card, id_card_front, id_card_back, vehicle_type, vehicle_number, status, longitude, latitude, created_at, updated_at) " +
            "VALUES(#{userId}, #{realName}, #{idCard}, #{idCardFront}, #{idCardBack}, #{vehicleType}, #{vehicleNumber}, #{status}, #{longitude}, #{latitude}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Rider rider);

    @Update("UPDATE rider SET status = #{status}, updated_at = #{updatedAt} WHERE user_id = #{userId}")
    int updateWorkStatus(@Param("userId") Long userId, @Param("status") Rider.RiderStatus status, @Param("updatedAt") java.time.LocalDateTime updatedAt);

    @Update("UPDATE rider SET status = #{status}, updated_at = #{updatedAt} WHERE id = #{id}")
    int updateWorkStatusById(@Param("id") Long id, @Param("status") Integer status, @Param("updatedAt") java.time.LocalDateTime updatedAt);

    @Update("UPDATE rider SET longitude = #{longitude}, latitude = #{latitude}, updated_at = #{updatedAt} WHERE user_id = #{userId}")
    int updateLocation(@Param("userId") Long userId, @Param("longitude") java.math.BigDecimal longitude, @Param("latitude") java.math.BigDecimal latitude, @Param("updatedAt") java.time.LocalDateTime updatedAt);

    @Select("SELECT * FROM rider")
    List<Rider> findAll();

    List<Rider> findByStatusAndKeyword(
            @Param("status") Rider.RiderStatus status,
            @Param("keyword") String keyword);

    /**
     * 保存骑手信息（插入或更新）
     */
    int save(Rider rider);

    /**
     * 计数总数
     */
    @Select("SELECT COUNT(*) FROM rider")
    long count();

    /**
     * 根据ID查找骑手
     */
    @Select("SELECT * FROM rider WHERE id = #{id}")
    Optional<Rider> findById(Long id);
}