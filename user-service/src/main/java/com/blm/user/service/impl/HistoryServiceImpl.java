package com.blm.user.service.impl;


import com.blm.common.entity.Food;
import com.blm.common.entity.History;
import com.blm.common.entity.Store;
import com.blm.common.exception.CommonException;
import com.blm.common.feign.StoreServiceClient;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.vo.FoodVO;
import com.blm.common.vo.PageVO;
import com.blm.common.vo.StoreVO;
import com.blm.user.repository.HistoryRepository;
import com.blm.user.service.HistoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private StoreServiceClient storeService;

    @Override
    public PageVO<StoreVO> listStoresHistory(Long userId, int page, int size) {
        PageHelper.startPage(page, size);
        List<History> historyList = historyRepository.findByUserIdOrderedDesc(userId, History.TYPE_STORE);

        PageInfo<History> pageInfo = new PageInfo<>(historyList);

        List<StoreVO> storeVOList = historyList.stream().map(history -> {
            Store store = storeService.getStoreById(history.getTargetId())
                    .orElseThrow(() -> new CommonException(ExceptionConstant.STORE_NOT_FOUND));
            StoreVO vo = new StoreVO();
            BeanUtils.copyProperties(store, vo);
            vo.setHistoryId(history.getId());
            vo.setVisitedAt(history.getCreatedAt());
            return vo;
        }).collect(Collectors.toList());

        return new PageVO<>(page, size, pageInfo.getTotal(), pageInfo.getPages(), storeVOList);
    }

    @Override
    public PageVO<FoodVO> listFoodsHistory(Long userId, int page, int size) {
        PageHelper.startPage(page, size);
        List<History> historyList = historyRepository.findByUserIdOrderedDesc(userId, History.TYPE_FOOD);

        PageInfo<History> pageInfo = new PageInfo<>(historyList);

        List<FoodVO> foodVOList = historyList.stream().map(history -> {
            Food food = storeService.getFoodById(history.getTargetId())
                    .orElseThrow(() -> new CommonException(ExceptionConstant.FOOD_NOT_FOUND));
            FoodVO vo = new FoodVO();
            BeanUtils.copyProperties(food, vo);
            // 替换id为历史记录id
            vo.setId(history.getId());
            vo.setVisitedAt(history.getCreatedAt());
            return vo;
        }).collect(Collectors.toList());

        return new PageVO<>(page, size, pageInfo.getTotal(), pageInfo.getPages(), foodVOList);
    }

    @Override
    @Transactional
    public void removeHistory(Long userId, Long historyId) {
        // Check if history exists
        historyRepository.findByIdAndUserId(historyId, userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.HISTORY_NOT_FOUND));

        historyRepository.deleteById(historyId);
    }

    @Override
    @Transactional
    public void addStoreHistory(Long userId, Long storeId) {
        // Check if store exists
        storeService.getStoreById(storeId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.STORE_NOT_FOUND));
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime earliest = now.toLocalDate().atStartOfDay();
        // Check if there is already a record for today
        Optional<History> recordInTheDay = historyRepository.findRecordInTheDay(userId, History.TYPE_STORE, earliest);
        if (recordInTheDay.isPresent()) {
            // If exists, update the existing record
            historyRepository.updateVisitedTime(recordInTheDay.get().getId(), now);
        } else {
            historyRepository.insert(userId, storeId, History.TYPE_STORE);
        }
    }

    @Override
    @Transactional
    public void addFoodHistory(Long userId, Long foodId) {
        // Check if food exists
        storeService.getFoodById(foodId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.FOOD_NOT_FOUND));
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime earliest = now.toLocalDate().atStartOfDay();
        // Check if there is already a record for today
        Optional<History> recordInTheDay = historyRepository.findRecordInTheDay(userId, History.TYPE_FOOD, earliest);
        if (recordInTheDay.isPresent()) {
            // If exists, update the existing record
            historyRepository.updateVisitedTime(recordInTheDay.get().getId(), now);
        } else {
            historyRepository.insert(userId, foodId, History.TYPE_FOOD);
        }
    }
}
