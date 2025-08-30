package com.blm.user.controller;

import com.blm.common.annotation.RequireRole;
import com.blm.common.dto.UserAddressDTO;
import com.blm.common.result.Result;
import com.blm.common.vo.UserAddressVO;
import com.blm.user.service.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户地址管理", description = "管理用户的收货地址")
@RestController
@RequestMapping("/api/addresses")
//@RequireRole("USER")
public class UserAddressController {

    @Autowired
    private UserAddressService addressService;

    @Operation(summary = "获取当前用户地址列表", description = "查询当前登录用户的所有收货地址")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = UserAddressVO.class)))
    @GetMapping
    public Result<List<UserAddressVO>> list(@RequestHeader("X-User-Id") Long userId) {
        List<UserAddressVO> list = addressService.listAddresses(userId);
        return Result.success(list);
    }

    @Operation(summary = "添加新地址", description = "为当前登录用户添加一个新的收货地址")
    @ApiResponse(responseCode = "200", description = "添加成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAddressVO.class)))
    @PostMapping
    public Result<UserAddressVO> add(@RequestHeader("X-User-Id") Long userId,
                                     @RequestBody(description = "新地址信息", required = true, content = @Content(schema = @Schema(implementation = UserAddressDTO.class))) @org.springframework.web.bind.annotation.RequestBody UserAddressDTO dto) {
        UserAddressVO vo = addressService.addAddress(userId, dto);
        return Result.success(vo);
    }

    @Operation(summary = "更新地址信息", description = "更新指定ID的收货地址信息")
    @ApiResponse(responseCode = "200", description = "更新成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAddressVO.class)))
    @PutMapping("/{id}")
    public Result<UserAddressVO> update(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "地址ID", required = true) @PathVariable Long id,
            @RequestBody(description = "更新后的地址信息", required = true, content = @Content(schema = @Schema(implementation = UserAddressDTO.class))) @org.springframework.web.bind.annotation.RequestBody UserAddressDTO dto) {
        UserAddressVO vo = addressService.updateAddress(userId, id, dto);
        return Result.success(vo);
    }

    @Operation(summary = "删除地址", description = "删除指定ID的收货地址")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@RequestHeader("X-User-Id") Long userId,
                               @Parameter(description = "地址ID", required = true) @PathVariable Long id) {
        addressService.deleteAddress(userId, id);
        return Result.success(null);
    }

    @Operation(summary = "设置默认地址", description = "将指定ID的地址设为当前用户的默认收货地址")
    @ApiResponse(responseCode = "200", description = "设置成功")
    @PostMapping("/{id}/default")
    public Result<Void> setDefault(@RequestHeader("X-User-Id") Long userId,
                                   @Parameter(description = "地址ID", required = true) @PathVariable Long id) {
        addressService.setDefaultAddress(userId, id);
        return Result.success(null);
    }
}