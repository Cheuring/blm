package com.blm.store.controller.Internal;

import com.blm.common.dto.AuditDTO;
import com.blm.common.entity.Food;
import com.blm.common.entity.Store;
import com.blm.common.entity.StoreCategory;
import com.blm.common.exception.CommonException;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.service.BaseService;
import com.blm.common.vo.FoodVO;
import com.blm.common.vo.PageVO;
import com.blm.common.vo.StoreCategoryVO;
import com.blm.common.vo.StoreVO;
import com.blm.store.repository.FoodCategoryRepository;
import com.blm.store.repository.FoodRepository;
import com.blm.store.repository.StoreCategoryRepository;
import com.blm.store.repository.StoreRepository;
import com.blm.store.service.StoreService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/internal/stores")
public class StoreInternal extends BaseService {

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodCategoryRepository categoryRepository;

    @Autowired
    private StoreCategoryRepository storeCategoryRepository;

    @GetMapping("/{storeId}")
    public Optional<Store> getStoreById(@PathVariable("storeId") Long storeId) {
        Store store = null;
        try {
            store = storeService.getStoreById(storeId);
        } catch (Exception e) {
            return Optional.ofNullable(store);
        }
        return Optional.ofNullable(store);
    }

    @GetMapping("/{storeId}/status")
    public Optional<Store> getStoreByIdAndStatus(@PathVariable("storeId") Long storeId,
                                                 @RequestParam("status") Store.StoreStatus status) {
        try {
            Optional<Store> store = storeRepository.findByIdAndStatus(storeId, status);
            return store;
        } catch (Exception e) {
            log.error("Error getting store by id and status: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @GetMapping("/foods/{foodId}")
    public Optional<Food> getFoodById(@PathVariable("foodId") Long foodId) {
        try {
            Optional<Food> food = foodRepository.findById(foodId);
            return food;
        } catch (Exception e) {
            log.error("Error getting food by id: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @GetMapping("/foods/{foodId}/status")
    public Optional<Food> getFoodByIdAndStatus(@PathVariable("foodId") Long foodId,
                                               @RequestParam("status") Food.FoodStatus status) {
        try {
            Optional<Food> food = foodRepository.findByIdAndStatus(foodId, status);
            return food;
        } catch (Exception e) {
            log.error("Error getting food by id and status: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @GetMapping("/{storeId}/owner")
    public Optional<Long> getStoreOwnerIdByStoreId(@PathVariable("storeId") Long storeId) {
        try {
            Long ownerId = storeService.getStoreOwnerId(storeId);
            return Optional.ofNullable(ownerId);
        } catch (Exception e) {
            log.error("Error getting store owner id: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @GetMapping("/")
    public Optional<PageVO<StoreVO>> getStoreByConditions(
            @RequestParam(value = "status", required = false) Store.StoreStatus status,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        // 验证状态值是否合法
        if (status != null) {
            try {
                Store.StoreStatus.valueOf(String.valueOf(status));
            } catch (IllegalArgumentException e) {
                return Optional.ofNullable(null);
            }
        }

        // 使用PageHelper进行分页
        PageHelper.startPage(page, size);

        // 根据条件查询店铺列表
        List<Store> stores = storeRepository.findByStatusAndKeyword(status, keyword);

        // 获取PageHelper分页信息
        Page<Store> pageInfo = (Page<Store>) stores;
        // 转换为VO列表
        List<StoreVO> storeVOs = entity2VO(stores, StoreVO.class);
        // 构造分页结果
        return Optional.of(new PageVO<>(page, size, pageInfo.getTotal(), pageInfo.getPages(), storeVOs));
    }

    @GetMapping("/{storeId}/foods")
    public Optional<PageVO<FoodVO>> getFoodByConditions(
            @PathVariable("storeId") Long storeId,
            @RequestParam(value = "status", required = false) Food.FoodStatus status,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        // 使用PageHelper进行分页
        PageHelper.startPage(page, size);

        // 根据条件查询商品列表
        List<Food> foods = foodRepository.findByStatusAndKeywordAndStoreId(status, keyword, storeId);

        // 获取PageHelper分页信息
        Page<Food> pageInfo = (Page<Food>) foods;
        // 转换为VO列表
        List<FoodVO> foodVOs = entity2VO(foods, FoodVO.class);

        // 构造分页结果
        return Optional.of(new PageVO<>(page, size, pageInfo.getTotal(), pageInfo.getPages(), foodVOs));
    }

    @PutMapping("/{storeId}/audit")
    public Optional<Integer> auditStore(@PathVariable("storeId") Long storeId,
                          @RequestParam(value = "status", required = false) Store.StoreStatus status,
                          @RequestParam(value = "reason", required = false) String reason,
                          @RequestParam("updateAt") LocalDateTime updateAt) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.STORE_NOT_FOUND));
        store.setUpdatedAt(updateAt);
        storeRepository.setRejectReason(storeId, reason);
        storeRepository.save(store);
        return Optional.of(1);
    }

    @PutMapping("/food/{foodId}/audit")
    public Optional<Integer> auditFood(@PathVariable("foodId") Long foodId,
                         @RequestParam(value = "status", required = false) Food.FoodStatus status,
                         @RequestParam(value = "reason", required = false) String reason,
                         @RequestParam("updateAt") LocalDateTime updateAt) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.FOOD_NOT_FOUND));
        food.setUpdatedAt(updateAt);
        foodRepository.save(food);
        return Optional.of(1);

    }

    @PutMapping("/{storeId}/rating")
    public Optional<Integer> updateStoreRating(@PathVariable("storeId") Long storeId,
                                 @RequestParam("newRating") double newRating,
                                 @RequestParam("updateAt") LocalDateTime updateAt) {
        try {
            return Optional.of(storeRepository.updateRating(storeId, newRating, updateAt));
        } catch (Exception e) {
            log.error("Error updating store rating: {}", e.getMessage());
            return Optional.of(0);
        }
    }

    @GetMapping("/category")
    public Optional<PageVO<StoreCategoryVO>> getAllStoreCategory(@RequestParam("page") int page,
                                                                 @RequestParam("size") int size) {
        //使用PageHelper进行分页
        PageHelper.startPage(page, size);
        //查询店铺种类
        List<StoreCategory> storeCategories = storeCategoryRepository.findAll();
        //获取PageHelper分页信息
        Page<StoreCategory> pageInfo = (Page<StoreCategory>) storeCategories;
        //转换为VO列表
        List<StoreCategoryVO> storeCategoryVOs = entity2VO(storeCategories, StoreCategoryVO.class);

        //构造分页结果
        return Optional.of(new PageVO<>(page, size, pageInfo.getTotal(), pageInfo.getPages(), storeCategoryVOs));
    }

    @GetMapping("/category/{id}")
    public Optional<StoreCategory> getStoreCategoryById(@PathVariable("id") Long id) {
        try {
            StoreCategory category = storeCategoryRepository.findById(id);
            return Optional.of(category);
        } catch (Exception e) {
            log.error("Error getting store category by id: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @PutMapping("/category/{id}")
    public Optional<StoreCategory> updateStoreCategory(@PathVariable("id") Long id,
                                                       @RequestBody StoreCategory storeCategory) {
        try {
            storeCategoryRepository.update(storeCategory);
            return Optional.of(storeCategory);
        } catch (Exception e) {
            log.error("Error updating store category: {}", e.getMessage());
            return Optional.empty();
        }

    }

    @DeleteMapping("/category/{id}")
    public Optional<Integer> deleteStoreCategoryById(@PathVariable("id") Long id) {
        try {
            storeCategoryRepository.deleteById(id);
            return Optional.of(1);
        } catch (Exception e) {
            log.error("Error deleting store category by id: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @PostMapping("/category")
    public Optional<StoreCategory> addStoreCategory(@RequestBody StoreCategory storeCategory) {
        try {
            storeCategoryRepository.insert(storeCategory);
            return Optional.of(storeCategory);
        } catch (Exception e) {
            log.error("Error adding store category: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
