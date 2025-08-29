package com.blm.admin.controller;

import com.blm.common.result.Result;
import com.blm.common.dto.AuditDTO;
import com.blm.common.entity.Food;
import com.blm.admin.service.AdminService;
import com.blm.common.vo.FoodVO;
import com.blm.common.vo.PageVO;
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
 * 管理员商品管理控制器
 */
@Tag(name = "管理员API - 商品管理", description = "管理员管理商品审核")
@RestController
@RequestMapping("/api/admin/foods")
@SecurityRequirement(name = "bearerAuth")
public class AdminFoodController {

    @Autowired
    private AdminService adminService;

    /**
     * 获取商品列表 (分页)
     */
    @Operation(summary = "获取商品列表", description = "管理员获取商品列表，可按状态、关键词和店铺过滤")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageFoodVO.class)))
    @GetMapping
    public Result<PageVO<FoodVO>> listFoods(
            @Parameter(description = "商品状态 (OFF_SHELF/ON_SHELF/SUSPENDED/PENDING)") @RequestParam(required = false) Food.FoodStatus status,
            @Parameter(description = "关键词 (商品名/描述)") @RequestParam(required = false) String keyword,
            @Parameter(description = "店铺ID") @RequestParam(required = false) Long storeId,
            @Parameter(description = "页码 (从1开始)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
                
        PageVO<FoodVO> foods = adminService.listFoods(status, keyword, storeId, page, size);
        return Result.success(foods);
    }

    /**
     * 审核/下架商品
     */
    @Operation(summary = "审核/下架商品", description = "管理员审核或下架商品")
    @ApiResponse(responseCode = "200", description = "操作成功")
    @PutMapping("/{id}/status")
    public Result<Void> auditFood(
            @Parameter(description = "商品ID", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "审核信息", required = true, 
                content = @Content(schema = @Schema(implementation = AuditDTO.class)))
            @RequestBody AuditDTO dto) {
        
        adminService.auditFood(id, dto);
        return Result.success(null);
    }

    // 用于Swagger文档
    private static class PageFoodVO extends PageVO<FoodVO> {}
}