
package com.blm.rider.controller.internal;

import com.blm.common.entity.Rider;
import com.blm.common.vo.PageVO;
import com.blm.common.vo.RiderVO;
import com.blm.rider.service.RiderInternalService;
import com.blm.rider.service.RiderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Optional;

/**
/**
 * 骑手服务内部接口实现
 */
@Slf4j
@RestController
@RequestMapping("/internal/riders")
public class RiderInternal {

	@Autowired
	private RiderInternalService riderInternalService;

	/**
	 * 根据ID获取骑手信息
	 */
	@GetMapping("/{riderId}")
	public Optional<Rider> getRiderById(@PathVariable("riderId") Long riderId) {
		Rider rider = null;
		try {
			rider = riderInternalService.getRiderById(riderId).orElse(null);
		} catch (Exception e) {
			log.warn("getRiderById error: {}", e.getMessage());
		}
		return Optional.ofNullable(rider);
	}

	/**
	 * 条件查询骑手列表
	 */
	@GetMapping("/")
	public Optional<PageVO<RiderVO>> getRiderByConditions(
			@RequestParam(value = "status", required = false) Rider.RiderStatus status,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam("page") int page,
			@RequestParam("size") int size) {
		PageVO<RiderVO> pageVO = null;
		try {
			pageVO = riderInternalService.getRiderByConditions(status, keyword, page, size).orElse(null);
		} catch (Exception e) {
			log.warn("getRiderByConditions error: {}", e.getMessage());
		}
		return Optional.ofNullable(pageVO);
	}

	/**
	 * 审核骑手
	 */
	@PutMapping("/{riderId}/audit")
	public int auditRider(
			@PathVariable("riderId") Long riderId,
			@RequestParam("status") Rider.RiderStatus status,
			@RequestParam("updateAt") LocalDateTime updateAt) {
		try {
			return riderInternalService.auditRider(riderId, status, updateAt);
		} catch (Exception e) {
			log.warn("auditRider error: {}", e.getMessage());
			return 0;
		}
	}

	/**
	 * 统计骑手总数
	 */
	@GetMapping("/count")
	public Optional<Long> countRider() {
		Long count = null;
		try {
			count = riderInternalService.countRider().orElse(null);
		} catch (Exception e) {
			log.warn("countRider error: {}", e.getMessage());
		}
		return Optional.ofNullable(count);
	}
}
