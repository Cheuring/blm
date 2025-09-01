package com.blm.store.service.impl;

import com.blm.common.feign.OrderServiceClient;
import com.blm.common.feign.UserServiceClient;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.entity.Review;
import com.blm.common.entity.Store;
import com.blm.common.entity.User;
import com.blm.common.exception.CommonException;
import com.blm.common.vo.RatingAggregateVO;
import com.blm.store.service.MerchantReviewService;
import com.blm.store.service.StoreService;
import com.blm.common.vo.PageVO;
import com.blm.common.vo.ReviewVO;
import com.blm.common.vo.StoreStatisticsVO;
import com.github.pagehelper.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商家评价管理服务实现类
 */
@Service
public class MerchantReviewServiceImpl implements MerchantReviewService {

    @Autowired
    private OrderServiceClient orderService;
    
    @Autowired
    private StoreService storeService;

    @Autowired
    private UserServiceClient userService;


    @Override
    public PageVO<ReviewVO> listStoreReviews(Long merchantId, Long storeId, Integer storeRating, int page, int size) {
        // 验证店铺所有权
        storeService.verifyStoreOwner(storeId, merchantId);
        
        PageVO<ReviewVO> reviews;
        if(storeRating == null){
            reviews = orderService.getReviewsByStoreId(storeId, page, size)
                    .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));
        }else if(storeRating >= 1 && storeRating <= 5){
            reviews = orderService.getReviewsByStoreIdAndRating(storeId, storeRating, page, size)
                    .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));
        }else{
            throw new CommonException(ExceptionConstant.REQ_PARAM_ERROR);
        }

        return reviews;
    }
    
    @Override
    public StoreStatisticsVO.ReviewStatisticsVO getReviewStatistics(Long merchantId, Long storeId) {
        // 验证店铺所有权
        Store store = storeService.getStoreById(storeId);
                
        if (!store.getMerchantId().equals(merchantId)) {
            throw new CommonException(ExceptionConstant.STORE_UNAUTHORIZED);
        }
        
        StoreStatisticsVO.ReviewStatisticsVO statistics = new StoreStatisticsVO.ReviewStatisticsVO();
        statistics.setStoreId(storeId);
        statistics.setStoreName(store.getName());

        List<RatingAggregateVO> ratingAggregations = orderService.aggregateRatingsByStoreId(storeId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));

        int totalReviews = 0;
        double sumRating = 0.0;
        Map<Integer, Integer> ratingCounts = new HashMap<>();

        for (RatingAggregateVO aggregation : ratingAggregations) {
            Integer rating = aggregation.getRating();
            Long count = aggregation.getCount();
            totalReviews += count;
            sumRating += rating * count;

            // 统计各评分数量
            ratingCounts.put(rating, count.intValue());
        }

        statistics.setTotalReviews(totalReviews);
        statistics.setAverageRating(totalReviews > 0
                ? BigDecimal.valueOf(sumRating / totalReviews).setScale(2, RoundingMode.HALF_UP).doubleValue()
                : 0.0);
        statistics.setRatingCounts(ratingCounts);
        // 计算好评率：4-5星占比
        int goodReviews = ratingCounts.getOrDefault(4, 0) + ratingCounts.getOrDefault(5, 0);
        BigDecimal goodRate = totalReviews > 0
                ? BigDecimal.valueOf((double) goodReviews / totalReviews).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        statistics.setGoodRatePercentage(goodRate.doubleValue());

        return statistics;
    }
}