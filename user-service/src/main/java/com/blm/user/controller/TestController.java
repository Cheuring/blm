package com.blm.user.controller;

import com.blm.user.config.TestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/test")
public class TestController {
    @Autowired
    private TestConfig testConfig;

    @GetMapping("/")
    public TestConfig test() {
        return testConfig;
    }
}
