package com.blm.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 认证中心启动类
 */
@SpringBootApplication(scanBasePackages = {"com.blm.auth", "com.blm.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.blm.common.feign")
public class AuthCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthCenterApplication.class, args);
    }
}
