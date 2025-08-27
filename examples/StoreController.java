package com.blm.store.controller;

import com.blm.common.annotation.RequireRole;
import com.blm.common.result.Result;
import com.blm.store.dto.StoreDTO;
import com.blm.store.service.StoreService;
import com.blm.store.vo.StoreVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商家店铺控制器 - 权限控制示例
 */
@Slf4j
@RestController
@RequestMapping("/api/stores")
@Tag(name = "商家店铺管理", description = "商家店铺相关接口")
public class StoreController {

    @Autowired
    private StoreService storeService;

    /**
     * 创建店铺 - 需要商家角色
     */
    @Operation(summary = "创建店铺", description = "商家创建新店铺")
    @PostMapping
    @RequireRole(value = {"MERCHANT"}, message = "只有商家才能创建店铺")
    public Result<StoreVO> createStore(@RequestHeader("X-User-Id") String userId,
                                       @Validated @RequestBody StoreDTO dto) {
        log.info("商家 {} 创建店铺", userId);
        StoreVO storeVO = storeService.createStore(Long.valueOf(userId), dto);
        return Result.success("店铺创建成功", storeVO);
    }

    /**
     * 获取店铺信息 - 公开接口，无需权限
     */
    @Operation(summary = "获取店铺信息", description = "获取店铺详细信息")
    @GetMapping("/{storeId}")
    public Result<StoreVO> getStore(@PathVariable("storeId") Long storeId) {
        StoreVO storeVO = storeService.getStoreById(storeId);
        return Result.success(storeVO);
    }

    /**
     * 更新店铺信息 - 需要商家角色且是店铺所有者
     */
    @Operation(summary = "更新店铺信息", description = "商家更新自己的店铺信息")
    @PutMapping("/{storeId}")
    @RequireRole(value = {"MERCHANT"}, message = "只有商家才能更新店铺")
    public Result<StoreVO> updateStore(@RequestHeader("X-User-Id") String userId,
                                       @PathVariable("storeId") Long storeId,
                                       @Validated @RequestBody StoreDTO dto) {
        log.info("商家 {} 更新店铺 {}", userId, storeId);
        StoreVO storeVO = storeService.updateStore(Long.valueOf(userId), storeId, dto);
        return Result.success("店铺更新成功", storeVO);
    }

    /**
     * 获取我的店铺列表 - 需要商家角色
     */
    @Operation(summary = "获取我的店铺", description = "获取当前商家的所有店铺")
    @GetMapping("/my")
    @RequireRole(value = {"MERCHANT"}, message = "只有商家才能查看自己的店铺")
    public Result<List<StoreVO>> getMyStores(@RequestHeader("X-User-Id") String userId) {
        List<StoreVO> stores = storeService.getStoresByMerchantId(Long.valueOf(userId));
        return Result.success(stores);
    }

    /**
     * 更改店铺状态 - 需要管理员或商家角色
     */
    @Operation(summary = "更改店铺状态", description = "管理员或商家更改店铺营业状态")
    @PutMapping("/{storeId}/status")
    @RequireRole(value = {"ADMIN", "MERCHANT"}, requireAll = false, message = "需要管理员或商家权限")
    public Result<Void> updateStoreStatus(@RequestHeader("X-User-Id") String userId,
                                          @RequestHeader("X-User-Roles") String userRoles,
                                          @PathVariable("storeId") Long storeId,
                                          @RequestParam("status") Integer status) {
        log.info("用户 {} (角色: {}) 更改店铺 {} 状态为 {}", userId, userRoles, storeId, status);
        storeService.updateStoreStatus(Long.valueOf(userId), storeId, status, userRoles);
        return Result.success("店铺状态更新成功");
    }

    /**
     * 审核店铺 - 仅管理员
     */
    @Operation(summary = "审核店铺", description = "管理员审核店铺申请")
    @PostMapping("/{storeId}/approve")
    @RequireRole(value = {"ADMIN"}, message = "只有管理员才能审核店铺")
    public Result<Void> approveStore(@RequestHeader("X-User-Id") String userId,
                                     @PathVariable("storeId") Long storeId,
                                     @RequestParam("approved") Boolean approved,
                                     @RequestParam(value = "reason", required = false) String reason) {
        log.info("管理员 {} 审核店铺 {}, 结果: {}", userId, storeId, approved);
        storeService.approveStore(Long.valueOf(userId), storeId, approved, reason);
        return Result.success(approved ? "店铺审核通过" : "店铺审核拒绝");
    }
}
