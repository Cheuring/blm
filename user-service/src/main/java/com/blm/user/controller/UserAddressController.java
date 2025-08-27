package com.blm.user.controller;

import com.blm.common.result.Result;
import com.blm.common.vo.UserAddressVO;
import com.blm.user.dto.UserAddressDTO;
import com.blm.user.service.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户地址控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@Tag(name = "用户地址管理", description = "用户地址相关接口")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    /**
     * 获取用户地址列表
     */
    @Operation(summary = "获取地址列表", description = "获取用户的所有地址")
    @GetMapping("/{userId}/addresses")
    public Result<List<UserAddressVO>> getUserAddresses(@PathVariable("userId") Long userId) {
        List<UserAddressVO> addresses = userAddressService.getUserAddresses(userId);
        return Result.success(addresses);
    }

    /**
     * 获取当前用户地址列表
     */
    @Operation(summary = "获取当前用户地址列表", description = "获取当前登录用户的所有地址")
    @GetMapping("/addresses")
    public Result<List<UserAddressVO>> getAddresses(@RequestHeader("X-User-Id") String userId) {
        List<UserAddressVO> addresses = userAddressService.getUserAddresses(Long.valueOf(userId));
        return Result.success(addresses);
    }

    /**
     * 根据地址ID获取地址信息
     */
    @Operation(summary = "获取地址详情", description = "根据地址ID获取地址详细信息")
    @GetMapping("/addresses/{addressId}")
    public Result<UserAddressVO> getAddressById(@PathVariable("addressId") Long addressId) {
        UserAddressVO address = userAddressService.getAddressById(addressId);
        return Result.success(address);
    }

    /**
     * 添加地址
     */
    @Operation(summary = "添加地址", description = "为当前用户添加新地址")
    @PostMapping("/addresses")
    public Result<UserAddressVO> addAddress(@RequestHeader("X-User-Id") String userId,
                                            @Validated @RequestBody UserAddressDTO dto) {
        UserAddressVO address = userAddressService.addAddress(Long.valueOf(userId), dto);
        return Result.success("地址添加成功", address);
    }

    /**
     * 更新地址
     */
    @Operation(summary = "更新地址", description = "更新用户地址信息")
    @PutMapping("/addresses/{addressId}")
    public Result<UserAddressVO> updateAddress(@RequestHeader("X-User-Id") String userId,
                                               @PathVariable("addressId") Long addressId,
                                               @Validated @RequestBody UserAddressDTO dto) {
        UserAddressVO address = userAddressService.updateAddress(Long.valueOf(userId), addressId, dto);
        return Result.success("地址更新成功", address);
    }

    /**
     * 删除地址
     */
    @Operation(summary = "删除地址", description = "删除用户地址")
    @DeleteMapping("/addresses/{addressId}")
    public Result<Void> deleteAddress(@RequestHeader("X-User-Id") String userId,
                                      @PathVariable("addressId") Long addressId) {
        userAddressService.deleteAddress(Long.valueOf(userId), addressId);
        return Result.success(null);
    }

    /**
     * 设置默认地址
     */
    @Operation(summary = "设置默认地址", description = "设置用户的默认收货地址")
    @PostMapping("/addresses/{addressId}/default")
    public Result<Void> setDefaultAddress(@RequestHeader("X-User-Id") String userId,
                                          @PathVariable("addressId") Long addressId) {
        userAddressService.setDefaultAddress(Long.valueOf(userId), addressId);
        return Result.success(null);
    }
}
