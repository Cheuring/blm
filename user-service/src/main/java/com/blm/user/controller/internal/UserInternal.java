package com.blm.user.controller.internal;

import com.blm.common.dto.RegisterDTO;
import com.blm.common.entity.User;
import com.blm.common.vo.UserVO;
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
    private UserService userService;

    @GetMapping("/{userId}")
    public Optional<User> getUserById(@PathVariable("userId") Long userId) {
        User user = null;
        try {
            user = userService.findUserById(userId);
        } catch (Exception ignored) {
        }
        return Optional.ofNullable(user);
    }

    @GetMapping("/username/{username}")
    public Optional<User> getUserByUsername(@PathVariable("username") String username) {
        User user = null;
        try {
            user = userService.findUserByUsername(username);
        } catch (Exception ignored) {
        }
        return Optional.ofNullable(user);
    }

    @PostMapping("/register")
    public Optional<UserVO> register(@RequestBody RegisterDTO dto) {
        UserVO userVO = null;
        try {
            userVO = userService.register(dto);
        } catch (Exception ignored) {
            log.warn(ignored.getMessage());
        }
        return Optional.ofNullable(userVO);
    }
}
