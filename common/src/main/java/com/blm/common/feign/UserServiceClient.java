package com.blm.common.feign;

import com.blm.common.result.Result;
import com.blm.common.vo.UserVO;
import com.blm.common.vo.UserAddressVO;
import com.blm.common.dto.UserRegisterDTO;
import com.blm.common.dto.UserLoginDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户服务Feign客户端
 */
@FeignClient(name = "user-service", path = "/api/users")
public interface UserServiceClient {

    /**
     * 用户注册
     */
    @PostMapping("/register")
    Result<UserVO> register(@RequestBody UserRegisterDTO dto);

    /**
     * 用户登录
     */
    @PostMapping("/login")
    Result<String> login(@RequestBody UserLoginDTO dto);

    /**
     * 根据用户ID获取用户信息
     */
    @GetMapping("/{userId}")
    Result<UserVO> getUserById(@PathVariable("userId") Long userId);

    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/username/{username}")
    Result<UserVO> getUserByUsername(@PathVariable("username") String username);

    /**
     * 获取用户地址列表
     */
    @GetMapping("/{userId}/addresses")
    Result<List<UserAddressVO>> getUserAddresses(@PathVariable("userId") Long userId);

    /**
     * 根据地址ID获取地址信息
     */
    @GetMapping("/addresses/{addressId}")
    Result<UserAddressVO> getAddressById(@PathVariable("addressId") Long addressId);
}
