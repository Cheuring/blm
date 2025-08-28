package com.blm.user.controller;

import com.blm.common.dto.PasswordUpdateDTO;
import com.blm.common.dto.UserProfileUpdateDTO;
import com.blm.common.result.Result;
import com.blm.common.vo.UserVO;
import com.blm.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户信息管理")
@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "获取当前用户个人资料", description = "获取当前登录用户的详细信息")
    @ApiResponse(responseCode = "200", description = "获取成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserVO.class)))
    @GetMapping("/profile")
    public Result<UserVO> getProfile(@RequestHeader("X-User-Id") String userId) {
        UserVO vo = userService.getUserProfile(userId);
        return Result.success(vo);
    }

    @Operation(summary = "更新当前用户个人资料", description = "更新当前登录用户的用户名、邮箱或头像")
    @ApiResponse(responseCode = "200", description = "更新成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserVO.class)))
    @PutMapping("/profile")
    public Result<UserVO> updateProfile(@RequestHeader("X-User-Id") String userId,
                                        @RequestBody(description = "要更新的用户信息", required = true, content = @Content(schema = @Schema(implementation = UserProfileUpdateDTO.class))) @org.springframework.web.bind.annotation.RequestBody UserProfileUpdateDTO dto) {
        UserVO vo = userService.updateUserProfile(userId, dto);
        return Result.success(vo);
    }

    @Operation(summary = "更新当前用户密码", description = "更新当前登录用户的密码")
    @ApiResponse(responseCode = "200", description = "密码更新成功")
    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestHeader("X-User-Id") String userId,
                                       @RequestBody(description = "密码更新信息", required = true, content = @Content(schema = @Schema(implementation = PasswordUpdateDTO.class))) @org.springframework.web.bind.annotation.RequestBody PasswordUpdateDTO dto) {
        userService.updateUserPassword(userId, dto);
        return Result.success(null);
    }

    // todo: file service
//    @Operation(summary = "上传用户头像", description = "上传当前登录用户的头像")
//    @ApiResponse(responseCode = "200", description = "头像上传成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
//    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public Result<String> upload(@RequestParam MultipartFile file) {
//        Long userId = securityUtil.getCurrentUserId();
//        return Result.success(minioUtil.uploadImages(userId, file, MinioUtil.ImageType.AVATAR));
//    }
//
//    @Operation(summary = "删除url对应的图像")
//    @ApiResponse(responseCode = "200", description = "删除成功")
//    @DeleteMapping("/avatar")
//    public Result<Void> deleteAvatar(
//            @RequestBody(description = "删除的文件对应的url,不包含endpoint 例如：blm-images/preview/avatar/1234567890.jpg",
//                    required = true,
//                    content = @Content(schema = @Schema(implementation = FileDeleteDTO.class)))
//            @org.springframework.web.bind.annotation.RequestBody FileDeleteDTO dto) {
//        Long userId = securityUtil.getCurrentUserId();
//        minioUtil.deleteUrlImage(userId, dto.getUrl());
//        return Result.success(null);
//    }
}