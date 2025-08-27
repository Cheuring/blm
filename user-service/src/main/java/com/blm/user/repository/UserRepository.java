package com.blm.user.repository;

import com.blm.common.entity.User;
import org.apache.ibatis.annotations.*;

/**
 * 用户数据访问层
 */
@Mapper
public interface UserRepository {

    /**
     * 根据ID查找用户
     */
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);

    /**
     * 根据用户名查找用户
     */
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    @Select("SELECT * FROM user WHERE email = #{email}")
    User findByEmail(String email);

    /**
     * 保存用户
     */
    @Insert("INSERT INTO user(username, password, phone, email, full_name, role, status, created_at, updated_at) " +
            "VALUES(#{username}, #{password}, #{phone}, #{email}, #{fullName}, #{role}, #{status}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(User user);

    /**
     * 更新用户信息
     */
    @Update("UPDATE user SET username = #{username}, phone = #{phone}, email = #{email}, " +
            "full_name = #{fullName}, updated_at = #{updatedAt} WHERE id = #{id}")
    int update(User user);

    /**
     * 更新用户密码
     */
    @Update("UPDATE user SET password = #{password}, updated_at = NOW() WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);
}
