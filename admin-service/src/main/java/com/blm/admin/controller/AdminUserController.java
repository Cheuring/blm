package com.blm.admin.controller;

import com.blm.common.result.Result;
import com.blm.common.dto.AuditDTO;
import com.blm.common.entity.User;
import com.blm.admin.service.AdminService;
import com.blm.common.vo.PageVO;
import com.blm.common.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员用户管理控制器
 */
@Tag(name = "管理员API - 用户管理", description = "管理员管理用户账号")
@RestController
@RequestMapping("/api/admin/users")
@SecurityRequirement(name = "bearerAuth")
public class AdminUserController {

    @Autowired
    private AdminService adminService;

    /**
     * 获取用户列表 (分页)
     */
    @Operation(summary = "获取用户列表", description = "管理员获取用户列表，可按角色、状态和关键词过滤")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageUserVO.class)))
    @GetMapping
    public Result<PageVO<UserVO>> listUsers(
            @Parameter(description = "用户角色 (USER/MERCHANT/RIDER)") @RequestParam(required = false) User.UserRole role,
            @Parameter(description = "用户状态 (1/0)") @RequestParam(required = false) Integer status,
            @Parameter(description = "关键词 (用户名/邮箱/手机号)") @RequestParam(required = false) String keyword,
            @Parameter(description = "页码 (从1开始)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        
        PageVO<UserVO> users = adminService.listUsers(role, status, keyword, page, size);
        return Result.success(users);
    }

    /**
     * 更改用户状态 (启用/禁用)
     */
    @Operation(summary = "更改用户状态", description = "管理员启用或禁用用户账号")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PutMapping("/{id}/status")
    public Result<Void> updateUserStatus(
            @Parameter(description = "用户ID", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "状态更新信息", required = true, 
                content = @Content(schema = @Schema(implementation = AuditDTO.class)))
            @RequestBody AuditDTO dto) {
        
        adminService.updateUserStatus(id, dto);
        return Result.success(null);
    }
    
    // 用于Swagger文档
    private static class PageUserVO extends PageVO<UserVO> {}
}