package com.blm.admin.controller;

import com.blm.common.result.Result;
import com.blm.admin.service.AdminService;
import com.blm.common.vo.PageVO;
import com.blm.common.vo.ReviewVO;
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
 * 管理员评价管理控制器
 */
@Tag(name = "管理员API - 评价管理", description = "管理员管理用户评价")
@RestController
@RequestMapping("/api/admin/reviews")
@SecurityRequirement(name = "bearerAuth")
public class AdminReviewController {

    @Autowired
    private AdminService adminService;

    /**
     * 获取评价列表 (分页)
     */
    @Operation(summary = "获取评价列表", description = "管理员获取所有评价，可按用户ID、商家ID等过滤")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageReviewVO.class)))
    @GetMapping
    public Result<PageVO<ReviewVO>> listReviews(
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "店铺ID") @RequestParam(required = false) Long storeId,
            @Parameter(description = "商品ID") @RequestParam(required = false) Long foodId,
            @Parameter(description = "评分") @RequestParam(required = false) Integer rating,
            @Parameter(description = "页码 (从1开始)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        
        PageVO<ReviewVO> reviews = adminService.listReviews(userId, storeId, foodId, rating, page, size);
        return Result.success(reviews);
    }

    /**
     * 删除评价
     */
    @Operation(summary = "删除评价", description = "管理员删除违规的用户评价")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @DeleteMapping("/{id}")
    public Result<Void> deleteReview(
            @Parameter(description = "评价ID", required = true) @PathVariable Long id) {
        
        adminService.deleteReview(id);
        return Result.success(null);
    }

    // 用于Swagger文档
    private static class PageReviewVO extends PageVO<ReviewVO> {}
}