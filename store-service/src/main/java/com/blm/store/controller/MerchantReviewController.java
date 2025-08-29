package com.blm.store.controller;

import com.blm.common.result.Result;
import com.blm.store.service.MerchantReviewService;
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

@Tag(name = "商家API-商家评价管理", description = "商家查看和管理店铺收到的评价")
@RestController
@RequestMapping("/api/merchant/stores/{storeId}/reviews")
@SecurityRequirement(name = "bearerAuth")
public class MerchantReviewController {

    @Autowired
    private MerchantReviewService reviewService;

    // 定义一个内部类用于Swagger文档生成
    private static class PageVOSchema {
        @Schema(name = "ReviewVOPage")
        private static class ReviewVOPage extends PageVO<ReviewVO> {}
    }

    @Operation(summary = "获取店铺评价列表", description = "根据对商家和骑手的评分获取指定店铺的评价列表，支持分页和筛选")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageVOSchema.ReviewVOPage.class)))
    @ApiResponse(responseCode = "401", description = "未授权或Token无效")
    @ApiResponse(responseCode = "403", description = "无权限访问 (非商家用户)")
    @ApiResponse(responseCode = "404", description = "店铺不存在或不属于当前商家")
    @GetMapping
    public Result<PageVO<ReviewVO>> listReviews(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺ID", required = true) @PathVariable Long storeId,
            //由于数据库设计中订单评分分为商店评分和骑手评分，所以此处rating应当区分，使用storeRating
            @Parameter(description = "店铺_评分筛选 (1-5)") @RequestParam(defaultValue = "5") Integer storeRating,
            @Parameter(description = "页码 (从0开始)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        PageVO<ReviewVO> reviewPage = reviewService.listStoreReviews(userId, storeId, storeRating, page, size);
        return Result.success(reviewPage);
    }
}