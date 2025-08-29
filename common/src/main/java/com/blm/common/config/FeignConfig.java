package com.blm.common.config;

import feign.Logger;
import feign.Request;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign 配置类
 * 用于配置 Feign 客户端的日志级别、超时时间等
 */
@Configuration
public class FeignConfig {

    /**
     * 配置 Feign 日志级别
     * NONE: 不记录任何日志
     * BASIC: 记录请求方法、URL、响应状态码和执行时间
     * HEADERS: 在 BASIC 基础上记录请求和响应的头信息
     * FULL: 记录请求和响应的头信息、正文和元数据
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * 配置请求超时时间
     */
    @Bean
    public Request.Options requestOptions() {
        return new Request.Options();
    }

    /**
     * 配置编码器
     */
    @Bean
    public Encoder feignEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringEncoder(messageConverters);
    }

    /**
     * 配置解码器
     */
    @Bean
    public Decoder feignDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new ResponseEntityDecoder(new SpringDecoder(messageConverters));
    }
}
