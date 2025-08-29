package com.blm.common.feign;

import com.blm.common.entity.Rider;
import com.blm.common.vo.PageVO;
import com.blm.common.vo.RiderVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 骑手服务Feign客户端
 */
@FeignClient(name = "rider-service", path = "/internal/riders")
public interface RiderServiceClient {

    @GetMapping("/{riderId}")
    Optional<Rider> getRiderById(@PathVariable Long riderId);

    @GetMapping("/")
    Optional<PageVO<RiderVO>> getRiderByConditions(
            @RequestParam(value = "status", required = false) Rider.RiderStatus status,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    );

    @PutMapping("/{riderId}/audit")
    int auditRider(
            @PathVariable("riderId") Long riderId,
            @RequestParam("status") Rider.RiderStatus status,
            @RequestParam("updateAt") LocalDateTime updateAt
    );

    @GetMapping("/count")
    Optional<Long> countRider();
}
