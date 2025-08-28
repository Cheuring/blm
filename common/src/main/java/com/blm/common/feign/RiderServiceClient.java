package com.blm.common.feign;

import com.blm.common.entity.Rider;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * 骑手服务Feign客户端
 */
@FeignClient(name = "rider-service", path = "/internal/riders")
public interface RiderServiceClient {

    @GetMapping("/{riderId}")
    Optional<Rider> getRiderById(@PathVariable Long riderId);
}
