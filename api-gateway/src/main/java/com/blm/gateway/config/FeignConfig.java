package com.blm.gateway.config;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Feign 配置类
 * 解决 Spring Cloud Gateway 与 Feign 集成时的 HttpMessageConverters 问题
 */
@Configuration
public class FeignConfig {

    /**
     * 配置 HttpMessageConverters Bean
     * Gateway 基于 WebFlux，需要手动提供 HttpMessageConverters 供 Feign 使用
     */
    @Bean
    public HttpMessageConverters messageConverters() {
        return new HttpMessageConverters(new MappingJackson2HttpMessageConverter());
    }
}
