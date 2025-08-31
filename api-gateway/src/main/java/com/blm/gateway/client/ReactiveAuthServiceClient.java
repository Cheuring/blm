package com.blm.gateway.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * 响应式认证服务客户端
 */
@Component
public class ReactiveAuthServiceClient {

    @Autowired
    private WebClient.Builder webClientBuilder;

    /**
     * 响应式验证Token并获取用户ID
     */
    public Mono<Long> getUserId(String token) {
        return webClientBuilder.build()
                .get()
                .uri("http://auth-center/internal/auth/userId?token={token}", token)
                .retrieve()
                .bodyToMono(Long.class)
                .filter(userId -> userId != null)
                .switchIfEmpty(Mono.empty())
                .onErrorResume(throwable -> {
                    // Token验证失败，返回空的 Mono
                    return Mono.empty();
                });
    }
}
