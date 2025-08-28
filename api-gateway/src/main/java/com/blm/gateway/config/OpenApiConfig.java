package com.blm.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

/**
 * 网关 OpenAPI 聚合配置
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gatewayOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("饱了么微服务 API 网关")
                        .description("聚合所有微服务的 API 文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("BLM Team")
                                .email("support@blm.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("API 网关")
                ));
    }
}
