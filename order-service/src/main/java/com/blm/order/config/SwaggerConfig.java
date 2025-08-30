package com.blm.order.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI orderServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("订单服务 API")
                        .description("订单管理、订单项管理等相关接口")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("BLM Team")
                                .email("support@blm.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8084").description("本地开发环境"),
                        new Server().url("http://localhost:8080").description("网关环境")
                ));
    }
}
