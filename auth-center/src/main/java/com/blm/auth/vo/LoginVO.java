package com.blm.auth.vo;

import com.blm.common.vo.UserVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 登录响应VO
 */
@Data
@Schema(description = "登录响应VO")
public class LoginVO {

    @Schema(description = "访问Token")
    private String accessToken;

    @Schema(description = "刷新Token")
    private String refreshToken;

    @Schema(description = "用户信息")
    private UserVO user;
}
