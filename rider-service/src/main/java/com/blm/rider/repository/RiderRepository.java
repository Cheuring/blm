package com.blm.rider.repository;

import com.blm.common.entity.Rider;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 骑手数据访问层
 */
@Mapper
public interface RiderRepository {

    @Insert("INSERT INTO rider(user_id, real_name, id_card, id_card_front, id_card_back, " +
            "vehicle_type, vehicle_number, status, longitude, latitude, created_at, updated_at) " +
            "VALUES(#{userId}, #{realName}, #{idCard}, #{idCardFront}, #{idCardBack}, " +
            "#{vehicleType}, #{vehicleNumber}, #{status}, #{longitude}, #{latitude}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Rider rider);

    @Select("SELECT * FROM rider WHERE id = #{id}")
    Optional<Rider> findById(Long id);

    @Select("SELECT * FROM rider WHERE user_id = #{userId}")
    Optional<Rider> findByUserId(Long userId);

    @Update("UPDATE rider SET status = #{status}, updated_at = #{updatedAt} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, 
                    @Param("status") Rider.RiderStatus status, 
                    @Param("updatedAt") LocalDateTime updatedAt);

    @Update("UPDATE rider SET longitude = #{longitude}, latitude = #{latitude}, updated_at = #{updatedAt} WHERE id = #{id}")
    int updateLocation(@Param("id") Long id, 
                      @Param("longitude") BigDecimal longitude, 
                      @Param("latitude") BigDecimal latitude, 
                      @Param("updatedAt") LocalDateTime updatedAt);

    @Select("SELECT * FROM rider WHERE status = #{status}")
    List<Rider> findByStatus(@Param("status") Rider.RiderStatus status);

    @Select("SELECT COUNT(*) FROM rider")
    long count();

    @Select("SELECT * FROM rider")
    List<Rider> findAll();
}
