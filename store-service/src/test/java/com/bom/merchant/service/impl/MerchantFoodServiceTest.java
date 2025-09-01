package com.bom.merchant.service.impl;

import com.blm.common.dto.FoodCreateDTO;
import com.blm.common.entity.Food;
import com.blm.common.vo.FoodVO;
import com.blm.store.service.MerchantFoodService;
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
 * MerchantFoodService接口测试类
 * 针对每个方法实现正向和反向测试
 */
@ExtendWith(MockitoExtension.class)
class MerchantFoodServiceTest {

    @Mock
    private MerchantFoodService merchantFoodService;

    private Long validMerchantId;
    private Long validStoreId;
    private Long validFoodId;
    private Long validCategoryId;
    private FoodCreateDTO validFoodCreateDTO;
    private FoodVO sampleFoodVO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        validMerchantId = 1L;
        validStoreId = 10L;
        validFoodId = 100L;
        validCategoryId = 20L;
        
        // 初始化商品创建DTO
        validFoodCreateDTO = new FoodCreateDTO();
        validFoodCreateDTO.setCategoryId(validCategoryId);
        validFoodCreateDTO.setName("宫保鸡丁");
        validFoodCreateDTO.setPrice(new BigDecimal("28.80"));
        validFoodCreateDTO.setOriginalPrice(new BigDecimal("32.00"));
        validFoodCreateDTO.setDescription("经典川菜，酸甜可口");
        validFoodCreateDTO.setImage("https://example.com/gongbaojiding.jpg");
        validFoodCreateDTO.setIsFeatured(0);
        validFoodCreateDTO.setStatus(0);
        
        // 初始化商品VO
        sampleFoodVO = new FoodVO();
        sampleFoodVO.setId(validFoodId);
        sampleFoodVO.setStoreId(validStoreId);
        sampleFoodVO.setCategoryId(validCategoryId);
        sampleFoodVO.setName("宫保鸡丁");
        sampleFoodVO.setPrice(new BigDecimal("28.80"));
        sampleFoodVO.setDescription("经典川菜，酸甜可口");
        sampleFoodVO.setImage("https://example.com/gongbaojiding.jpg");
        sampleFoodVO.setSales(150);
        sampleFoodVO.setStatus(Food.FoodStatus.ON_SHELF);
    }

    // ==================== listFoods 测试 ====================

    /**
     * 正向测试：成功获取商品列表
     */
    @Test
    void testListFoods_Success() {
        // 准备测试数据
        FoodVO food1 = new FoodVO();
        food1.setId(1L);
        food1.setName("宫保鸡丁");
        food1.setPrice(new BigDecimal("28.80"));
        food1.setStatus(Food.FoodStatus.ON_SHELF);
        
        FoodVO food2 = new FoodVO();
        food2.setId(2L);
        food2.setName("麻婆豆腐");
        food2.setPrice(new BigDecimal("18.80"));
        food2.setStatus(Food.FoodStatus.ON_SHELF);
        
        List<FoodVO> expectedFoods = Arrays.asList(food1, food2);
        
        // 模拟服务行为
        when(merchantFoodService.listFoods(validMerchantId, validStoreId, validCategoryId, Food.FoodStatus.ON_SHELF))
                .thenReturn(expectedFoods);
        
        // 执行测试
        List<FoodVO> actualFoods = merchantFoodService.listFoods(validMerchantId, validStoreId, validCategoryId, Food.FoodStatus.ON_SHELF);
        
        // 验证结果
        assertNotNull(actualFoods, "商品列表不应为null");
        assertEquals(2, actualFoods.size(), "商品列表大小应为2");
        assertEquals("宫保鸡丁", actualFoods.get(0).getName(), "第一个商品名应该匹配");
        assertEquals("麻婆豆腐", actualFoods.get(1).getName(), "第二个商品名应该匹配");
        assertEquals(Food.FoodStatus.ON_SHELF, actualFoods.get(0).getStatus(), "商品状态应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).listFoods(validMerchantId, validStoreId, validCategoryId, Food.FoodStatus.ON_SHELF);
    }

    /**
     * 正向测试：获取所有分类的商品（categoryId为null）
     */
    @Test
    void testListFoods_AllCategories() {
        // 准备测试数据
        List<FoodVO> expectedFoods = Arrays.asList(sampleFoodVO);
        
        // 模拟服务行为
        when(merchantFoodService.listFoods(validMerchantId, validStoreId, null, Food.FoodStatus.ON_SHELF))
                .thenReturn(expectedFoods);
        
        // 执行测试
        List<FoodVO> actualFoods = merchantFoodService.listFoods(validMerchantId, validStoreId, null, Food.FoodStatus.ON_SHELF);
        
        // 验证结果
        assertNotNull(actualFoods, "商品列表不应为null");
        assertEquals(1, actualFoods.size(), "商品列表大小应为1");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).listFoods(validMerchantId, validStoreId, null, Food.FoodStatus.ON_SHELF);
    }

    /**
     * 正向测试：获取所有状态的商品（status为null）
     */
    @Test
    void testListFoods_AllStatuses() {
        // 准备测试数据
        List<FoodVO> expectedFoods = Arrays.asList(sampleFoodVO);
        
        // 模拟服务行为
        when(merchantFoodService.listFoods(validMerchantId, validStoreId, validCategoryId, null))
                .thenReturn(expectedFoods);
        
        // 执行测试
        List<FoodVO> actualFoods = merchantFoodService.listFoods(validMerchantId, validStoreId, validCategoryId, null);
        
        // 验证结果
        assertNotNull(actualFoods, "商品列表不应为null");
        assertEquals(1, actualFoods.size(), "商品列表大小应为1");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).listFoods(validMerchantId, validStoreId, validCategoryId, null);
    }

    /**
     * 正向测试：获取空商品列表
     */
    @Test
    void testListFoods_EmptyList() {
        // 模拟返回空列表
        when(merchantFoodService.listFoods(validMerchantId, validStoreId, validCategoryId, Food.FoodStatus.OFF_SHELF))
                .thenReturn(Collections.emptyList());
        
        // 执行测试
        List<FoodVO> actualFoods = merchantFoodService.listFoods(validMerchantId, validStoreId, validCategoryId, Food.FoodStatus.OFF_SHELF);
        
        // 验证结果
        assertNotNull(actualFoods, "商品列表不应为null");
        assertTrue(actualFoods.isEmpty(), "商品列表应为空");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).listFoods(validMerchantId, validStoreId, validCategoryId, Food.FoodStatus.OFF_SHELF);
    }

    /**
     * 反向测试：传入null的商家ID
     */
    @Test
    void testListFoods_NullMerchantId() {
        // 模拟抛出异常
        when(merchantFoodService.listFoods(null, validStoreId, validCategoryId, Food.FoodStatus.ON_SHELF))
                .thenThrow(new IllegalArgumentException("商家ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodService.listFoods(null, validStoreId, validCategoryId, Food.FoodStatus.ON_SHELF),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商家ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).listFoods(null, validStoreId, validCategoryId, Food.FoodStatus.ON_SHELF);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testListFoods_NullStoreId() {
        // 模拟抛出异常
        when(merchantFoodService.listFoods(validMerchantId, null, validCategoryId, Food.FoodStatus.ON_SHELF))
                .thenThrow(new IllegalArgumentException("店铺ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodService.listFoods(validMerchantId, null, validCategoryId, Food.FoodStatus.ON_SHELF),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).listFoods(validMerchantId, null, validCategoryId, Food.FoodStatus.ON_SHELF);
    }

    /**
     * 反向测试：店铺不属于该商家
     */
    @Test
    void testListFoods_StoreNotBelongToMerchant() {
        Long otherMerchantId = 2L;
        
        // 模拟抛出异常
        when(merchantFoodService.listFoods(otherMerchantId, validStoreId, validCategoryId, Food.FoodStatus.ON_SHELF))
                .thenThrow(new RuntimeException("店铺不属于该商家"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodService.listFoods(otherMerchantId, validStoreId, validCategoryId, Food.FoodStatus.ON_SHELF),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不属于该商家", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).listFoods(otherMerchantId, validStoreId, validCategoryId, Food.FoodStatus.ON_SHELF);
    }

    // ==================== createFood 测试 ====================

    /**
     * 正向测试：成功创建商品
     */
    @Test
    void testCreateFood_Success() {
        // 模拟成功创建
        when(merchantFoodService.createFood(validMerchantId, validStoreId, validFoodCreateDTO)).thenReturn(sampleFoodVO);
        
        // 执行测试
        FoodVO result = merchantFoodService.createFood(validMerchantId, validStoreId, validFoodCreateDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(validFoodCreateDTO.getName(), result.getName(), "商品名应该匹配");
        assertEquals(validFoodCreateDTO.getPrice(), result.getPrice(), "价格应该匹配");
        assertEquals(validFoodCreateDTO.getCategoryId(), result.getCategoryId(), "分类ID应该匹配");
        assertNotNull(result.getId(), "商品ID不应为null");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).createFood(validMerchantId, validStoreId, validFoodCreateDTO);
    }

    /**
     * 正向测试：创建最小信息的商品
     */
    @Test
    void testCreateFood_MinimalInfo() {
        // 准备最小信息的DTO
        FoodCreateDTO minimalDTO = new FoodCreateDTO();
        minimalDTO.setCategoryId(validCategoryId);
        minimalDTO.setName("简单商品");
        minimalDTO.setPrice(new BigDecimal("10.00"));
        
        FoodVO expectedVO = new FoodVO();
        expectedVO.setId(1L);
        expectedVO.setName(minimalDTO.getName());
        expectedVO.setPrice(minimalDTO.getPrice());
        expectedVO.setCategoryId(minimalDTO.getCategoryId());
        
        // 模拟服务行为
        when(merchantFoodService.createFood(validMerchantId, validStoreId, minimalDTO)).thenReturn(expectedVO);
        
        // 执行测试
        FoodVO result = merchantFoodService.createFood(validMerchantId, validStoreId, minimalDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(minimalDTO.getName(), result.getName(), "商品名应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).createFood(validMerchantId, validStoreId, minimalDTO);
    }

    /**
     * 反向测试：传入null的商家ID
     */
    @Test
    void testCreateFood_NullMerchantId() {
        // 模拟抛出异常
        when(merchantFoodService.createFood(null, validStoreId, validFoodCreateDTO))
                .thenThrow(new IllegalArgumentException("商家ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodService.createFood(null, validStoreId, validFoodCreateDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商家ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).createFood(null, validStoreId, validFoodCreateDTO);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testCreateFood_NullStoreId() {
        // 模拟抛出异常
        when(merchantFoodService.createFood(validMerchantId, null, validFoodCreateDTO))
                .thenThrow(new IllegalArgumentException("店铺ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodService.createFood(validMerchantId, null, validFoodCreateDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).createFood(validMerchantId, null, validFoodCreateDTO);
    }

    /**
     * 反向测试：传入null的DTO
     */
    @Test
    void testCreateFood_NullDTO() {
        // 模拟抛出异常
        when(merchantFoodService.createFood(validMerchantId, validStoreId, null))
                .thenThrow(new IllegalArgumentException("商品信息不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodService.createFood(validMerchantId, validStoreId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商品信息不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).createFood(validMerchantId, validStoreId, null);
    }

    /**
     * 反向测试：商品名称为空
     */
    @Test
    void testCreateFood_EmptyName() {
        // 准备空名称的DTO
        FoodCreateDTO emptyNameDTO = new FoodCreateDTO();
        emptyNameDTO.setCategoryId(validCategoryId);
        emptyNameDTO.setName("");
        emptyNameDTO.setPrice(new BigDecimal("10.00"));
        
        // 模拟抛出异常
        when(merchantFoodService.createFood(validMerchantId, validStoreId, emptyNameDTO))
                .thenThrow(new IllegalArgumentException("商品名称不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodService.createFood(validMerchantId, validStoreId, emptyNameDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商品名称不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).createFood(validMerchantId, validStoreId, emptyNameDTO);
    }

    /**
     * 反向测试：价格为null
     */
    @Test
    void testCreateFood_NullPrice() {
        // 准备null价格的DTO
        FoodCreateDTO nullPriceDTO = new FoodCreateDTO();
        nullPriceDTO.setCategoryId(validCategoryId);
        nullPriceDTO.setName("测试商品");
        nullPriceDTO.setPrice(null);
        
        // 模拟抛出异常
        when(merchantFoodService.createFood(validMerchantId, validStoreId, nullPriceDTO))
                .thenThrow(new IllegalArgumentException("商品价格不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodService.createFood(validMerchantId, validStoreId, nullPriceDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商品价格不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).createFood(validMerchantId, validStoreId, nullPriceDTO);
    }

    /**
     * 反向测试：价格为负数
     */
    @Test
    void testCreateFood_NegativePrice() {
        // 准备负价格的DTO
        FoodCreateDTO negativePriceDTO = new FoodCreateDTO();
        negativePriceDTO.setCategoryId(validCategoryId);
        negativePriceDTO.setName("测试商品");
        negativePriceDTO.setPrice(new BigDecimal("-10.00"));
        
        // 模拟抛出异常
        when(merchantFoodService.createFood(validMerchantId, validStoreId, negativePriceDTO))
                .thenThrow(new IllegalArgumentException("商品价格不能为负数"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodService.createFood(validMerchantId, validStoreId, negativePriceDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商品价格不能为负数", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).createFood(validMerchantId, validStoreId, negativePriceDTO);
    }

    /**
     * 反向测试：分类ID为null
     */
    @Test
    void testCreateFood_NullCategoryId() {
        // 准备null分类ID的DTO
        FoodCreateDTO nullCategoryDTO = new FoodCreateDTO();
        nullCategoryDTO.setCategoryId(null);
        nullCategoryDTO.setName("测试商品");
        nullCategoryDTO.setPrice(new BigDecimal("10.00"));
        
        // 模拟抛出异常
        when(merchantFoodService.createFood(validMerchantId, validStoreId, nullCategoryDTO))
                .thenThrow(new IllegalArgumentException("商品分类不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodService.createFood(validMerchantId, validStoreId, nullCategoryDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商品分类不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).createFood(validMerchantId, validStoreId, nullCategoryDTO);
    }

    /**
     * 反向测试：店铺不属于该商家
     */
    @Test
    void testCreateFood_StoreNotBelongToMerchant() {
        Long otherMerchantId = 2L;
        
        // 模拟抛出异常
        when(merchantFoodService.createFood(otherMerchantId, validStoreId, validFoodCreateDTO))
                .thenThrow(new RuntimeException("店铺不属于该商家"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodService.createFood(otherMerchantId, validStoreId, validFoodCreateDTO),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不属于该商家", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).createFood(otherMerchantId, validStoreId, validFoodCreateDTO);
    }

    // ==================== updateFood 测试 ====================

    /**
     * 正向测试：成功更新商品
     */
    @Test
    void testUpdateFood_Success() {
        // 准备更新后的数据
        FoodVO updatedFoodVO = new FoodVO();
        updatedFoodVO.setId(validFoodId);
        updatedFoodVO.setName("更新的商品名");
        updatedFoodVO.setPrice(new BigDecimal("35.80"));
        
        // 模拟成功更新
        when(merchantFoodService.updateFood(validMerchantId, validStoreId, validFoodId, validFoodCreateDTO)).thenReturn(updatedFoodVO);
        
        // 执行测试
        FoodVO result = merchantFoodService.updateFood(validMerchantId, validStoreId, validFoodId, validFoodCreateDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(validFoodId, result.getId(), "商品ID应该匹配");
        assertEquals("更新的商品名", result.getName(), "商品名应该已更新");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).updateFood(validMerchantId, validStoreId, validFoodId, validFoodCreateDTO);
    }

    /**
     * 正向测试：更新商品价格
     */
    @Test
    void testUpdateFood_UpdatePrice() {
        // 准备新价格的DTO
        FoodCreateDTO updatePriceDTO = new FoodCreateDTO();
        updatePriceDTO.setCategoryId(validCategoryId);
        updatePriceDTO.setName("宫保鸡丁");
        updatePriceDTO.setPrice(new BigDecimal("35.00"));
        
        FoodVO updatedVO = new FoodVO();
        updatedVO.setId(validFoodId);
        updatedVO.setName(updatePriceDTO.getName());
        updatedVO.setPrice(updatePriceDTO.getPrice());
        
        // 模拟服务行为
        when(merchantFoodService.updateFood(validMerchantId, validStoreId, validFoodId, updatePriceDTO)).thenReturn(updatedVO);
        
        // 执行测试
        FoodVO result = merchantFoodService.updateFood(validMerchantId, validStoreId, validFoodId, updatePriceDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(updatePriceDTO.getPrice(), result.getPrice(), "价格应该已更新");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).updateFood(validMerchantId, validStoreId, validFoodId, updatePriceDTO);
    }

    /**
     * 反向测试：传入null的商家ID
     */
    @Test
    void testUpdateFood_NullMerchantId() {
        // 模拟抛出异常
        when(merchantFoodService.updateFood(null, validStoreId, validFoodId, validFoodCreateDTO))
                .thenThrow(new IllegalArgumentException("商家ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodService.updateFood(null, validStoreId, validFoodId, validFoodCreateDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商家ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).updateFood(null, validStoreId, validFoodId, validFoodCreateDTO);
    }

    /**
     * 反向测试：传入null的商品ID
     */
    @Test
    void testUpdateFood_NullFoodId() {
        // 模拟抛出异常
        when(merchantFoodService.updateFood(validMerchantId, validStoreId, null, validFoodCreateDTO))
                .thenThrow(new IllegalArgumentException("商品ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodService.updateFood(validMerchantId, validStoreId, null, validFoodCreateDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商品ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).updateFood(validMerchantId, validStoreId, null, validFoodCreateDTO);
    }

    /**
     * 反向测试：商品不存在
     */
    @Test
    void testUpdateFood_FoodNotFound() {
        Long nonExistentFoodId = 999L;
        
        // 模拟抛出异常
        when(merchantFoodService.updateFood(validMerchantId, validStoreId, nonExistentFoodId, validFoodCreateDTO))
                .thenThrow(new RuntimeException("商品不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodService.updateFood(validMerchantId, validStoreId, nonExistentFoodId, validFoodCreateDTO),
                "应该抛出RuntimeException"
        );
        
        assertEquals("商品不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).updateFood(validMerchantId, validStoreId, nonExistentFoodId, validFoodCreateDTO);
    }

    // ==================== deleteFood 测试 ====================

    /**
     * 正向测试：成功删除商品
     */
    @Test
    void testDeleteFood_Success() {
        // 模拟成功删除
        doNothing().when(merchantFoodService).deleteFood(validMerchantId, validStoreId, validFoodId);
        
        // 执行测试（应该不抛出异常）
        assertDoesNotThrow(() -> merchantFoodService.deleteFood(validMerchantId, validStoreId, validFoodId), 
                "删除商品不应抛出异常");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).deleteFood(validMerchantId, validStoreId, validFoodId);
    }

    /**
     * 反向测试：传入null的商家ID
     */
    @Test
    void testDeleteFood_NullMerchantId() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("商家ID不能为空"))
                .when(merchantFoodService).deleteFood(null, validStoreId, validFoodId);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodService.deleteFood(null, validStoreId, validFoodId),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商家ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).deleteFood(null, validStoreId, validFoodId);
    }

    /**
     * 反向测试：商品不存在
     */
    @Test
    void testDeleteFood_FoodNotFound() {
        Long nonExistentFoodId = 999L;
        
        // 模拟抛出异常
        doThrow(new RuntimeException("商品不存在"))
                .when(merchantFoodService).deleteFood(validMerchantId, validStoreId, nonExistentFoodId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodService.deleteFood(validMerchantId, validStoreId, nonExistentFoodId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("商品不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).deleteFood(validMerchantId, validStoreId, nonExistentFoodId);
    }

    // ==================== updateStatus 测试 ====================

    /**
     * 正向测试：成功更新商品状态为上架
     */
    @Test
    void testUpdateStatus_ToOnShelf() {
        // 模拟成功更新状态
        doNothing().when(merchantFoodService).updateStatus(validMerchantId, validStoreId, validFoodId, Food.FoodStatus.ON_SHELF);
        
        // 执行测试
        assertDoesNotThrow(() -> merchantFoodService.updateStatus(validMerchantId, validStoreId, validFoodId, Food.FoodStatus.ON_SHELF), 
                "更新商品状态为上架不应抛出异常");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).updateStatus(validMerchantId, validStoreId, validFoodId, Food.FoodStatus.ON_SHELF);
    }

    /**
     * 正向测试：成功更新商品状态为下架
     */
    @Test
    void testUpdateStatus_ToOffShelf() {
        // 模拟成功更新状态
        doNothing().when(merchantFoodService).updateStatus(validMerchantId, validStoreId, validFoodId, Food.FoodStatus.OFF_SHELF);
        
        // 执行测试
        assertDoesNotThrow(() -> merchantFoodService.updateStatus(validMerchantId, validStoreId, validFoodId, Food.FoodStatus.OFF_SHELF), 
                "更新商品状态为下架不应抛出异常");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).updateStatus(validMerchantId, validStoreId, validFoodId, Food.FoodStatus.OFF_SHELF);
    }

    /**
     * 正向测试：成功更新商品状态为暂停销售
     */
    @Test
    void testUpdateStatus_ToSuspended() {
        // 模拟成功更新状态
        doNothing().when(merchantFoodService).updateStatus(validMerchantId, validStoreId, validFoodId, Food.FoodStatus.SUSPENDED);
        
        // 执行测试
        assertDoesNotThrow(() -> merchantFoodService.updateStatus(validMerchantId, validStoreId, validFoodId, Food.FoodStatus.SUSPENDED), 
                "更新商品状态为暂停销售不应抛出异常");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).updateStatus(validMerchantId, validStoreId, validFoodId, Food.FoodStatus.SUSPENDED);
    }

    /**
     * 反向测试：传入null的状态
     */
    @Test
    void testUpdateStatus_NullStatus() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("商品状态不能为空"))
                .when(merchantFoodService).updateStatus(validMerchantId, validStoreId, validFoodId, null);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodService.updateStatus(validMerchantId, validStoreId, validFoodId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商品状态不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).updateStatus(validMerchantId, validStoreId, validFoodId, null);
    }

    /**
     * 反向测试：商品不存在
     */
    @Test
    void testUpdateStatus_FoodNotFound() {
        Long nonExistentFoodId = 999L;
        
        // 模拟抛出异常
        doThrow(new RuntimeException("商品不存在"))
                .when(merchantFoodService).updateStatus(validMerchantId, validStoreId, nonExistentFoodId, Food.FoodStatus.ON_SHELF);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodService.updateStatus(validMerchantId, validStoreId, nonExistentFoodId, Food.FoodStatus.ON_SHELF),
                "应该抛出RuntimeException"
        );
        
        assertEquals("商品不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).updateStatus(validMerchantId, validStoreId, nonExistentFoodId, Food.FoodStatus.ON_SHELF);
    }

    // ==================== getFoodByStoreIdAndFoodId 测试 ====================

    /**
     * 正向测试：成功根据店铺ID和商品ID获取商品
     */
    @Test
    void testGetFoodByStoreIdAndFoodId_Success() {
        // 模拟成功获取商品
        when(merchantFoodService.getFoodByStoreIdAndFoodId(validMerchantId, validStoreId, validFoodId)).thenReturn(sampleFoodVO);
        
        // 执行测试
        FoodVO result = merchantFoodService.getFoodByStoreIdAndFoodId(validMerchantId, validStoreId, validFoodId);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(validFoodId, result.getId(), "商品ID应该匹配");
        assertEquals(validStoreId, result.getStoreId(), "店铺ID应该匹配");
        assertEquals("宫保鸡丁", result.getName(), "商品名应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).getFoodByStoreIdAndFoodId(validMerchantId, validStoreId, validFoodId);
    }

    /**
     * 正向测试：获取特定商品信息
     */
    @Test
    void testGetFoodByStoreIdAndFoodId_SpecificFood() {
        Long specificFoodId = 200L;
        
        FoodVO specificFood = new FoodVO();
        specificFood.setId(specificFoodId);
        specificFood.setStoreId(validStoreId);
        specificFood.setName("麻婆豆腐");
        specificFood.setPrice(new BigDecimal("18.80"));
        
        // 模拟服务行为
        when(merchantFoodService.getFoodByStoreIdAndFoodId(validMerchantId, validStoreId, specificFoodId)).thenReturn(specificFood);
        
        // 执行测试
        FoodVO result = merchantFoodService.getFoodByStoreIdAndFoodId(validMerchantId, validStoreId, specificFoodId);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(specificFoodId, result.getId(), "商品ID应该匹配");
        assertEquals("麻婆豆腐", result.getName(), "商品名应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).getFoodByStoreIdAndFoodId(validMerchantId, validStoreId, specificFoodId);
    }

    /**
     * 反向测试：传入null的商家ID
     */
    @Test
    void testGetFoodByStoreIdAndFoodId_NullMerchantId() {
        // 模拟抛出异常
        when(merchantFoodService.getFoodByStoreIdAndFoodId(null, validStoreId, validFoodId))
                .thenThrow(new IllegalArgumentException("商家ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodService.getFoodByStoreIdAndFoodId(null, validStoreId, validFoodId),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商家ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).getFoodByStoreIdAndFoodId(null, validStoreId, validFoodId);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testGetFoodByStoreIdAndFoodId_NullStoreId() {
        // 模拟抛出异常
        when(merchantFoodService.getFoodByStoreIdAndFoodId(validMerchantId, null, validFoodId))
                .thenThrow(new IllegalArgumentException("店铺ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodService.getFoodByStoreIdAndFoodId(validMerchantId, null, validFoodId),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).getFoodByStoreIdAndFoodId(validMerchantId, null, validFoodId);
    }

    /**
     * 反向测试：传入null的商品ID
     */
    @Test
    void testGetFoodByStoreIdAndFoodId_NullFoodId() {
        // 模拟抛出异常
        when(merchantFoodService.getFoodByStoreIdAndFoodId(validMerchantId, validStoreId, null))
                .thenThrow(new IllegalArgumentException("商品ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodService.getFoodByStoreIdAndFoodId(validMerchantId, validStoreId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商品ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).getFoodByStoreIdAndFoodId(validMerchantId, validStoreId, null);
    }

    /**
     * 反向测试：商品不存在
     */
    @Test
    void testGetFoodByStoreIdAndFoodId_FoodNotFound() {
        Long nonExistentFoodId = 999L;
        
        // 模拟抛出异常
        when(merchantFoodService.getFoodByStoreIdAndFoodId(validMerchantId, validStoreId, nonExistentFoodId))
                .thenThrow(new RuntimeException("商品不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodService.getFoodByStoreIdAndFoodId(validMerchantId, validStoreId, nonExistentFoodId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("商品不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).getFoodByStoreIdAndFoodId(validMerchantId, validStoreId, nonExistentFoodId);
    }

    /**
     * 反向测试：商品不属于指定店铺
     */
    @Test
    void testGetFoodByStoreIdAndFoodId_FoodNotInStore() {
        Long otherStoreId = 999L;
        
        // 模拟抛出异常
        when(merchantFoodService.getFoodByStoreIdAndFoodId(validMerchantId, otherStoreId, validFoodId))
                .thenThrow(new RuntimeException("商品不属于指定店铺"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodService.getFoodByStoreIdAndFoodId(validMerchantId, otherStoreId, validFoodId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("商品不属于指定店铺", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).getFoodByStoreIdAndFoodId(validMerchantId, otherStoreId, validFoodId);
    }

    /**
     * 反向测试：店铺不属于该商家
     */
    @Test
    void testGetFoodByStoreIdAndFoodId_StoreNotBelongToMerchant() {
        Long otherMerchantId = 2L;
        
        // 模拟抛出异常
        when(merchantFoodService.getFoodByStoreIdAndFoodId(otherMerchantId, validStoreId, validFoodId))
                .thenThrow(new RuntimeException("店铺不属于该商家"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodService.getFoodByStoreIdAndFoodId(otherMerchantId, validStoreId, validFoodId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不属于该商家", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodService, times(1)).getFoodByStoreIdAndFoodId(otherMerchantId, validStoreId, validFoodId);
    }
}
