package com.blm.order.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI orderServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("饱了么订单服务API")
                        .description("订单和购物车管理相关接口")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("饱了么团队")
                                .email("contact@blm.com")));
    }
}
