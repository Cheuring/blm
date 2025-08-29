package com.blm.store.service;

import com.blm.common.vo.PageVO;
import com.blm.common.vo.ReviewVO;
import com.blm.common.vo.StoreStatisticsVO;

/**
 * 商家评价管理服务接口
 */
public interface MerchantReviewService {
    
    /**
     * 分页获取店铺评价
     * 
     * @param merchantId 商家ID
     * @param storeId 店铺ID
     * @param storeRating 评分筛选(可选)
     * @param page 页码(从0开始)
     * @param size 每页大小
     * @return 分页评价列表
     */
    PageVO<ReviewVO> listStoreReviews(Long merchantId, Long storeId, Integer storeRating, int page, int size);
    
    /**
     * 获取店铺评价统计
     * 
     * @param merchantId 商家ID
     * @param storeId 店铺ID
     * @return 评价统计信息
     */
    StoreStatisticsVO.ReviewStatisticsVO getReviewStatistics(Long merchantId, Long storeId);
}