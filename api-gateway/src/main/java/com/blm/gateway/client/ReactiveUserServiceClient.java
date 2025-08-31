package com.blm.gateway.client;

import com.blm.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * 响应式用户服务客户端
 */
@Component
public class ReactiveUserServiceClient {

    @Autowired
    private WebClient.Builder webClientBuilder;

    /**
     * 响应式获取用户信息
     */
    public Mono<User> getUserById(Long userId) {
        return webClientBuilder.build()
                .get()
                .uri("http://user-service/internal/users/{id}", userId)
                .retrieve()
                .bodyToMono(User.class)
                .onErrorResume(throwable -> {
                    // 错误处理，返回空的 Mono
                    return Mono.empty();
                });
    }
}
