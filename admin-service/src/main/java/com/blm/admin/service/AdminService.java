package com.blm.admin.service;

import com.blm.common.dto.AuditDTO;
import com.blm.common.dto.StoreCategoryDTO;
import com.blm.common.entity.*;
import com.blm.common.vo.*;

import java.time.LocalDate;

/**
 * 管理员服务接口
 */
public interface AdminService {

    /**
     * 获取用户列表
     * @param role 用户角色
     * @param status 用户状态
     * @param keyword 关键词搜索
     * @param page 页码
     * @param size 每页大小
     * @return 用户分页列表
     */
    PageVO<UserVO> listUsers(User.UserRole role, Integer status, String keyword, int page, int size);

    /**
     * 更新用户状态
     * @param userId 用户ID
     * @param dto 状态更新DTO
     */
    void updateUserStatus(Long userId, AuditDTO dto);

    /**
     * 获取商家店铺列表
     * @param status 店铺状态
     * @param keyword 关键词搜索
     * @param page 页码
     * @param size 每页大小
     * @return 店铺分页列表
     */
    PageVO<StoreVO> listStores(Store.StoreStatus status, String keyword, int page, int size);

    /**
     * 审核店铺
     * @param storeId 店铺ID
     * @param dto 审核DTO
     */
    void auditStore(Long storeId, AuditDTO dto);

    /**
     * 获取商品列表
     * @param status 商品状态
     * @param keyword 关键词搜索
     * @param storeId 店铺ID
     * @param page 页码
     * @param size 每页大小
     * @return 商品分页列表
     */
    PageVO<FoodVO> listFoods(Food.FoodStatus status, String keyword, Long storeId, int page, int size);

    /**
     * 审核商品
     * @param foodId 商品ID
     * @param dto 审核DTO
     */
    void auditFood(Long foodId, AuditDTO dto);

    /**
     * 获取骑手列表
     * @param status 骑手状态
     * @param keyword 关键词搜索
     * @param page 页码
     * @param size 每页大小
     * @return 骑手分页列表
     */
    PageVO<RiderVO> listRiders(Rider.RiderStatus status, String keyword, int page, int size);

    /**
     * 更新骑手状态
     * @param riderId 骑手ID
     * @param dto 状态更新DTO
     */
    void updateRiderStatus(Long riderId, AuditDTO dto);

    /**
     * 获取所有订单
     * @param status 订单状态
     * @param userId 用户ID
     * @param storeId 店铺ID
     * @param riderId 骑手ID
     * @param page 页码
     * @param size 每页大小
     * @return 订单分页列表
     */
    PageVO<OrderVO> listAllOrders(Order.OrderStatus status, Long userId, Long storeId, Long riderId, int page, int size);

    /**
     * 获取评价列表
     * @param userId 用户ID
     * @param storeId 店铺ID
     * @param foodId 商品ID
     * @param rating 评分
     * @param page 页码
     * @param size 每页大小
     * @return 评价分页列表
     */
    PageVO<ReviewVO> listReviews(Long userId, Long storeId, Long foodId, Integer rating, int page, int size);

    /**
     * 删除评价
     * @param reviewId 评价ID
     */
    void deleteReview(Long reviewId);

    /**
     * 获取平台统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 平台统计数据
     */
    PlatformStatsVO getPlatformStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * 获取店铺种类列表
     * @param page
     * @param size
     * @return
     */
    PageVO<StoreCategoryVO> listStoreCategory(int page, int size);

    /**
     * 新增店铺种类
     * @param dto
     * @return
     */
    StoreCategoryVO addStoreCategory(StoreCategoryDTO dto);

    /**
     * 更新店铺种类
     * @param id
     * @param dto
     * @return
     */
    StoreCategoryVO updateStoreCategory(Long id, StoreCategoryDTO dto);

    /**
     * 删除店铺种类
     * @param id
     */
    void deleteStoreCategory(Long id);

    OrderDetailVO getOrderDetail(Long id);
}