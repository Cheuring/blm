package com.blm.store.service;

import com.blm.common.vo.*;

import java.time.LocalDate;

/**
 * 商家统计服务接口
 */

public interface MerchantStatisticsService {
    /**
     * 获取商家统计数据
     * @param merchantId 商家ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 商家统计数据
     */
    StoreStatisticsVO getStoreformStatistics(Long merchantId,Long  storeId, LocalDate startDate, LocalDate endDate);
}