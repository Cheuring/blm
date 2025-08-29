package com.blm.rider.service.impl;

import com.blm.common.entity.Rider;
import com.blm.common.exception.CommonException;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.service.BaseService;
import com.blm.common.vo.PageVO;
import com.blm.common.vo.RiderVO;
import com.blm.rider.repository.RiderRepository;
import com.blm.rider.service.RiderInternalService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RiderInternalServiceImpl extends BaseService implements RiderInternalService {
    private static final Logger log = LoggerFactory.getLogger(RiderInternalServiceImpl.class);

    @Autowired
    private RiderRepository riderRepository;

    @Override
    public Optional<Rider> getRiderById(Long riderId) {
        Rider rider = null;
        try {
            rider = riderRepository.findById(riderId)
                    .orElseThrow(() -> new CommonException(ExceptionConstant.RIDER_NOT_FOUND));
        } catch (Exception e) {
            log.warn("getRiderById error: {}", e.getMessage());
        }
        return Optional.ofNullable(rider);
    }

    @Override
    public Optional<PageVO<RiderVO>> getRiderByConditions(Rider.RiderStatus status, String keyword, int page, int size) {
        try {
            // 使用PageHelper进行分页
            PageHelper.startPage(page, size);

            // 根据条件查询骑手列表
            List<Rider> riders = riderRepository.findByStatusAndKeyword(status, keyword);

            // 获取PageHelper分页信息
            Page<Rider> pageInfo = (Page<Rider>) riders;
            // 转换为VO列表
            List<RiderVO> riderVOs = entity2VO(riders, RiderVO.class);

            // 构造分页结果
            return Optional.of(new PageVO<>(page, size, pageInfo.getTotal(), pageInfo.getPages(), riderVOs));
        } catch (Exception e) {
            log.warn("getRiderByConditions error: {}", e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public int auditRider(Long riderId, Rider.RiderStatus status, LocalDateTime updateAt) {
        try {
            Rider rider = riderRepository.findById(riderId)
                    .orElseThrow(() -> new CommonException(ExceptionConstant.RIDER_NOT_FOUND));

            rider.setStatus(status);
            rider.setUpdatedAt(LocalDateTime.now());
            riderRepository.save(rider);
            return 1;
        } catch (Exception e) {
            log.warn("auditRider error: {}", e.getMessage());
            return 0;
        }
    }

    @Override
    public Optional<Long> countRider() {
        Long count = null;
        try {
            long totalRiders = riderRepository.count();
            count = Long.valueOf(totalRiders);
        } catch (Exception e) {
            log.warn("countRider error: {}", e.getMessage());
        }
        return Optional.ofNullable(count);
    }
}
