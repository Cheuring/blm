package com.blm.common.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 用户实体类
 */
@Data
public class User {
    public static final Integer ACTIVE = 1;
    public static final Integer INACTIVE = 0;

    public enum UserRole {
        USER("普通用户"),
        MERCHANT("商家"),
        RIDER("骑手"),
        ADMIN("管理员");

        private final String description;

        UserRole(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private Long id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String fullName;
    private String avatar;
    private String role; // USER, MERCHANT, RIDER, ADMIN 多个身份以 ',' 分隔
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 获取用户角色列表
     */
    public List<String> getRoles() {
        if (role == null || role.isEmpty()) {
            return Arrays.asList("USER");
        }
        return Arrays.asList(role.split(","));
    }

    /**
     * 检查用户是否拥有指定角色
     */
    public boolean hasRole(String targetRole) {
        return getRoles().contains(targetRole);
    }

    /**
     * 检查用户是否拥有指定角色
     */
    public boolean hasRole(UserRole targetRole) {
        return hasRole(targetRole.name());
    }
}
