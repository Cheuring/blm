package com.blm.common.feign;

import com.blm.common.dto.RegisterDTO;
import com.blm.common.entity.User;
import com.blm.common.entity.UserAddress;
import com.blm.common.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 用户服务Feign客户端
 */
@FeignClient(name = "user-service", path = "/internal/users")
public interface UserServiceClient {

    /**
     * 根据用户ID获取用户信息
     */
    @GetMapping("/{userId}")
    Optional<User> getUserById(@PathVariable("userId") Long userId);

    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/username/{username}")
    Optional<User> getUserByUsername(@PathVariable("username") String username);

    @PostMapping("/register")
    Optional<UserVO> register(@RequestBody RegisterDTO dto);

    @GetMapping("{userId}/address/{Id}")
    Optional<UserAddress> getAddressByUserIdAndId(@PathVariable("userId") Long userId, @PathVariable("Id") Long Id);

    @GetMapping("/address/{Id}")
    Optional<UserAddress> getAddressById(@PathVariable("Id") Long Id);
}
