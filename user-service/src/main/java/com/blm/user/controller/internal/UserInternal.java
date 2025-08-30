package com.blm.user.controller.internal;

import com.blm.common.dto.RegisterDTO;
import com.blm.common.entity.User;
import com.blm.common.entity.UserAddress;
import com.blm.common.vo.UserVO;
import com.blm.common.vo.PageVO;
import com.blm.user.service.UserInternalService;
import com.blm.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/internal/users")
public class UserInternal {

    @Autowired
    private UserInternalService userInternalService;

    @Autowired
    private UserService userService;

    /**
     * 根据用户ID获取用户信息
     */
    @GetMapping("/{userId}")
    public Optional<User> getUserById(@PathVariable("userId") Long userId) {
        User user = null;
        try {
            user = userService.findUserById(userId);
        } catch (Exception e) {
            log.warn("getUserById error: {}", e.getMessage());
        }
        return Optional.ofNullable(user);
    }

    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/username/{username}")
    public Optional<User> getUserByUsername(@PathVariable("username") String username) {
        User user = null;
        try {
            user = userService.findUserByUsername(username);
        } catch (Exception e) {
            log.warn("getUserByUsername error: {}", e.getMessage());
        }
        return Optional.ofNullable(user);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Optional<UserVO> register(@RequestBody RegisterDTO dto) {
        UserVO userVO = null;
        try {
            userVO = userService.register(dto);
        } catch (Exception e) {
            log.warn("register error: {}", e.getMessage());
        }
        return Optional.ofNullable(userVO);
    }

    /**
     * 根据用户ID和id获取用户地址信息
     */
    @GetMapping("/{userId}/address/{id}")
    public Optional<UserAddress> getAddressByUserIdAndId(@PathVariable("userId") Long userId, @PathVariable("id") Long id) {
        UserAddress address = null;
        try {
            address = userInternalService.findAddressByUserIdAndId(userId, id);
        } catch (Exception e) {
            log.warn("getAddressByUserIdAndId error: {}", e.getMessage());
        }
        return Optional.ofNullable(address);
    }

    /**
     * 根据用户ID获取用户地址信息
     */
    @GetMapping("/address/{id}")
    public Optional<UserAddress> getAddressById(@PathVariable("id") Long id) {
        UserAddress address = null;
        try {
            address = userInternalService.findAddressById(id);
        } catch (Exception e) {
            log.warn("getAddressById error: {}", e.getMessage());
        }
        return Optional.ofNullable(address);
    }

    /**
     * 更新用户角色
     */
    @PutMapping("/{userId}/role")
    public int updateRole(@PathVariable("userId") Long userId, @RequestParam("role") User.UserRole role) {
        try {
            return userInternalService.updateRole(userId, role);
        } catch (Exception e) {
            log.warn("updateRole error: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * 更新状态
     */
    @PutMapping("/{userId}/status")
    public int updateStatus(@PathVariable("userId") Long userId, @RequestParam("status") Integer status) {
        try {
            return userInternalService.updateStatus(userId, status);
        } catch (Exception e) {
            log.warn("updateStatus error: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * 根据条件获取用户列表
     */
    @GetMapping("")
    public Optional<PageVO<UserVO>> getByConditions(
            @RequestParam(value = "role", required = false) User.UserRole role,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        PageVO<UserVO> pageVO = null;
        try {
            pageVO = userInternalService.getByConditions(role, status, keyword, page, size);
        } catch (Exception e) {
            log.warn("getByConditions error: {}", e.getMessage());
        }
        return Optional.ofNullable(pageVO);
    }

    /**
     * 获取用户数量
     */
    @GetMapping("/count")
    public Optional<Long> countUser(
            @RequestParam(value = "role", required = false) User.UserRole role,
            @RequestParam(value = "start", required = false) java.time.LocalDateTime start,
            @RequestParam(value = "end", required = false) java.time.LocalDateTime end
    ) {
        Long count = null;
        try {
            count = userInternalService.countUser(role, start, end);
        } catch (Exception e) {
            log.warn("countUser error: {}", e.getMessage());
        }
        return Optional.ofNullable(count);
    }
}
