package com.blm.user.service;

import com.blm.common.vo.FoodVO;
import com.blm.common.vo.PageVO;
import com.blm.common.vo.StoreVO;
import jakarta.validation.constraints.NotNull;

public interface HistoryService {
    /**
     * 获取用户的浏览历史（支持分页和类型过滤）
     */
    PageVO<StoreVO> listStoresHistory(Long userId, int page, int size);
    PageVO<FoodVO> listFoodsHistory(Long userId, int page, int size);

    /**
     * 移除指定的浏览历史
     */
    void removeHistory(Long userId, Long historyId);

    /**
     * 添加浏览历史
     */
    void addStoreHistory(Long userId, @NotNull(message = "目标ID不能为空") Long targetId);
    void addFoodHistory(Long userId, @NotNull(message = "目标ID不能为空") Long targetId);

}