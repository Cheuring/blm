package com.blm.user.repository;

import com.blm.common.entity.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问层
 */
@Mapper
public interface UserRepository {

    @Select("SELECT * FROM user WHERE id = #{id}")
    Optional<User> findById(@Param("id") Long id);

    @Select("SELECT * FROM user WHERE username = #{username}")
    Optional<User> findByUsername(@Param("username") String username);

    @Update("UPDATE user SET password=#{password}, updated_at=#{updatedAt} WHERE id=#{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password, @Param("updatedAt") LocalDateTime updatedAt);

    @Select("SELECT COUNT(*) FROM user WHERE role = #{role}")
    long countByRole(User.UserRole role);

    @Select("SELECT COUNT(*) FROM user")
    long count();

    @Select("SELECT COUNT(*) FROM user WHERE created_at BETWEEN #{start} AND #{end}")
    long countByCreatedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    /**
     * 根据条件查询用户列表（支持PageHelper）
     */
    List<User> findByConditions(
            @Param("role") User.UserRole role,
            @Param("status") Integer status,
            @Param("keyword") String keyword);

    /**
     * 查询所有用户
     */
    @Select("SELECT * FROM user")
    List<User> findAll();

    /**
     * 更新用户状态
     */
    @Update("UPDATE user SET status = #{status}, updated_at = #{updatedAt} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("updatedAt") LocalDateTime updatedAt);

    /**
     * 更新用户角色
     */
    @Update("UPDATE user SET role = #{role}, updated_at = #{updatedAt} WHERE id = #{id}")
    int updateRole(@Param("id") Long id, @Param("role") String role, @Param("updatedAt") LocalDateTime updatedAt);

    /**
     * 检查用户名是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM user WHERE username = #{username}")
    boolean existsByUsername(String username);

    /**
     * 检查手机号是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM user WHERE phone = #{phone}")
    boolean existsByPhone(String phone);

    /**
     * 保存用户（插入或更新）
     */
    int save(User user);

    @Select("SELECT id FROM user WHERE username = #{username}")
    Long getUserIdByUsername(@Param("username") String username);

    @Delete("DELETE FROM user WHERE id = #{userId}")
    void deleteById(@Param("userId") Long userId);

    void deleteBatchById(List<Long> userIds);
}
