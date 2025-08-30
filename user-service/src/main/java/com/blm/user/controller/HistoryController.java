package com.blm.user.controller;

import com.blm.common.annotation.RequireRole;
import com.blm.common.dto.HistoryDTO;
import com.blm.common.result.Result;
import com.blm.common.vo.FoodVO;
import com.blm.common.vo.PageVO;
import com.blm.common.vo.StoreVO;
import com.blm.user.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "收藏与历史", description = "用户管理浏览历史")
@RestController
@RequestMapping("/api/history")
//@RequireRole("USER")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    // Helper schema
    private static class PageVOSchema {
        @Schema(name = "StoreVOPage")
        private static class StoreVOPage extends PageVO<StoreVO> {
        }

        @Schema(name = "FoodVOPage")
        private static class FoodVOPage extends PageVO<FoodVO> {
        }
    }

    @Operation(summary = "删除历史", description = "删除指定历史")
    @ApiResponse(responseCode = "200", description = "移除成功")
    @DeleteMapping("/{historyId}")
    public Result<Void> removeFoodHistory(@RequestHeader("X-User-Id") Long userId,
                                          @Parameter(description = "要移除历史ID", required = true) @PathVariable Long historyId) {
        historyService.removeHistory(userId, historyId);
        return Result.success(null);
    }
    // --- Store History --- 

    @Operation(summary = "获取历史店铺列表", description = "查询当前登录用户历史所有店铺，支持分页")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageVOSchema.StoreVOPage.class)))
    @GetMapping("/stores")
    public Result<PageVO<StoreVO>> listHistoryStores(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "页码 (从1开始)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        PageVO<StoreVO> list = historyService.listStoresHistory(userId, page, size);
        return Result.success(list);
    }

    @Operation(summary = "添加店铺历史", description = "将指定店铺添加到当前用户的历史")
    @ApiResponse(responseCode = "200", description = "添加成功")
    @PostMapping("/stores")
    public Result<Void> addStoreHistory(@RequestHeader("X-User-Id") Long userId,
                                        @RequestBody(description = "要添加的店铺信息", required = true, content = @Content(schema = @Schema(implementation = HistoryDTO.class))) @Valid @org.springframework.web.bind.annotation.RequestBody HistoryDTO dto) {
        historyService.addStoreHistory(userId, dto.getTargetId());
        return Result.success(null);
    }

    // --- Food History ---

    @Operation(summary = "获取历史的商品列表", description = "查询当前登录用户历史的所有商品")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageVOSchema.FoodVOPage.class)))
    @GetMapping("/foods")
    public Result<PageVO<FoodVO>> listHistoryFoods(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "页码 (从1开始)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        PageVO<FoodVO> list = historyService.listFoodsHistory(userId, page, size);
        return Result.success(list);
    }

    @Operation(summary = "添加商品历史", description = "将指定商品添加到当前用户的历史")
    @ApiResponse(responseCode = "200", description = "添加成功")
    @PostMapping("/foods")
    public Result<Void> addFoodHistory(@RequestHeader("X-User-Id") Long userId,
                                       @RequestBody(description = "要添加的商品信息", required = true, content = @Content(schema = @Schema(implementation = HistoryDTO.class))) @Valid @org.springframework.web.bind.annotation.RequestBody HistoryDTO dto) {
        historyService.addFoodHistory(userId, dto.getTargetId());
        return Result.success(null);
    }
}