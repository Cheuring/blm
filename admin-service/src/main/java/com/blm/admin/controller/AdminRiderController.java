package com.blm.admin.controller;

import com.blm.common.result.Result;
import com.blm.common.dto.AuditDTO;
import com.blm.common.entity.Rider;
import com.blm.admin.service.AdminService;
import com.blm.common.vo.PageVO;
import com.blm.common.vo.RiderVO;
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
 * 管理员骑手管理控制器
 */
@Tag(name = "管理员API - 骑手管理", description = "管理员管理骑手账号")
@RestController
@RequestMapping("/api/admin/riders")
@SecurityRequirement(name = "bearerAuth")
public class AdminRiderController {

    @Autowired
    private AdminService adminService;

    /**
     * 获取骑手列表 (分页)
     */
    @Operation(summary = "获取骑手列表", description = "管理员获取骑手列表，可按状态和关键词过滤")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageRiderVO.class)))
    @GetMapping
    public Result<PageVO<RiderVO>> listRiders(
            @Parameter(description = "骑手状态") @RequestParam(required = false) Rider.RiderStatus status,
            @Parameter(description = "关键词 (姓名)") @RequestParam(required = false) String keyword,
            @Parameter(description = "页码 (从1开始)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        
        PageVO<RiderVO> riders = adminService.listRiders(status, keyword, page, size);
        return Result.success(riders);
    }

    /**
     * 更改骑手状态 (启用/禁用)
     */
    @Operation(summary = "更改骑手状态", description = "管理员启用或禁用骑手账号")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PutMapping("/{id}/status")
    public Result<Void> updateRiderStatus(
            @Parameter(description = "骑手ID", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "状态更新信息", required = true, 
                content = @Content(schema = @Schema(implementation = AuditDTO.class)))
            @RequestBody AuditDTO dto) {
        
        adminService.updateRiderStatus(id, dto);
        return Result.success(null);
    }
    
    // 用于Swagger文档
    private static class PageRiderVO extends PageVO<RiderVO> {}
}