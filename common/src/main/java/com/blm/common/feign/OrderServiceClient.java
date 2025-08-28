package com.blm.common.feign;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 订单服务Feign客户端
 */
@FeignClient(name = "order-service", path = "/internal/orders")
public interface OrderServiceClient {
}
