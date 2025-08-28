package com.blm.rider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.blm.rider", "com.blm.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.blm.common.feign")
public class RiderApplication {

    public static void main(String[] args) {
        SpringApplication.run(RiderApplication.class, args);
    }
}
