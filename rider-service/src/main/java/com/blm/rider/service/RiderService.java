package com.blm.rider.service;

import com.blm.common.dto.LocationUpdateDTO;
import com.blm.common.dto.OrderStatusUpdateDTO;
import com.blm.common.dto.RiderRegisterDTO;
import com.blm.common.dto.WorkStatusUpdateDTO;
import com.blm.common.entity.Rider;
import com.blm.common.vo.RiderOrderVO;
import com.blm.common.vo.RiderStatsVO;

import java.util.List;

public interface RiderService {
    /**
     * 注册成为骑手
     * @param userId 用户ID
     * @param dto 注册信息
     */
    void registerRider(Long userId, RiderRegisterDTO dto);
    
    /**
     * 更新工作状态
     * @param userId 用户ID
     * @param dto 工作状态信息
     */
    void updateWorkStatus(Long userId, WorkStatusUpdateDTO dto);
    
    /**
     * 更新骑手位置
     *
     * @param userId 用户ID
     * @param dto    位置更新信息
     * @return
     */
    Long updateLocation(Long userId, LocationUpdateDTO dto);
    
    /**
     * 获取可接单列表
     * @return 可接单列表
     */
    List<RiderOrderVO> listAvailableOrders();
    
    /**
     * 获取骑手的订单列表
     * @param userId 用户ID
     * @return 骑手订单列表
     */
    List<RiderOrderVO> listMyOrders(Long userId);
    
    /**
     * 接受订单
     * @param userId 用户ID
     * @param orderId 订单ID
     */
    void acceptOrder(Long userId, Long orderId);
    
    /**
     * 取餐开始配送
     * @param userId 用户ID
     * @param orderId 订单ID
     */
    void pickupOrder(Long userId, Long orderId);
    
    /**
     * 获取骑手统计数据
     * @param userId 用户ID
     * @param period 时间周期(day/week/month)
     * @return 统计数据
     */
    RiderStatsVO getStats(Long userId, String period);

    Rider.RiderStatus getWorkStatus(Long userId);

    void delivered(Long userId, Long orderId);
}