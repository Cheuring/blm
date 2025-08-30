package com.blm.user.repository;

import com.blm.common.entity.UserAddress;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserAddressRepository {

    @Select("SELECT * FROM user_address WHERE user_id = #{userId} ORDER BY is_default DESC, created_at DESC")
    List<UserAddress> findAllByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM user_address WHERE id = #{id} AND user_id = #{userId}")
    Optional<UserAddress> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Insert("INSERT INTO user_address(user_id, receiver, phone, province, city, district, detail_address, is_default, created_at, updated_at) " +
            "VALUES(#{userId}, #{receiver}, #{phone}, #{province}, #{city}, #{district}, #{detailAddress}, #{isDefault}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserAddress address);

    @Update("UPDATE user_address SET receiver=#{receiver}, phone=#{phone}, province=#{province}, city=#{city}, " +
            "district=#{district}, detail_address=#{detailAddress}, is_default=#{isDefault}, updated_at=#{updatedAt} " +
            "WHERE id=#{id} AND user_id=#{userId}")
    int update(UserAddress address);

    @Delete("DELETE FROM user_address WHERE id = #{id} AND user_id = #{userId}")
    int delete(@Param("id") Long id, @Param("userId") Long userId);

    @Update("UPDATE user_address SET is_default = 0 WHERE user_id = #{userId}")
    int resetDefault(@Param("userId") Long userId);

    @Update("UPDATE user_address SET is_default = 1 WHERE id = #{id} AND user_id = #{userId}")
    int setDefault(@Param("id") Long id, @Param("userId") Long userId);

    @Select("SELECT * FROM user_address WHERE id = #{id}")
    Optional<UserAddress> findById(@Param("id") Long id);
}