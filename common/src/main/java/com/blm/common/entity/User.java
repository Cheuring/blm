package com.blm.common.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    public static final int ACTIVE = 1;
    public static final int INACTIVE = 0;

    public enum UserRole {
        USER,
        MERCHANT,
        RIDER,
        ADMIN,
    }

    private Long id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String avatar;
    private String role; // USER, MERCHANT, RIDER, ADMIN 多个身份以 ',' 分隔
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}