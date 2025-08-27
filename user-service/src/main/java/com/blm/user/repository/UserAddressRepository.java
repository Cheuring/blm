package com.blm.user.repository;

import com.blm.common.entity.UserAddress;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户地址数据访问层
 */
@Mapper
public interface UserAddressRepository {

    /**
     * 根据用户ID查找地址列表
     */
    @Select("SELECT * FROM user_address WHERE user_id = #{userId} ORDER BY is_default DESC, created_at DESC")
    List<UserAddress> findByUserId(Long userId);

    /**
     * 根据ID查找地址
     */
    @Select("SELECT * FROM user_address WHERE id = #{id}")
    UserAddress findById(Long id);

    /**
     * 插入地址
     */
    @Insert("INSERT INTO user_address(user_id, receiver, phone, province, city, district, detail_address, is_default, created_at, updated_at) " +
            "VALUES(#{userId}, #{receiver}, #{phone}, #{province}, #{city}, #{district}, #{detailAddress}, #{isDefault}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserAddress address);

    /**
     * 更新地址
     */
    @Update("UPDATE user_address SET receiver = #{receiver}, phone = #{phone}, province = #{province}, " +
            "city = #{city}, district = #{district}, detail_address = #{detailAddress}, " +
            "is_default = #{isDefault}, updated_at = #{updatedAt} WHERE id = #{id}")
    int update(UserAddress address);

    /**
     * 删除地址
     */
    @Delete("DELETE FROM user_address WHERE id = #{id}")
    int deleteById(Long id);

    /**
     * 取消用户的所有默认地址
     */
    @Update("UPDATE user_address SET is_default = 0, updated_at = NOW() WHERE user_id = #{userId} AND is_default = 1")
    int cancelDefaultByUserId(Long userId);

    /**
     * 设置地址为默认
     */
    @Update("UPDATE user_address SET is_default = 1, updated_at = NOW() WHERE id = #{id}")
    int setDefault(Long id);
}
