package com.blm.admin.controller;

import com.blm.common.entity.Store;
import com.blm.common.result.Result;
import com.blm.common.dto.AuditDTO;
import com.blm.common.dto.StoreCategoryDTO;
import com.blm.admin.service.AdminService;
import com.blm.common.vo.PageVO;
import com.blm.common.vo.StoreCategoryVO;
import com.blm.common.vo.StoreVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员商家管理控制器
 */
@Tag(name = "管理员API - 商家管理", description = "管理员管理商家店铺")
@RestController
@RequestMapping("/api/admin/stores")
@SecurityRequirement(name = "bearerAuth")
public class AdminStoreController {

    @Autowired
    private AdminService adminService;

    /**
     * 获取商家店铺列表 (分页)
     */
    @Operation(summary = "获取商家列表", description = "管理员获取商家店铺列表，可按状态和关键词过滤")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageStoreVO.class)))
    @GetMapping
    public Result<PageVO<StoreVO>> listStores(
            @Parameter(description = "店铺状态 (PENDING/SUSPENDED/OPEN/CLOSED)") @RequestParam(required = false) Store.StoreStatus status,
            @Parameter(description = "关键词 (店铺名/位置名)") @RequestParam(required = false) String keyword,
            @Parameter(description = "页码 (从1开始)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {

        PageVO<StoreVO> stores = adminService.listStores(status, keyword, page, size);
        return Result.success(stores);
    }

    /**
     * 审核商家店铺 (通过/拒绝)
     */
    @Operation(summary = "审核商家店铺", description = "管理员审核商家店铺申请")
    @ApiResponse(responseCode = "200", description = "审核成功")
    @PutMapping("/{id}/status")
    public Result<Void> auditStore(
            @Parameter(description = "店铺ID", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "审核信息", required = true,
                    content = @Content(schema = @Schema(implementation = AuditDTO.class)))
            @RequestBody AuditDTO dto) {

        adminService.auditStore(id, dto);
        return Result.success(null);
    }

    //查询店铺种类
    @Operation(summary = "查询店铺种类", description = "管理员查询店铺种类")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageStoreCategoryVO.class)))
    @GetMapping("/category")
    public Result<PageVO<StoreCategoryVO>> listStoreCategory(
            @Parameter(description = "页码 (从1开始)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size
    ) {
        PageVO<StoreCategoryVO> storeCategories = adminService.listStoreCategory(page, size);
        return Result.success(storeCategories);
    }

    //新增店铺种类
    @Operation(summary = "新增店铺种类", description = "管理员新增店铺种类")
    @ApiResponse(responseCode = "200", description = "新增成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StoreCategoryVO.class)))
    @PostMapping("/category")
    public Result<StoreCategoryVO> addStoreCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "店铺分类信息", required = true,
                    content = @Content(schema = @Schema(implementation = StoreCategoryDTO.class)))
            @Valid @RequestBody StoreCategoryDTO dto
    ) {
        StoreCategoryVO category = adminService.addStoreCategory(dto);
        return Result.success(category);
    }

    //更新店铺种类
    @Operation(summary = "更新店铺种类", description = "管理员更新店铺种类")
    @ApiResponse(responseCode = "200", description = "更新成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StoreCategoryVO.class)))
    @PutMapping("/category/{id}")
    public Result<StoreCategoryVO> updateStoreCategory(
            @Parameter(description = "店铺分类ID", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "店铺分类信息", required = true,
                    content = @Content(schema = @Schema(implementation = StoreCategoryDTO.class)))
            @Valid @RequestBody StoreCategoryDTO dto
    ) {
        StoreCategoryVO category = adminService.updateStoreCategory(id, dto);
        return Result.success(category);
    }

    //删除店铺种类
    @Operation(summary = "删除店铺种类", description = "管理员删除店铺种类")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @DeleteMapping("/category/{id}")
    public Result<Void> deleteStoreCategory(
            @Parameter(description = "店铺分类ID", required = true) @PathVariable Long id
    ) {
        adminService.deleteStoreCategory(id);
        return Result.success(null);
    }

    // 用于Swagger文档
    private static class PageStoreVO extends PageVO<StoreVO> {
    }

    private static class PageStoreCategoryVO extends PageVO<StoreCategoryVO> {
    }
}