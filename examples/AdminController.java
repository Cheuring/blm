package com.blm.admin.controller;

import com.blm.common.annotation.RequireRole;
import com.blm.common.result.Result;
import com.blm.admin.dto.UserManageDTO;
import com.blm.admin.dto.SystemConfigDTO;
import com.blm.admin.service.AdminService;
import com.blm.admin.vo.StatisticsVO;
import com.blm.admin.vo.SystemConfigVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员控制器 - 权限控制示例
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
@Tag(name = "管理员管理", description = "管理员专用接口")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 获取平台统计数据 - 仅管理员和超级管理员
     */
    @Operation(summary = "获取平台统计", description = "查看平台整体运营数据")
    @GetMapping("/statistics")
    @RequireRole(value = {"ADMIN", "SUPER_ADMIN"}, logic = "OR", message = "需要管理员权限")
    public Result<StatisticsVO> getPlatformStatistics(@RequestHeader("X-User-Id") String userId,
                                                       @RequestParam(value = "startDate", required = false) String startDate,
                                                       @RequestParam(value = "endDate", required = false) String endDate) {
        StatisticsVO statistics = adminService.getPlatformStatistics(startDate, endDate);
        return Result.success(statistics);
    }

    /**
     * 用户管理 - 封禁/解封用户
     */
    @Operation(summary = "用户封禁管理", description = "封禁或解封用户账户")
    @PostMapping("/users/{targetUserId}/ban")
    @RequireRole(value = {"ADMIN", "SUPER_ADMIN"}, logic = "OR", message = "需要管理员权限")
    public Result<Void> banUser(@RequestHeader("X-User-Id") String userId,
                                @PathVariable("targetUserId") Long targetUserId,
                                @Validated @RequestBody UserManageDTO dto) {
        log.info("管理员 {} 对用户 {} 执行封禁操作", userId, targetUserId);
        adminService.banUser(Long.valueOf(userId), targetUserId, dto);
        return Result.success("用户状态更新成功");
    }

    /**
     * 商家审核 - 审核商家入驻申请
     */
    @Operation(summary = "商家审核", description = "审核商家入驻申请")
    @PostMapping("/merchants/{merchantId}/review")
    @RequireRole(value = {"ADMIN", "SUPER_ADMIN"}, logic = "OR", message = "需要管理员权限")
    public Result<Void> reviewMerchant(@RequestHeader("X-User-Id") String userId,
                                       @PathVariable("merchantId") Long merchantId,
                                       @RequestParam("approved") Boolean approved,
                                       @RequestParam(value = "reason", required = false) String reason) {
        log.info("管理员 {} 审核商家 {}, 结果: {}", userId, merchantId, approved);
        adminService.reviewMerchant(Long.valueOf(userId), merchantId, approved, reason);
        return Result.success(approved ? "商家申请通过" : "商家申请拒绝");
    }

    /**
     * 系统配置管理 - 仅超级管理员
     */
    @Operation(summary = "更新系统配置", description = "更新系统配置参数")
    @PostMapping("/system/config")
    @RequireRole(value = {"SUPER_ADMIN"}, message = "需要超级管理员权限")
    public Result<Void> updateSystemConfig(@RequestHeader("X-User-Id") String userId,
                                           @Validated @RequestBody SystemConfigDTO dto) {
        log.info("超级管理员 {} 更新系统配置", userId);
        adminService.updateSystemConfig(Long.valueOf(userId), dto);
        return Result.success("系统配置更新成功");
    }

    /**
     * 获取系统配置 - 仅管理员
     */
    @Operation(summary = "获取系统配置", description = "查看当前系统配置")
    @GetMapping("/system/config")
    @RequireRole(value = {"ADMIN", "SUPER_ADMIN"}, logic = "OR", message = "需要管理员权限")
    public Result<SystemConfigVO> getSystemConfig() {
        SystemConfigVO config = adminService.getSystemConfig();
        return Result.success(config);
    }

    /**
     * 权限管理 - 给用户分配角色 (仅超级管理员)
     */
    @Operation(summary = "分配用户角色", description = "给用户分配或撤销角色")
    @PostMapping("/users/{targetUserId}/roles")
    @RequireRole(value = {"SUPER_ADMIN"}, message = "需要超级管理员权限")
    public Result<Void> assignUserRole(@RequestHeader("X-User-Id") String userId,
                                       @PathVariable("targetUserId") Long targetUserId,
                                       @RequestParam("roles") String roles,
                                       @RequestParam("action") String action) { // ADD or REMOVE
        log.info("超级管理员 {} 对用户 {} 执行角色 {} 操作: {}", userId, targetUserId, action, roles);
        adminService.manageUserRoles(Long.valueOf(userId), targetUserId, roles, action);
        return Result.success("用户角色更新成功");
    }

    /**
     * 查看所有管理员 - 仅超级管理员
     */
    @Operation(summary = "查看所有管理员", description = "查看平台所有管理员列表")
    @GetMapping("/admins")
    @RequireRole(value = {"SUPER_ADMIN"}, message = "需要超级管理员权限")
    public Result<List<UserVO>> getAllAdmins(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                              @RequestParam(value = "size", defaultValue = "20") Integer size) {
        List<UserVO> admins = adminService.getAllAdmins(page, size);
        return Result.success(admins);
    }

    /**
     * 创建管理员账户 - 仅超级管理员
     */
    @Operation(summary = "创建管理员", description = "创建新的管理员账户")
    @PostMapping("/admins")
    @RequireRole(value = {"SUPER_ADMIN"}, message = "需要超级管理员权限")
    public Result<Void> createAdmin(@RequestHeader("X-User-Id") String userId,
                                    @Validated @RequestBody CreateAdminDTO dto) {
        log.info("超级管理员 {} 创建新管理员账户", userId);
        adminService.createAdmin(Long.valueOf(userId), dto);
        return Result.success("管理员账户创建成功");
    }

    /**
     * 平台数据导出 - 仅超级管理员
     */
    @Operation(summary = "数据导出", description = "导出平台数据")
    @PostMapping("/export")
    @RequireRole(value = {"SUPER_ADMIN"}, message = "需要超级管理员权限")
    public Result<String> exportData(@RequestHeader("X-User-Id") String userId,
                                     @RequestParam("type") String dataType,
                                     @RequestParam(value = "startDate", required = false) String startDate,
                                     @RequestParam(value = "endDate", required = false) String endDate) {
        log.info("超级管理员 {} 导出数据类型: {}", userId, dataType);
        String downloadUrl = adminService.exportData(Long.valueOf(userId), dataType, startDate, endDate);
        return Result.success(downloadUrl, "数据导出成功");
    }

    /**
     * 系统日志查看 - 仅超级管理员
     */
    @Operation(summary = "查看系统日志", description = "查看系统操作日志")
    @GetMapping("/logs")
    @RequireRole(value = {"SUPER_ADMIN"}, message = "需要超级管理员权限")
    public Result<List<SystemLogVO>> getSystemLogs(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                    @RequestParam(value = "size", defaultValue = "50") Integer size,
                                                    @RequestParam(value = "level", required = false) String level,
                                                    @RequestParam(value = "module", required = false) String module) {
        List<SystemLogVO> logs = adminService.getSystemLogs(page, size, level, module);
        return Result.success(logs);
    }
}
