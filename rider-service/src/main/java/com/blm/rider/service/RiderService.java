package com.blm.rider.service;

import com.blm.common.dto.LocationUpdateDTO;
import com.blm.common.dto.RiderRegisterDTO;
import com.blm.common.dto.WorkStatusUpdateDTO;
import com.blm.common.vo.RiderOrderVO;
import com.blm.common.vo.RiderStatsVO;
import com.blm.common.vo.RiderVO;

import java.util.List;

/**
 * 骑手服务接口
 */
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
     * 更新位置信息
     * @param userId 用户ID
     * @param dto 位置信息
     */
    void updateLocation(Long userId, LocationUpdateDTO dto);

    /**
     * 获取可接订单列表
     * @param userId 用户ID
     * @return 可接订单列表
     */
    List<RiderOrderVO> getAvailableOrders(Long userId);

    /**
     * 获取我的订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    List<RiderOrderVO> getMyOrders(Long userId);

    /**
     * 接受订单
     * @param userId 用户ID
     * @param orderId 订单ID
     */
    void acceptOrder(Long userId, Long orderId);

    /**
     * 取餐
     * @param userId 用户ID
     * @param orderId 订单ID
     */
    void pickupOrder(Long userId, Long orderId);

    /**
     * 完成配送
     * @param userId 用户ID
     * @param orderId 订单ID
     */
    void completeDelivery(Long userId, Long orderId);

    /**
     * 获取统计数据
     * @param userId 用户ID
     * @param period 统计周期
     * @return 统计数据
     */
    RiderStatsVO getStats(Long userId, String period);

    /**
     * 根据骑手ID获取骑手信息
     * @param riderId 骑手ID
     * @return 骑手信息
     */
    RiderVO getRiderById(Long riderId);

    /**
     * 根据用户ID获取骑手信息
     * @param userId 用户ID
     * @return 骑手信息
     */
    RiderVO getRiderByUserId(Long userId);
}
