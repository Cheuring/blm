package com.blm.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "用户基本信息视图对象") // Added description
public class UserVO {
    @Schema(description = "用户ID")
    private Long id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "头像URL")
    private String avatar;
    @Schema(description = "角色", example = "USER")
    private String role;
    @Schema(description = "状态: 0-禁用, 1-正常")
    private Integer status;
    @Schema(description = "注册时间")
    private LocalDateTime createdAt;
}