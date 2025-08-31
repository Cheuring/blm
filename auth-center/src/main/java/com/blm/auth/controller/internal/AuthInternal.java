package com.blm.auth.controller.internal;

import com.blm.common.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/internal/auth")
public class AuthInternal {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/userId")
    public Long getUserId(@RequestParam("token") String token) {
        try {
            String userId = jwtUtil.getUserIdFromToken(token);
            if (userId != null) {
                return Long.parseLong(userId);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Failed to get userId from token", e);
            return null;
        }
    }
}
