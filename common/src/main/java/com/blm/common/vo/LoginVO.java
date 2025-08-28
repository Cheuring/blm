package com.blm.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户登录响应视图对象")
public class LoginVO {
    @Schema(description = "认证令牌 (JWT)")
    private String token;

    @Schema(description = "登录用户信息")
    private UserVO userInfo;
}