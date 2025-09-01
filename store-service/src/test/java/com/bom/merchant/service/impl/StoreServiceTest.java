package com.bom.merchant.service.impl;

import com.blm.common.dto.StoreQueryDTO;
import com.blm.common.entity.Food;
import com.blm.common.entity.Store;
import com.blm.common.vo.*;
import com.blm.store.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * StoreService接口测试类
 * 针对每个方法实现正向和反向测试
 */
@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreService storeService;

    private Long validStoreId;
    private Long validFoodId;
    private Long validCategoryId;
    private Long validUserId;
    private StoreQueryDTO validStoreQueryDTO;
    private StoreVO sampleStoreVO;
    private StoreDetailVO sampleStoreDetailVO;
    private FoodVO sampleFoodVO;
    private FoodDetailVO sampleFoodDetailVO;
    private PageVO<StoreVO> samplePageVO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        validStoreId = 1L;
        validFoodId = 10L;
        validCategoryId = 5L;
        validUserId = 100L;
        
        // 初始化查询DTO
        validStoreQueryDTO = new StoreQueryDTO();
        validStoreQueryDTO.setKeyword("美味餐厅");
        validStoreQueryDTO.setCategoryId(validCategoryId);
        validStoreQueryDTO.setLongitude(116.307852);
        validStoreQueryDTO.setLatitude(39.983424);
        validStoreQueryDTO.setSortBy(StoreQueryDTO.SearchSort.DISTANCE);
        validStoreQueryDTO.setMinPrice(0.0);
        validStoreQueryDTO.setMaxPrice(50.0);
        validStoreQueryDTO.setPage(1);
        validStoreQueryDTO.setSize(10);
        
        // 初始化店铺VO
        sampleStoreVO = new StoreVO();
        sampleStoreVO.setId(validStoreId);
        sampleStoreVO.setName("美味快餐店");
        sampleStoreVO.setLogo("https://example.com/logo.jpg");
        sampleStoreVO.setDescription("提供各种美味快餐");
        sampleStoreVO.setPhone("13800138000");
        sampleStoreVO.setAddress("北京市海淀区中关村大街1号");
        sampleStoreVO.setLongitude(new BigDecimal("116.307852"));
        sampleStoreVO.setLatitude(new BigDecimal("39.983424"));
        sampleStoreVO.setBusinessHours("08:00-22:00");
        sampleStoreVO.setDeliveryFee(new BigDecimal("5.00"));
        sampleStoreVO.setMinOrderAmount(new BigDecimal("20.00"));
        sampleStoreVO.setAverageDeliveryTime(30);
        sampleStoreVO.setCategoryId(validCategoryId);
        sampleStoreVO.setStatus(Store.StoreStatus.OPEN);
        sampleStoreVO.setRating(new BigDecimal("4.5"));
        sampleStoreVO.setMonthlySales(1000);
        sampleStoreVO.setHasPromotion(true);
        
        // 初始化店铺详情VO
        sampleStoreDetailVO = new StoreDetailVO();
        sampleStoreDetailVO.setId(validStoreId);
        sampleStoreDetailVO.setName("美味快餐店");
        sampleStoreDetailVO.setDescription("提供各种美味快餐");
        sampleStoreDetailVO.setRating(new BigDecimal("4.5"));
        sampleStoreDetailVO.setStatus(Store.StoreStatus.OPEN);
        
        // 商品分类列表
        FoodCategoryVO category1 = new FoodCategoryVO();
        category1.setId(1L);
        category1.setName("主食");
        category1.setSort(1);
        
        FoodCategoryVO category2 = new FoodCategoryVO();
        category2.setId(2L);
        category2.setName("饮品");
        category2.setSort(2);
        
        sampleStoreDetailVO.setCategories(Arrays.asList(category1, category2));
        
        // 特色商品列表
        FoodVO featuredFood = new FoodVO();
        featuredFood.setId(1L);
        featuredFood.setName("招牌汉堡");
        featuredFood.setPrice(new BigDecimal("25.00"));
        featuredFood.setDescription("店铺招牌美食");
        
        sampleStoreDetailVO.setFeaturedFoods(Arrays.asList(featuredFood));
        
        // 初始化商品VO
        sampleFoodVO = new FoodVO();
        sampleFoodVO.setId(validFoodId);
        sampleFoodVO.setName("美味汉堡");
        sampleFoodVO.setPrice(new BigDecimal("20.00"));
        sampleFoodVO.setDescription("香嫩多汁的汉堡");
        sampleFoodVO.setImage("https://example.com/burger.jpg");
        sampleFoodVO.setStoreId(validStoreId);
        sampleFoodVO.setCategoryId(1L);
        sampleFoodVO.setStatus(Food.FoodStatus.ON_SHELF);
        sampleFoodVO.setSales(500);
        
        // 初始化商品详情VO
        sampleFoodDetailVO = new FoodDetailVO();
        sampleFoodDetailVO.setId(validFoodId);
        sampleFoodDetailVO.setName("美味汉堡");
        sampleFoodDetailVO.setPrice(new BigDecimal("20.00"));
        sampleFoodDetailVO.setDescription("香嫩多汁的汉堡");
        
        // 评价列表
        ReviewVO review = new ReviewVO();
        review.setId(1L);
        review.setContent("很好吃的汉堡");
        review.setStoreRating(5);
        review.setUserId(1L);
        review.setUserName("张三");
        
        sampleFoodDetailVO.setReviews(Arrays.asList(review));
        
        // 初始化分页VO
        samplePageVO = new PageVO<>();
        samplePageVO.setContent(Arrays.asList(sampleStoreVO));
        samplePageVO.setTotalElements(1L);
        samplePageVO.setTotalPages(1);
        samplePageVO.setNumber(1);
        samplePageVO.setSize(10);
    }

    // ==================== listStores 测试 ====================

    /**
     * 正向测试：成功根据条件分页查询店铺列表
     */
    @Test
    void testListStores_Success() {
        // 模拟成功查询
        when(storeService.listStores(validStoreQueryDTO)).thenReturn(samplePageVO);
        
        // 执行测试
        PageVO<StoreVO> result = storeService.listStores(validStoreQueryDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(1L, result.getTotalElements(), "总元素数应为1");
        assertEquals(1, result.getTotalPages(), "总页数应为1");
        assertEquals(1, result.getNumber(), "当前页码应为1");
        assertFalse(result.getContent().isEmpty(), "内容列表不应为空");
        assertEquals("美味快餐店", result.getContent().get(0).getName(), "店铺名称应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).listStores(validStoreQueryDTO);
    }

    /**
     * 正向测试：按距离排序查询
     */
    @Test
    void testListStores_SortByDistance() {
        StoreQueryDTO distanceQueryDTO = new StoreQueryDTO();
        distanceQueryDTO.setSortBy(StoreQueryDTO.SearchSort.DISTANCE);
        distanceQueryDTO.setLongitude(116.307852);
        distanceQueryDTO.setLatitude(39.983424);
        distanceQueryDTO.setPage(1);
        distanceQueryDTO.setSize(10);
        
        // 模拟服务行为
        when(storeService.listStores(distanceQueryDTO)).thenReturn(samplePageVO);
        
        // 执行测试
        PageVO<StoreVO> result = storeService.listStores(distanceQueryDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertFalse(result.getContent().isEmpty(), "内容列表不应为空");
        
        // 验证方法被调用
        verify(storeService, times(1)).listStores(distanceQueryDTO);
    }

    /**
     * 正向测试：按评分排序查询
     */
    @Test
    void testListStores_SortByRating() {
        StoreQueryDTO ratingQueryDTO = new StoreQueryDTO();
        ratingQueryDTO.setSortBy(StoreQueryDTO.SearchSort.RATING);
        ratingQueryDTO.setPage(1);
        ratingQueryDTO.setSize(10);
        
        // 模拟服务行为
        when(storeService.listStores(ratingQueryDTO)).thenReturn(samplePageVO);
        
        // 执行测试
        PageVO<StoreVO> result = storeService.listStores(ratingQueryDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        
        // 验证方法被调用
        verify(storeService, times(1)).listStores(ratingQueryDTO);
    }

    /**
     * 正向测试：空结果分页查询
     */
    @Test
    void testListStores_EmptyResult() {
        PageVO<StoreVO> emptyPageVO = new PageVO<>();
        emptyPageVO.setContent(Collections.emptyList());
        emptyPageVO.setTotalElements(0L);
        emptyPageVO.setTotalPages(0);
        emptyPageVO.setNumber(1);
        emptyPageVO.setSize(10);
        
        // 模拟返回空结果
        when(storeService.listStores(validStoreQueryDTO)).thenReturn(emptyPageVO);
        
        // 执行测试
        PageVO<StoreVO> result = storeService.listStores(validStoreQueryDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertTrue(result.getContent().isEmpty(), "内容列表应为空");
        assertEquals(0L, result.getTotalElements(), "总元素数应为0");
        
        // 验证方法被调用
        verify(storeService, times(1)).listStores(validStoreQueryDTO);
    }

    /**
     * 反向测试：传入null的查询DTO
     */
    @Test
    void testListStores_NullQueryDTO() {
        // 模拟抛出异常
        when(storeService.listStores(null))
                .thenThrow(new IllegalArgumentException("查询条件不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeService.listStores(null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("查询条件不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).listStores(null);
    }

    /**
     * 反向测试：无效的页码
     */
    @Test
    void testListStores_InvalidPage() {
        StoreQueryDTO invalidPageDTO = new StoreQueryDTO();
        invalidPageDTO.setPage(0); // 无效页码
        invalidPageDTO.setSize(10);
        
        // 模拟抛出异常
        when(storeService.listStores(invalidPageDTO))
                .thenThrow(new IllegalArgumentException("页码必须大于0"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeService.listStores(invalidPageDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("页码必须大于0", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).listStores(invalidPageDTO);
    }

    // ==================== listRecommended 测试 ====================

    /**
     * 正向测试：成功获取推荐店铺列表（带位置信息）
     */
    @Test
    void testListRecommended_WithLocation() {
        Double longitude = 116.307852;
        Double latitude = 39.983424;
        
        List<StoreVO> recommendedStores = Arrays.asList(sampleStoreVO);
        
        // 模拟服务行为
        when(storeService.listRecommended(longitude, latitude)).thenReturn(recommendedStores);
        
        // 执行测试
        List<StoreVO> result = storeService.listRecommended(longitude, latitude);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertFalse(result.isEmpty(), "推荐列表不应为空");
        assertEquals(1, result.size(), "推荐列表大小应为1");
        assertEquals("美味快餐店", result.get(0).getName(), "店铺名称应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).listRecommended(longitude, latitude);
    }

    /**
     * 正向测试：成功获取推荐店铺列表（不带位置信息）
     */
    @Test
    void testListRecommended_WithoutLocation() {
        List<StoreVO> recommendedStores = Arrays.asList(sampleStoreVO);
        
        // 模拟服务行为
        when(storeService.listRecommended(null, null)).thenReturn(recommendedStores);
        
        // 执行测试
        List<StoreVO> result = storeService.listRecommended(null, null);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertFalse(result.isEmpty(), "推荐列表不应为空");
        
        // 验证方法被调用
        verify(storeService, times(1)).listRecommended(null, null);
    }

    /**
     * 正向测试：获取空的推荐列表
     */
    @Test
    void testListRecommended_EmptyList() {
        // 模拟返回空列表
        when(storeService.listRecommended(116.307852, 39.983424)).thenReturn(Collections.emptyList());
        
        // 执行测试
        List<StoreVO> result = storeService.listRecommended(116.307852, 39.983424);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertTrue(result.isEmpty(), "推荐列表应为空");
        
        // 验证方法被调用
        verify(storeService, times(1)).listRecommended(116.307852, 39.983424);
    }

    /**
     * 反向测试：无效的经纬度
     */
    @Test
    void testListRecommended_InvalidCoordinates() {
        Double invalidLongitude = 200.0; // 超出范围
        Double invalidLatitude = 100.0;  // 超出范围
        
        // 模拟抛出异常
        when(storeService.listRecommended(invalidLongitude, invalidLatitude))
                .thenThrow(new IllegalArgumentException("经纬度坐标无效"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeService.listRecommended(invalidLongitude, invalidLatitude),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("经纬度坐标无效", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).listRecommended(invalidLongitude, invalidLatitude);
    }

    // ==================== getStoreDetail 测试 ====================

    /**
     * 正向测试：成功获取店铺详细信息
     */
    @Test
    void testGetStoreDetail_Success() {
        // 模拟成功获取详情
        when(storeService.getStoreDetail(validStoreId)).thenReturn(sampleStoreDetailVO);
        
        // 执行测试
        StoreDetailVO result = storeService.getStoreDetail(validStoreId);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(validStoreId, result.getId(), "店铺ID应该匹配");
        assertEquals("美味快餐店", result.getName(), "店铺名称应该匹配");
        assertNotNull(result.getCategories(), "分类列表不应为null");
        assertEquals(2, result.getCategories().size(), "分类列表大小应为2");
        assertNotNull(result.getFeaturedFoods(), "特色商品列表不应为null");
        assertEquals(1, result.getFeaturedFoods().size(), "特色商品列表大小应为1");
        
        // 验证方法被调用
        verify(storeService, times(1)).getStoreDetail(validStoreId);
    }

    /**
     * 正向测试：获取无分类和特色商品的店铺详情
     */
    @Test
    void testGetStoreDetail_NoExtraInfo() {
        StoreDetailVO simpleStoreDetail = new StoreDetailVO();
        simpleStoreDetail.setId(validStoreId);
        simpleStoreDetail.setName("简单店铺");
        simpleStoreDetail.setCategories(Collections.emptyList());
        simpleStoreDetail.setFeaturedFoods(Collections.emptyList());
        
        // 模拟服务行为
        when(storeService.getStoreDetail(validStoreId)).thenReturn(simpleStoreDetail);
        
        // 执行测试
        StoreDetailVO result = storeService.getStoreDetail(validStoreId);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals("简单店铺", result.getName(), "店铺名称应该匹配");
        assertTrue(result.getCategories().isEmpty(), "分类列表应为空");
        assertTrue(result.getFeaturedFoods().isEmpty(), "特色商品列表应为空");
        
        // 验证方法被调用
        verify(storeService, times(1)).getStoreDetail(validStoreId);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testGetStoreDetail_NullStoreId() {
        // 模拟抛出异常
        when(storeService.getStoreDetail(null))
                .thenThrow(new IllegalArgumentException("店铺ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeService.getStoreDetail(null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).getStoreDetail(null);
    }

    /**
     * 反向测试：店铺不存在
     */
    @Test
    void testGetStoreDetail_StoreNotFound() {
        Long nonExistentStoreId = 999L;
        
        // 模拟抛出异常
        when(storeService.getStoreDetail(nonExistentStoreId))
                .thenThrow(new RuntimeException("店铺不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> storeService.getStoreDetail(nonExistentStoreId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).getStoreDetail(nonExistentStoreId);
    }

    // ==================== listStoreFoods 测试 ====================

    /**
     * 正向测试：成功获取店铺内商品列表（无分类筛选）
     */
    @Test
    void testListStoreFoods_WithoutCategory() {
        List<FoodVO> storeFoods = Arrays.asList(sampleFoodVO);
        
        // 模拟服务行为
        when(storeService.listStoreFoods(validStoreId, null)).thenReturn(storeFoods);
        
        // 执行测试
        List<FoodVO> result = storeService.listStoreFoods(validStoreId, null);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(1, result.size(), "商品列表大小应为1");
        assertEquals("美味汉堡", result.get(0).getName(), "商品名称应该匹配");
        assertEquals(validStoreId, result.get(0).getStoreId(), "店铺ID应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).listStoreFoods(validStoreId, null);
    }

    /**
     * 正向测试：成功获取店铺内商品列表（有分类筛选）
     */
    @Test
    void testListStoreFoods_WithCategory() {
        List<FoodVO> categoryFoods = Arrays.asList(sampleFoodVO);
        
        // 模拟服务行为
        when(storeService.listStoreFoods(validStoreId, validCategoryId)).thenReturn(categoryFoods);
        
        // 执行测试
        List<FoodVO> result = storeService.listStoreFoods(validStoreId, validCategoryId);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertFalse(result.isEmpty(), "商品列表不应为空");
        
        // 验证方法被调用
        verify(storeService, times(1)).listStoreFoods(validStoreId, validCategoryId);
    }

    /**
     * 正向测试：获取空的商品列表
     */
    @Test
    void testListStoreFoods_EmptyList() {
        // 模拟返回空列表
        when(storeService.listStoreFoods(validStoreId, validCategoryId)).thenReturn(Collections.emptyList());
        
        // 执行测试
        List<FoodVO> result = storeService.listStoreFoods(validStoreId, validCategoryId);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertTrue(result.isEmpty(), "商品列表应为空");
        
        // 验证方法被调用
        verify(storeService, times(1)).listStoreFoods(validStoreId, validCategoryId);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testListStoreFoods_NullStoreId() {
        // 模拟抛出异常
        when(storeService.listStoreFoods(null, validCategoryId))
                .thenThrow(new IllegalArgumentException("店铺ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeService.listStoreFoods(null, validCategoryId),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).listStoreFoods(null, validCategoryId);
    }

    /**
     * 反向测试：店铺不存在
     */
    @Test
    void testListStoreFoods_StoreNotFound() {
        Long nonExistentStoreId = 999L;
        
        // 模拟抛出异常
        when(storeService.listStoreFoods(nonExistentStoreId, validCategoryId))
                .thenThrow(new RuntimeException("店铺不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> storeService.listStoreFoods(nonExistentStoreId, validCategoryId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).listStoreFoods(nonExistentStoreId, validCategoryId);
    }

    // ==================== getFoodDetail 测试 ====================

    /**
     * 正向测试：成功获取商品详细信息
     */
    @Test
    void testGetFoodDetail_Success() {
        // 模拟成功获取详情
        when(storeService.getFoodDetail(validFoodId)).thenReturn(sampleFoodDetailVO);
        
        // 执行测试
        FoodDetailVO result = storeService.getFoodDetail(validFoodId);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(validFoodId, result.getId(), "商品ID应该匹配");
        assertEquals("美味汉堡", result.getName(), "商品名称应该匹配");
        assertNotNull(result.getReviews(), "评价列表不应为null");
        assertEquals(1, result.getReviews().size(), "评价列表大小应为1");
        assertEquals("很好吃的汉堡", result.getReviews().get(0).getContent(), "评价内容应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).getFoodDetail(validFoodId);
    }

    /**
     * 正向测试：获取无评价的商品详情
     */
    @Test
    void testGetFoodDetail_NoReviews() {
        FoodDetailVO noReviewFoodDetail = new FoodDetailVO();
        noReviewFoodDetail.setId(validFoodId);
        noReviewFoodDetail.setName("新商品");
        noReviewFoodDetail.setReviews(Collections.emptyList());
        
        // 模拟服务行为
        when(storeService.getFoodDetail(validFoodId)).thenReturn(noReviewFoodDetail);
        
        // 执行测试
        FoodDetailVO result = storeService.getFoodDetail(validFoodId);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals("新商品", result.getName(), "商品名称应该匹配");
        assertTrue(result.getReviews().isEmpty(), "评价列表应为空");
        
        // 验证方法被调用
        verify(storeService, times(1)).getFoodDetail(validFoodId);
    }

    /**
     * 反向测试：传入null的商品ID
     */
    @Test
    void testGetFoodDetail_NullFoodId() {
        // 模拟抛出异常
        when(storeService.getFoodDetail(null))
                .thenThrow(new IllegalArgumentException("商品ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeService.getFoodDetail(null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商品ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).getFoodDetail(null);
    }

    /**
     * 反向测试：商品不存在
     */
    @Test
    void testGetFoodDetail_FoodNotFound() {
        Long nonExistentFoodId = 999L;
        
        // 模拟抛出异常
        when(storeService.getFoodDetail(nonExistentFoodId))
                .thenThrow(new RuntimeException("商品不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> storeService.getFoodDetail(nonExistentFoodId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("商品不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).getFoodDetail(nonExistentFoodId);
    }

    // ==================== getStoreOwnerId 测试 ====================

    /**
     * 正向测试：成功获取店铺所有者ID
     */
    @Test
    void testGetStoreOwnerId_Success() {
        // 模拟成功获取所有者ID
        when(storeService.getStoreOwnerId(validStoreId)).thenReturn(validUserId);
        
        // 执行测试
        Long result = storeService.getStoreOwnerId(validStoreId);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(validUserId, result, "所有者ID应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).getStoreOwnerId(validStoreId);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testGetStoreOwnerId_NullStoreId() {
        // 模拟抛出异常
        when(storeService.getStoreOwnerId(null))
                .thenThrow(new IllegalArgumentException("店铺ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeService.getStoreOwnerId(null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).getStoreOwnerId(null);
    }

    /**
     * 反向测试：店铺不存在
     */
    @Test
    void testGetStoreOwnerId_StoreNotFound() {
        Long nonExistentStoreId = 999L;
        
        // 模拟抛出异常
        when(storeService.getStoreOwnerId(nonExistentStoreId))
                .thenThrow(new RuntimeException("店铺不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> storeService.getStoreOwnerId(nonExistentStoreId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).getStoreOwnerId(nonExistentStoreId);
    }

    // ==================== getStoreById 测试 ====================

    /**
     * 正向测试：成功根据ID获取店铺实体
     */
    @Test
    void testGetStoreById_Success() {
        Store sampleStore = new Store();
        sampleStore.setId(validStoreId);
        sampleStore.setName("美味快餐店");
        sampleStore.setStatus(Store.StoreStatus.OPEN);
        
        // 模拟成功获取店铺实体
        when(storeService.getStoreById(validStoreId)).thenReturn(sampleStore);
        
        // 执行测试
        Store result = storeService.getStoreById(validStoreId);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(validStoreId, result.getId(), "店铺ID应该匹配");
        assertEquals("美味快餐店", result.getName(), "店铺名称应该匹配");
        assertEquals(Store.StoreStatus.OPEN, result.getStatus(), "店铺状态应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).getStoreById(validStoreId);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testGetStoreById_NullStoreId() {
        // 模拟抛出异常
        when(storeService.getStoreById(null))
                .thenThrow(new IllegalArgumentException("店铺ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeService.getStoreById(null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).getStoreById(null);
    }

    /**
     * 反向测试：店铺不存在
     */
    @Test
    void testGetStoreById_StoreNotFound() {
        Long nonExistentStoreId = 999L;
        
        // 模拟抛出异常
        when(storeService.getStoreById(nonExistentStoreId))
                .thenThrow(new RuntimeException("店铺不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> storeService.getStoreById(nonExistentStoreId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).getStoreById(nonExistentStoreId);
    }

    // ==================== verifyStoreOwner 测试 ====================

    /**
     * 正向测试：成功验证店铺所有者
     */
    @Test
    void testVerifyStoreOwner_Success() {
        // 模拟成功验证
        doNothing().when(storeService).verifyStoreOwner(validStoreId, validUserId);
        
        // 执行测试
        assertDoesNotThrow(() -> storeService.verifyStoreOwner(validStoreId, validUserId), 
                "验证店铺所有者不应抛出异常");
        
        // 验证方法被调用
        verify(storeService, times(1)).verifyStoreOwner(validStoreId, validUserId);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testVerifyStoreOwner_NullStoreId() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("店铺ID不能为空"))
                .when(storeService).verifyStoreOwner(null, validUserId);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeService.verifyStoreOwner(null, validUserId),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).verifyStoreOwner(null, validUserId);
    }

    /**
     * 反向测试：传入null的用户ID
     */
    @Test
    void testVerifyStoreOwner_NullUserId() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("用户ID不能为空"))
                .when(storeService).verifyStoreOwner(validStoreId, null);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeService.verifyStoreOwner(validStoreId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("用户ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).verifyStoreOwner(validStoreId, null);
    }

    /**
     * 反向测试：用户不是店铺所有者
     */
    @Test
    void testVerifyStoreOwner_NotOwner() {
        Long nonOwnerUserId = 999L;
        
        // 模拟抛出异常
        doThrow(new RuntimeException("用户不是店铺所有者"))
                .when(storeService).verifyStoreOwner(validStoreId, nonOwnerUserId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> storeService.verifyStoreOwner(validStoreId, nonOwnerUserId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("用户不是店铺所有者", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).verifyStoreOwner(validStoreId, nonOwnerUserId);
    }

    // ==================== listStoreCategories 测试 ====================

    /**
     * 正向测试：成功获取店铺分类列表
     */
    @Test
    void testListStoreCategories_Success() {
        // 准备测试数据
        StoreCategoryVO category1 = new StoreCategoryVO();
        category1.setId(1L);
        category1.setName("快餐简餐");
        category1.setSort(1);
        category1.setIcon("https://example.com/fast-food.png");
        
        StoreCategoryVO category2 = new StoreCategoryVO();
        category2.setId(2L);
        category2.setName("中式料理");
        category2.setSort(2);
        category2.setIcon("https://example.com/chinese.png");
        
        List<StoreCategoryVO> categories = Arrays.asList(category1, category2);
        
        // 模拟服务行为
        when(storeService.listStoreCategories()).thenReturn(categories);
        
        // 执行测试
        List<StoreCategoryVO> result = storeService.listStoreCategories();
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(2, result.size(), "分类列表大小应为2");
        assertEquals("快餐简餐", result.get(0).getName(), "第一个分类名称应该匹配");
        assertEquals("中式料理", result.get(1).getName(), "第二个分类名称应该匹配");
        assertEquals(1, result.get(0).getSort(), "第一个分类排序应该匹配");
        assertEquals(2, result.get(1).getSort(), "第二个分类排序应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).listStoreCategories();
    }

    /**
     * 正向测试：获取空的分类列表
     */
    @Test
    void testListStoreCategories_EmptyList() {
        // 模拟返回空列表
        when(storeService.listStoreCategories()).thenReturn(Collections.emptyList());
        
        // 执行测试
        List<StoreCategoryVO> result = storeService.listStoreCategories();
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertTrue(result.isEmpty(), "分类列表应为空");
        
        // 验证方法被调用
        verify(storeService, times(1)).listStoreCategories();
    }

    /**
     * 正向测试：获取单个分类
     */
    @Test
    void testListStoreCategories_SingleCategory() {
        StoreCategoryVO singleCategory = new StoreCategoryVO();
        singleCategory.setId(1L);
        singleCategory.setName("特色美食");
        singleCategory.setSort(1);
        
        List<StoreCategoryVO> categories = Arrays.asList(singleCategory);
        
        // 模拟服务行为
        when(storeService.listStoreCategories()).thenReturn(categories);
        
        // 执行测试
        List<StoreCategoryVO> result = storeService.listStoreCategories();
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(1, result.size(), "分类列表大小应为1");
        assertEquals("特色美食", result.get(0).getName(), "分类名称应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).listStoreCategories();
    }

    /**
     * 反向测试：数据库查询异常
     */
    @Test
    void testListStoreCategories_DatabaseException() {
        // 模拟抛出数据库异常
        when(storeService.listStoreCategories())
                .thenThrow(new RuntimeException("数据库查询失败"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> storeService.listStoreCategories(),
                "应该抛出RuntimeException"
        );
        
        assertEquals("数据库查询失败", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(storeService, times(1)).listStoreCategories();
    }
}
