package com.blm.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 用户服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.blm.user", "com.blm.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.blm.common.feign")
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
