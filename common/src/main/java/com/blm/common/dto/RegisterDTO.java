package com.blm.common.dto;

import com.blm.common.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "用户注册数据传输对象")
public class RegisterDTO {
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty @NotBlank
    private String username;
    
    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "[a-zA-Z0-9_!@#$%^&*()]{6,20}", message = "密码必须是6-20位字母、数字或特殊字符")
    private String password;
    
    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;
    
    @Schema(description = "邮箱")
    private String email;
    
    @Schema(description = "角色", allowableValues = {"USER", "ADMIN", "RIDER", "MERCHANT"}, example = "USER")
    private User.UserRole role; // Optional, default to USER if not provided
}
