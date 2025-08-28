package com.blm.rider.service.impl;

import com.blm.common.dto.LocationUpdateDTO;
import com.blm.common.dto.RiderRegisterDTO;
import com.blm.common.dto.WorkStatusUpdateDTO;
import com.blm.common.entity.Rider;
import com.blm.common.entity.RiderStats;
import com.blm.common.exception.BusinessException;
import com.blm.common.feign.OrderServiceClient;
import com.blm.common.feign.UserServiceClient;
import com.blm.common.result.Result;
import com.blm.common.vo.RiderOrderVO;
import com.blm.common.vo.RiderStatsVO;
import com.blm.common.vo.RiderVO;
import com.blm.common.vo.UserVO;
import com.blm.rider.repository.RiderRepository;
import com.blm.rider.repository.RiderStatsRepository;
import com.blm.rider.service.RiderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 骑手服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {

    private final RiderRepository riderRepository;
    private final RiderStatsRepository riderStatsRepository;
    private final OrderServiceClient orderServiceClient;
    private final UserServiceClient userServiceClient;

    @Override
    @Transactional
    public void registerRider(Long userId, RiderRegisterDTO dto) {
        // 检查用户是否已经是骑手
        Optional<Rider> existingRider = riderRepository.findByUserId(userId);
        if (existingRider.isPresent()) {
            throw new BusinessException("用户已经是骑手");
        }

        // 验证用户存在
        Result<UserVO> userResult = userServiceClient.getUserById(userId);
        if (!userResult.isSuccess() || userResult.getData() == null) {
            throw new BusinessException("用户不存在");
        }

        // 创建骑手记录
        Rider rider = new Rider();
        BeanUtils.copyProperties(dto, rider);
        rider.setUserId(userId);
        rider.setStatus(Rider.RiderStatus.OFFLINE); // 默认离线状态
        rider.setCreatedAt(LocalDateTime.now());
        rider.setUpdatedAt(LocalDateTime.now());

        riderRepository.insert(rider);
        log.info("用户 {} 注册成为骑手成功", userId);
    }

    @Override
    @Transactional
    public void updateWorkStatus(Long userId, WorkStatusUpdateDTO dto) {
        Rider rider = _getRiderByUserId(userId);
        rider.setStatus(dto.getStatus());
        rider.setUpdatedAt(LocalDateTime.now());
        
        riderRepository.updateStatus(rider.getId(), dto.getStatus(), LocalDateTime.now());
        log.info("骑手 {} 更新工作状态为 {}", userId, dto.getStatus());
    }

    @Override
    @Transactional
    public void updateLocation(Long userId, LocationUpdateDTO dto) {
        Rider rider = _getRiderByUserId(userId);
        
        riderRepository.updateLocation(rider.getId(), dto.getLongitude(), dto.getLatitude(), LocalDateTime.now());
        log.info("骑手 {} 更新位置为 ({}, {})", userId, dto.getLongitude(), dto.getLatitude());
    }

    @Override
    public List<RiderOrderVO> getAvailableOrders(Long userId) {
        // 验证骑手存在且状态为在线
        Rider rider = _getRiderByUserId(userId);
        if (!Rider.RiderStatus.ONLINE.equals(rider.getStatus())) {
            throw new BusinessException("骑手不在线，无法查看可接订单");
        }

        // 调用订单服务获取可接订单
        Result<List<RiderOrderVO>> result = orderServiceClient.getAvailableOrders();
        if (result.isSuccess()) {
            return result.getData();
        } else {
            throw new BusinessException("获取可接订单失败: " + result.getMessage());
        }
    }

    @Override
    public List<RiderOrderVO> getMyOrders(Long userId) {
        Rider rider = _getRiderByUserId(userId);
        
        // 调用订单服务获取骑手订单
        Result<List<RiderOrderVO>> result = orderServiceClient.getRiderOrders(rider.getId());
        if (result.isSuccess()) {
            return result.getData();
        } else {
            throw new BusinessException("获取订单列表失败: " + result.getMessage());
        }
    }

    @Override
    @Transactional
    public void acceptOrder(Long userId, Long orderId) {
        Rider rider = _getRiderByUserId(userId);
        
        // 检查骑手状态
        if (!Rider.RiderStatus.ONLINE.equals(rider.getStatus())) {
            throw new BusinessException("骑手不在线，无法接单");
        }

        // 调用订单服务接单
        Result<Void> result = orderServiceClient.acceptOrder(orderId, rider.getId());
        if (!result.isSuccess()) {
            throw new BusinessException("接单失败: " + result.getMessage());
        }
        
        log.info("骑手 {} 接受订单 {} 成功", userId, orderId);
    }

    @Override
    @Transactional
    public void pickupOrder(Long userId, Long orderId) {
        Rider rider = _getRiderByUserId(userId);
        
        // 调用订单服务取餐
        Result<Void> result = orderServiceClient.pickupOrder(orderId, rider.getId());
        if (!result.isSuccess()) {
            throw new BusinessException("取餐失败: " + result.getMessage());
        }
        
        log.info("骑手 {} 取餐订单 {} 成功", userId, orderId);
    }

    @Override
    @Transactional
    public void completeDelivery(Long userId, Long orderId) {
        Rider rider = _getRiderByUserId(userId);
        
        // 调用订单服务完成配送
        Result<Void> result = orderServiceClient.completeDelivery(orderId, rider.getId());
        if (!result.isSuccess()) {
            throw new BusinessException("完成配送失败: " + result.getMessage());
        }
        
        log.info("骑手 {} 完成配送订单 {} 成功", userId, orderId);
        
        // 更新统计数据
        updateRiderStats(rider.getId(), LocalDate.now());
    }

    @Override
    public RiderStatsVO getStats(Long userId, String period) {
        Rider rider = _getRiderByUserId(userId);
        
        LocalDate endDate = LocalDate.now();
        LocalDate startDate;
        
        switch (period) {
            case "week":
                startDate = endDate.minusDays(7);
                break;
            case "month":
                startDate = endDate.minusDays(30);
                break;
            default: // day
                startDate = endDate;
                break;
        }
        
        List<RiderStats> statsList = riderStatsRepository.findByRiderIdAndDateRange(
            rider.getId(), startDate, endDate);
        
        // 聚合统计数据
        RiderStatsVO statsVO = new RiderStatsVO();
        statsVO.setDate(endDate);
        
        int totalOrders = 0;
        int completedOrders = 0;
        int canceledOrders = 0;
        BigDecimal totalIncome = BigDecimal.ZERO;
        
        for (RiderStats stats : statsList) {
            totalOrders += stats.getOrdersCount();
            completedOrders += stats.getCompletedOrders();
            canceledOrders += stats.getCanceledOrders();
            totalIncome = totalIncome.add(stats.getTotalIncome());
        }
        
        statsVO.setOrdersCount(totalOrders);
        statsVO.setCompletedOrders(completedOrders);
        statsVO.setCanceledOrders(canceledOrders);
        statsVO.setTotalIncome(totalIncome);
        
        // 计算完成率
        if (totalOrders > 0) {
            BigDecimal completionRate = BigDecimal.valueOf(completedOrders)
                .divide(BigDecimal.valueOf(totalOrders), 2, BigDecimal.ROUND_HALF_UP);
            statsVO.setCompletionRate(completionRate);
        } else {
            statsVO.setCompletionRate(BigDecimal.ZERO);
        }
        
        return statsVO;
    }

    @Override
    public RiderVO getRiderById(Long riderId) {
        Optional<Rider> riderOpt = riderRepository.findById(riderId);
        if (!riderOpt.isPresent()) {
            throw new BusinessException("骑手不存在");
        }
        
        RiderVO riderVO = new RiderVO();
        BeanUtils.copyProperties(riderOpt.get(), riderVO);
        return riderVO;
    }

    @Override
    public RiderVO getRiderByUserId(Long userId) {
        Optional<Rider> riderOpt = riderRepository.findByUserId(userId);
        if (!riderOpt.isPresent()) {
            throw new BusinessException("用户不是骑手");
        }
        
        RiderVO riderVO = new RiderVO();
        BeanUtils.copyProperties(riderOpt.get(), riderVO);
        return riderVO;
    }

    /**
     * 获取骑手实体（内部方法）
     */
    private Rider _getRiderByUserId(Long userId) {
        return riderRepository.findByUserId(userId)
            .orElseThrow(() -> new BusinessException("用户不是骑手"));
    }

    /**
     * 更新骑手统计数据
     */
    private void updateRiderStats(Long riderId, LocalDate date) {
        Optional<RiderStats> existingStats = riderStatsRepository.findByRiderIdAndDate(riderId, date);
        
        RiderStats stats;
        if (existingStats.isPresent()) {
            stats = existingStats.get();
            stats.setCompletedOrders(stats.getCompletedOrders() + 1);
        } else {
            stats = new RiderStats();
            stats.setRiderId(riderId);
            stats.setDate(date);
            stats.setOrdersCount(1);
            stats.setCompletedOrders(1);
            stats.setCanceledOrders(0);
            stats.setTotalIncome(BigDecimal.ZERO); // 这里应该根据实际业务逻辑计算收入
            stats.setCreatedAt(LocalDateTime.now());
        }
        
        stats.setUpdatedAt(LocalDateTime.now());
        riderStatsRepository.save(stats);
    }
}
