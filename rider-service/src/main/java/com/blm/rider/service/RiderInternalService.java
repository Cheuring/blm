package com.blm.rider.service;

import com.blm.common.entity.Rider;
import com.blm.common.vo.PageVO;
import com.blm.common.vo.RiderVO;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RiderInternalService {
    Optional<Rider> getRiderById(Long riderId);

    Optional<PageVO<RiderVO>> getRiderByConditions(Rider.RiderStatus status, String keyword, int page, int size);

    int auditRider(Long riderId, Rider.RiderStatus status, LocalDateTime updateAt);

    Optional<Long> countRider();
}
