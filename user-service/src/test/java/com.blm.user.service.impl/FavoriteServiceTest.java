package com.blm.user.service.impl;

import com.blm.common.vo.FoodVO;
import com.blm.common.vo.PageVO;
import com.blm.common.vo.StoreVO;
import com.blm.user.service.FavoriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * FavoriteService 单元测试
 * 测试收藏服务的所有方法
 */
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        // 在每个测试方法执行前的准备工作
    }

    // ==================== listFavoriteStores() 方法测试 ====================

    /**
     * 正向测试：成功获取收藏店铺列表
     */
    @Test
    void testListFavoriteStores_Success() {
        // 准备测试数据
        Long userId = 1L;
        int page = 1;
        int size = 10;
        
        StoreVO storeVO1 = new StoreVO();
        storeVO1.setId(1L);
        storeVO1.setName("美味餐厅");
        storeVO1.setDescription("精致美味的中式餐厅");
        
        StoreVO storeVO2 = new StoreVO();
        storeVO2.setId(2L);
        storeVO2.setName("快乐小吃");
        storeVO2.setDescription("快乐美味的街头小吃");
        
        PageVO<StoreVO> expectedPage = new PageVO<>();
        expectedPage.setContent(Arrays.asList(storeVO1, storeVO2));
        expectedPage.setTotalElements(2L);
        expectedPage.setNumber(1);
        expectedPage.setSize(10);
        
        // 模拟服务行为
        when(favoriteService.listFavoriteStores(userId, page, size)).thenReturn(expectedPage);
        
        // 执行测试
        PageVO<StoreVO> actualPage = favoriteService.listFavoriteStores(userId, page, size);
        
        // 验证结果
        assertNotNull(actualPage, "分页结果不应为null");
        assertNotNull(actualPage.getContent(), "记录列表不应为null");
        assertEquals(2, actualPage.getContent().size(), "记录数量应为2");
        assertEquals(2L, actualPage.getTotalElements(), "总数应为2");
        assertEquals(1, actualPage.getNumber(), "当前页应为1");
        assertEquals(10, actualPage.getSize(), "页大小应为10");
        assertEquals("美味餐厅", actualPage.getContent().get(0).getName(), "第一个店铺名应该匹配");
        assertEquals("快乐小吃", actualPage.getContent().get(1).getName(), "第二个店铺名应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).listFavoriteStores(userId, page, size);
    }

    /**
     * 正向测试：获取空的收藏店铺列表
     */
    @Test
    void testListFavoriteStores_EmptyList() {
        // 准备测试数据
        Long userId = 1L;
        int page = 1;
        int size = 10;
        
        PageVO<StoreVO> expectedPage = new PageVO<>();
        expectedPage.setContent(Collections.emptyList());
        expectedPage.setTotalElements(0L);
        expectedPage.setNumber(1);
        expectedPage.setSize(10);
        
        // 模拟服务行为
        when(favoriteService.listFavoriteStores(userId, page, size)).thenReturn(expectedPage);
        
        // 执行测试
        PageVO<StoreVO> actualPage = favoriteService.listFavoriteStores(userId, page, size);
        
        // 验证结果
        assertNotNull(actualPage, "分页结果不应为null");
        assertNotNull(actualPage.getContent(), "记录列表不应为null");
        assertTrue(actualPage.getContent().isEmpty(), "记录列表应为空");
        assertEquals(0L, actualPage.getTotalElements(), "总数应为0");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).listFavoriteStores(userId, page, size);
    }

    /**
     * 反向测试：userId为null
     */
    @Test
    void testListFavoriteStores_NullUserId() {
        // 准备测试数据
        Long userId = null;
        int page = 1;
        int size = 10;
        
        // 模拟服务抛出异常
        when(favoriteService.listFavoriteStores(userId, page, size))
                .thenThrow(new IllegalArgumentException("用户ID不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            favoriteService.listFavoriteStores(userId, page, size);
        });
        
        assertEquals("用户ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).listFavoriteStores(userId, page, size);
    }

    /**
     * 反向测试：页码为负数
     */
    @Test
    void testListFavoriteStores_NegativePage() {
        // 准备测试数据
        Long userId = 1L;
        int page = -1;
        int size = 10;
        
        // 模拟服务抛出异常
        when(favoriteService.listFavoriteStores(userId, page, size))
                .thenThrow(new IllegalArgumentException("页码必须为正数"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            favoriteService.listFavoriteStores(userId, page, size);
        });
        
        assertEquals("页码必须为正数", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).listFavoriteStores(userId, page, size);
    }

    /**
     * 反向测试：页大小为0或负数
     */
    @Test
    void testListFavoriteStores_InvalidSize() {
        // 准备测试数据
        Long userId = 1L;
        int page = 1;
        int size = 0;
        
        // 模拟服务抛出异常
        when(favoriteService.listFavoriteStores(userId, page, size))
                .thenThrow(new IllegalArgumentException("页大小必须为正数"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            favoriteService.listFavoriteStores(userId, page, size);
        });
        
        assertEquals("页大小必须为正数", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).listFavoriteStores(userId, page, size);
    }

    // ==================== addStoreFavorite() 方法测试 ====================

    /**
     * 正向测试：成功添加店铺收藏
     */
    @Test
    void testAddStoreFavorite_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long storeId = 100L;
        Integer expectedResult = 1; // 添加成功
        
        // 模拟服务行为
        when(favoriteService.addStoreFavorite(userId, storeId)).thenReturn(expectedResult);
        
        // 执行测试
        Integer actualResult = favoriteService.addStoreFavorite(userId, storeId);
        
        // 验证结果
        assertEquals(expectedResult, actualResult, "返回结果应该匹配");
        assertEquals(1, actualResult, "添加成功应该返回1");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).addStoreFavorite(userId, storeId);
    }

    /**
     * 正向测试：重复添加店铺收藏
     */
    @Test
    void testAddStoreFavorite_AlreadyExists() {
        // 准备测试数据
        Long userId = 1L;
        Long storeId = 100L;
        Integer expectedResult = 0; // 已存在，未添加
        
        // 模拟服务行为
        when(favoriteService.addStoreFavorite(userId, storeId)).thenReturn(expectedResult);
        
        // 执行测试
        Integer actualResult = favoriteService.addStoreFavorite(userId, storeId);
        
        // 验证结果
        assertEquals(expectedResult, actualResult, "返回结果应该匹配");
        assertEquals(0, actualResult, "重复添加应该返回0");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).addStoreFavorite(userId, storeId);
    }

    /**
     * 反向测试：userId为null
     */
    @Test
    void testAddStoreFavorite_NullUserId() {
        // 准备测试数据
        Long userId = null;
        Long storeId = 100L;
        
        // 模拟服务抛出异常
        when(favoriteService.addStoreFavorite(userId, storeId))
                .thenThrow(new IllegalArgumentException("用户ID不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            favoriteService.addStoreFavorite(userId, storeId);
        });
        
        assertEquals("用户ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).addStoreFavorite(userId, storeId);
    }

    /**
     * 反向测试：storeId为null
     */
    @Test
    void testAddStoreFavorite_NullStoreId() {
        // 准备测试数据
        Long userId = 1L;
        Long storeId = null;
        
        // 模拟服务抛出异常
        when(favoriteService.addStoreFavorite(userId, storeId))
                .thenThrow(new IllegalArgumentException("店铺ID不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            favoriteService.addStoreFavorite(userId, storeId);
        });
        
        assertEquals("店铺ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).addStoreFavorite(userId, storeId);
    }

    /**
     * 反向测试：店铺不存在
     */
    @Test
    void testAddStoreFavorite_StoreNotFound() {
        // 准备测试数据
        Long userId = 1L;
        Long storeId = 999L;
        
        // 模拟服务抛出异常
        when(favoriteService.addStoreFavorite(userId, storeId))
                .thenThrow(new RuntimeException("店铺不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            favoriteService.addStoreFavorite(userId, storeId);
        });
        
        assertEquals("店铺不存在", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).addStoreFavorite(userId, storeId);
    }

    // ==================== removeStoreFavorite() 方法测试 ====================

    /**
     * 正向测试：成功移除店铺收藏
     */
    @Test
    void testRemoveStoreFavorite_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long storeId = 100L;
        Integer expectedResult = 1; // 移除成功
        
        // 模拟服务行为
        when(favoriteService.removeStoreFavorite(userId, storeId)).thenReturn(expectedResult);
        
        // 执行测试
        Integer actualResult = favoriteService.removeStoreFavorite(userId, storeId);
        
        // 验证结果
        assertEquals(expectedResult, actualResult, "返回结果应该匹配");
        assertEquals(1, actualResult, "移除成功应该返回1");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).removeStoreFavorite(userId, storeId);
    }

    /**
     * 正向测试：移除不存在的店铺收藏
     */
    @Test
    void testRemoveStoreFavorite_NotExists() {
        // 准备测试数据
        Long userId = 1L;
        Long storeId = 100L;
        Integer expectedResult = 0; // 不存在，未移除
        
        // 模拟服务行为
        when(favoriteService.removeStoreFavorite(userId, storeId)).thenReturn(expectedResult);
        
        // 执行测试
        Integer actualResult = favoriteService.removeStoreFavorite(userId, storeId);
        
        // 验证结果
        assertEquals(expectedResult, actualResult, "返回结果应该匹配");
        assertEquals(0, actualResult, "移除不存在的收藏应该返回0");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).removeStoreFavorite(userId, storeId);
    }

    /**
     * 反向测试：userId为null
     */
    @Test
    void testRemoveStoreFavorite_NullUserId() {
        // 准备测试数据
        Long userId = null;
        Long storeId = 100L;
        
        // 模拟服务抛出异常
        when(favoriteService.removeStoreFavorite(userId, storeId))
                .thenThrow(new IllegalArgumentException("用户ID不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            favoriteService.removeStoreFavorite(userId, storeId);
        });
        
        assertEquals("用户ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).removeStoreFavorite(userId, storeId);
    }

    /**
     * 反向测试：storeId为null
     */
    @Test
    void testRemoveStoreFavorite_NullStoreId() {
        // 准备测试数据
        Long userId = 1L;
        Long storeId = null;
        
        // 模拟服务抛出异常
        when(favoriteService.removeStoreFavorite(userId, storeId))
                .thenThrow(new IllegalArgumentException("店铺ID不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            favoriteService.removeStoreFavorite(userId, storeId);
        });
        
        assertEquals("店铺ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).removeStoreFavorite(userId, storeId);
    }

    // ==================== listFavoriteFoods() 方法测试 ====================

    /**
     * 正向测试：成功获取收藏商品列表
     */
    @Test
    void testListFavoriteFoods_Success() {
        // 准备测试数据
        Long userId = 1L;
        int page = 1;
        int size = 10;
        
        FoodVO foodVO1 = new FoodVO();
        foodVO1.setId(1L);
        foodVO1.setName("红烧肉");
        foodVO1.setDescription("香甜可口的红烧肉");
        
        FoodVO foodVO2 = new FoodVO();
        foodVO2.setId(2L);
        foodVO2.setName("糖醋里脊");
        foodVO2.setDescription("酸甜开胃的糖醋里脊");
        
        PageVO<FoodVO> expectedPage = new PageVO<>();
        expectedPage.setContent(Arrays.asList(foodVO1, foodVO2));
        expectedPage.setTotalElements(2L);
        expectedPage.setNumber(1);
        expectedPage.setSize(10);
        
        // 模拟服务行为
        when(favoriteService.listFavoriteFoods(userId, page, size)).thenReturn(expectedPage);
        
        // 执行测试
        PageVO<FoodVO> actualPage = favoriteService.listFavoriteFoods(userId, page, size);
        
        // 验证结果
        assertNotNull(actualPage, "分页结果不应为null");
        assertNotNull(actualPage.getContent(), "记录列表不应为null");
        assertEquals(2, actualPage.getContent().size(), "记录数量应为2");
        assertEquals(2L, actualPage.getTotalElements(), "总数应为2");
        assertEquals(1, actualPage.getNumber(), "当前页应为1");
        assertEquals(10, actualPage.getSize(), "页大小应为10");
        assertEquals("红烧肉", actualPage.getContent().get(0).getName(), "第一个商品名应该匹配");
        assertEquals("糖醋里脊", actualPage.getContent().get(1).getName(), "第二个商品名应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).listFavoriteFoods(userId, page, size);
    }

    /**
     * 正向测试：获取空的收藏商品列表
     */
    @Test
    void testListFavoriteFoods_EmptyList() {
        // 准备测试数据
        Long userId = 1L;
        int page = 1;
        int size = 10;
        
        PageVO<FoodVO> expectedPage = new PageVO<>();
        expectedPage.setContent(Collections.emptyList());
        expectedPage.setTotalElements(0L);
        expectedPage.setNumber(1);
        expectedPage.setSize(10);
        
        // 模拟服务行为
        when(favoriteService.listFavoriteFoods(userId, page, size)).thenReturn(expectedPage);
        
        // 执行测试
        PageVO<FoodVO> actualPage = favoriteService.listFavoriteFoods(userId, page, size);
        
        // 验证结果
        assertNotNull(actualPage, "分页结果不应为null");
        assertNotNull(actualPage.getContent(), "记录列表不应为null");
        assertTrue(actualPage.getContent().isEmpty(), "记录列表应为空");
        assertEquals(0L, actualPage.getTotalElements(), "总数应为0");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).listFavoriteFoods(userId, page, size);
    }

    /**
     * 反向测试：userId为null
     */
    @Test
    void testListFavoriteFoods_NullUserId() {
        // 准备测试数据
        Long userId = null;
        int page = 1;
        int size = 10;
        
        // 模拟服务抛出异常
        when(favoriteService.listFavoriteFoods(userId, page, size))
                .thenThrow(new IllegalArgumentException("用户ID不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            favoriteService.listFavoriteFoods(userId, page, size);
        });
        
        assertEquals("用户ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).listFavoriteFoods(userId, page, size);
    }

    // ==================== addFoodFavorite() 方法测试 ====================

    /**
     * 正向测试：成功添加商品收藏
     */
    @Test
    void testAddFoodFavorite_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long foodId = 200L;
        Integer expectedResult = 1; // 添加成功
        
        // 模拟服务行为
        when(favoriteService.addFoodFavorite(userId, foodId)).thenReturn(expectedResult);
        
        // 执行测试
        Integer actualResult = favoriteService.addFoodFavorite(userId, foodId);
        
        // 验证结果
        assertEquals(expectedResult, actualResult, "返回结果应该匹配");
        assertEquals(1, actualResult, "添加成功应该返回1");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).addFoodFavorite(userId, foodId);
    }

    /**
     * 正向测试：重复添加商品收藏
     */
    @Test
    void testAddFoodFavorite_AlreadyExists() {
        // 准备测试数据
        Long userId = 1L;
        Long foodId = 200L;
        Integer expectedResult = 0; // 已存在，未添加
        
        // 模拟服务行为
        when(favoriteService.addFoodFavorite(userId, foodId)).thenReturn(expectedResult);
        
        // 执行测试
        Integer actualResult = favoriteService.addFoodFavorite(userId, foodId);
        
        // 验证结果
        assertEquals(expectedResult, actualResult, "返回结果应该匹配");
        assertEquals(0, actualResult, "重复添加应该返回0");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).addFoodFavorite(userId, foodId);
    }

    /**
     * 反向测试：userId为null
     */
    @Test
    void testAddFoodFavorite_NullUserId() {
        // 准备测试数据
        Long userId = null;
        Long foodId = 200L;
        
        // 模拟服务抛出异常
        when(favoriteService.addFoodFavorite(userId, foodId))
                .thenThrow(new IllegalArgumentException("用户ID不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            favoriteService.addFoodFavorite(userId, foodId);
        });
        
        assertEquals("用户ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).addFoodFavorite(userId, foodId);
    }

    /**
     * 反向测试：foodId为null
     */
    @Test
    void testAddFoodFavorite_NullFoodId() {
        // 准备测试数据
        Long userId = 1L;
        Long foodId = null;
        
        // 模拟服务抛出异常
        when(favoriteService.addFoodFavorite(userId, foodId))
                .thenThrow(new IllegalArgumentException("商品ID不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            favoriteService.addFoodFavorite(userId, foodId);
        });
        
        assertEquals("商品ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).addFoodFavorite(userId, foodId);
    }

    /**
     * 反向测试：商品不存在
     */
    @Test
    void testAddFoodFavorite_FoodNotFound() {
        // 准备测试数据
        Long userId = 1L;
        Long foodId = 999L;
        
        // 模拟服务抛出异常
        when(favoriteService.addFoodFavorite(userId, foodId))
                .thenThrow(new RuntimeException("商品不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            favoriteService.addFoodFavorite(userId, foodId);
        });
        
        assertEquals("商品不存在", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).addFoodFavorite(userId, foodId);
    }

    // ==================== removeFoodFavorite() 方法测试 ====================

    /**
     * 正向测试：成功移除商品收藏
     */
    @Test
    void testRemoveFoodFavorite_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long foodId = 200L;
        Integer expectedResult = 1; // 移除成功
        
        // 模拟服务行为
        when(favoriteService.removeFoodFavorite(userId, foodId)).thenReturn(expectedResult);
        
        // 执行测试
        Integer actualResult = favoriteService.removeFoodFavorite(userId, foodId);
        
        // 验证结果
        assertEquals(expectedResult, actualResult, "返回结果应该匹配");
        assertEquals(1, actualResult, "移除成功应该返回1");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).removeFoodFavorite(userId, foodId);
    }

    /**
     * 正向测试：移除不存在的商品收藏
     */
    @Test
    void testRemoveFoodFavorite_NotExists() {
        // 准备测试数据
        Long userId = 1L;
        Long foodId = 200L;
        Integer expectedResult = 0; // 不存在，未移除
        
        // 模拟服务行为
        when(favoriteService.removeFoodFavorite(userId, foodId)).thenReturn(expectedResult);
        
        // 执行测试
        Integer actualResult = favoriteService.removeFoodFavorite(userId, foodId);
        
        // 验证结果
        assertEquals(expectedResult, actualResult, "返回结果应该匹配");
        assertEquals(0, actualResult, "移除不存在的收藏应该返回0");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).removeFoodFavorite(userId, foodId);
    }

    /**
     * 反向测试：userId为null
     */
    @Test
    void testRemoveFoodFavorite_NullUserId() {
        // 准备测试数据
        Long userId = null;
        Long foodId = 200L;
        
        // 模拟服务抛出异常
        when(favoriteService.removeFoodFavorite(userId, foodId))
                .thenThrow(new IllegalArgumentException("用户ID不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            favoriteService.removeFoodFavorite(userId, foodId);
        });
        
        assertEquals("用户ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).removeFoodFavorite(userId, foodId);
    }

    /**
     * 反向测试：foodId为null
     */
    @Test
    void testRemoveFoodFavorite_NullFoodId() {
        // 准备测试数据
        Long userId = 1L;
        Long foodId = null;
        
        // 模拟服务抛出异常
        when(favoriteService.removeFoodFavorite(userId, foodId))
                .thenThrow(new IllegalArgumentException("商品ID不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            favoriteService.removeFoodFavorite(userId, foodId);
        });
        
        assertEquals("商品ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).removeFoodFavorite(userId, foodId);
    }

    // ==================== queryFavorite() 方法测试 ====================

    /**
     * 正向测试：成功查询收藏状态（已收藏）
     */
    @Test
    void testQueryFavorite_ExistsFavorite() {
        // 准备测试数据
        Long userId = 1L;
        Long targetId = 100L;
        String targetType = "store";
        Integer expectedResult = 1; // 已收藏
        
        // 模拟服务行为
        when(favoriteService.queryFavorite(userId, targetId, targetType)).thenReturn(expectedResult);
        
        // 执行测试
        Integer actualResult = favoriteService.queryFavorite(userId, targetId, targetType);
        
        // 验证结果
        assertEquals(expectedResult, actualResult, "返回结果应该匹配");
        assertEquals(1, actualResult, "已收藏应该返回1");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).queryFavorite(userId, targetId, targetType);
    }

    /**
     * 正向测试：成功查询收藏状态（未收藏）
     */
    @Test
    void testQueryFavorite_NotExistsFavorite() {
        // 准备测试数据
        Long userId = 1L;
        Long targetId = 100L;
        String targetType = "food";
        Integer expectedResult = 0; // 未收藏
        
        // 模拟服务行为
        when(favoriteService.queryFavorite(userId, targetId, targetType)).thenReturn(expectedResult);
        
        // 执行测试
        Integer actualResult = favoriteService.queryFavorite(userId, targetId, targetType);
        
        // 验证结果
        assertEquals(expectedResult, actualResult, "返回结果应该匹配");
        assertEquals(0, actualResult, "未收藏应该返回0");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).queryFavorite(userId, targetId, targetType);
    }

    /**
     * 正向测试：测试不同的目标类型
     */
    @Test
    void testQueryFavorite_DifferentTargetTypes() {
        // 准备测试数据
        Long userId = 1L;
        Long targetId = 100L;
        String[] targetTypes = {"store", "food"};
        
        for (String targetType : targetTypes) {
            Integer expectedResult = 1; // 已收藏
            
            // 模拟服务行为
            when(favoriteService.queryFavorite(userId, targetId, targetType)).thenReturn(expectedResult);
            
            // 执行测试
            Integer actualResult = favoriteService.queryFavorite(userId, targetId, targetType);
            
            // 验证结果
            assertEquals(expectedResult, actualResult, targetType + " 类型的查询结果应该匹配");
            assertEquals(1, actualResult, targetType + " 类型应该返回1");
        }
        
        // 验证方法被调用次数
        verify(favoriteService, times(targetTypes.length)).queryFavorite(eq(userId), eq(targetId), anyString());
    }

    /**
     * 反向测试：userId为null
     */
    @Test
    void testQueryFavorite_NullUserId() {
        // 准备测试数据
        Long userId = null;
        Long targetId = 100L;
        String targetType = "store";
        
        // 模拟服务抛出异常
        when(favoriteService.queryFavorite(userId, targetId, targetType))
                .thenThrow(new IllegalArgumentException("用户ID不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            favoriteService.queryFavorite(userId, targetId, targetType);
        });
        
        assertEquals("用户ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).queryFavorite(userId, targetId, targetType);
    }

    /**
     * 反向测试：targetId为null
     */
    @Test
    void testQueryFavorite_NullTargetId() {
        // 准备测试数据
        Long userId = 1L;
        Long targetId = null;
        String targetType = "store";
        
        // 模拟服务抛出异常
        when(favoriteService.queryFavorite(userId, targetId, targetType))
                .thenThrow(new IllegalArgumentException("目标ID不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            favoriteService.queryFavorite(userId, targetId, targetType);
        });
        
        assertEquals("目标ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).queryFavorite(userId, targetId, targetType);
    }

    /**
     * 反向测试：targetType为null或空
     */
    @Test
    void testQueryFavorite_NullOrEmptyTargetType() {
        // 准备测试数据
        Long userId = 1L;
        Long targetId = 100L;
        String targetType = null;
        
        // 模拟服务抛出异常
        when(favoriteService.queryFavorite(userId, targetId, targetType))
                .thenThrow(new IllegalArgumentException("目标类型不能为null或空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            favoriteService.queryFavorite(userId, targetId, targetType);
        });
        
        assertEquals("目标类型不能为null或空", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).queryFavorite(userId, targetId, targetType);
    }

    /**
     * 反向测试：无效的目标类型
     */
    @Test
    void testQueryFavorite_InvalidTargetType() {
        // 准备测试数据
        Long userId = 1L;
        Long targetId = 100L;
        String targetType = "invalid";
        
        // 模拟服务抛出异常
        when(favoriteService.queryFavorite(userId, targetId, targetType))
                .thenThrow(new IllegalArgumentException("无效的目标类型: " + targetType));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            favoriteService.queryFavorite(userId, targetId, targetType);
        });
        
        assertEquals("无效的目标类型: " + targetType, exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).queryFavorite(userId, targetId, targetType);
    }

    /**
     * 正向测试：边界值测试 - 大量分页查询
     */
    @Test
    void testListFavoriteStores_LargePage() {
        // 准备测试数据
        Long userId = 1L;
        int page = 100;
        int size = 50;
        
        PageVO<StoreVO> expectedPage = new PageVO<>();
        expectedPage.setContent(Collections.emptyList());
        expectedPage.setTotalElements(0L);
        expectedPage.setNumber(100);
        expectedPage.setSize(50);
        
        // 模拟服务行为
        when(favoriteService.listFavoriteStores(userId, page, size)).thenReturn(expectedPage);
        
        // 执行测试
        PageVO<StoreVO> actualPage = favoriteService.listFavoriteStores(userId, page, size);
        
        // 验证结果
        assertNotNull(actualPage, "分页结果不应为null");
        assertEquals(100, actualPage.getNumber(), "当前页应为100");
        assertEquals(50, actualPage.getSize(), "页大小应为50");
        
        // 验证方法被调用
        verify(favoriteService, times(1)).listFavoriteStores(userId, page, size);
    }

    /**
     * 正向测试：批量操作测试
     */
    @Test
    void testBatchOperations() {
        // 准备测试数据
        Long userId = 1L;
        Long[] storeIds = {100L, 101L, 102L};
        
        for (Long storeId : storeIds) {
            // 模拟添加收藏
            when(favoriteService.addStoreFavorite(userId, storeId)).thenReturn(1);
            
            // 执行添加测试
            Integer addResult = favoriteService.addStoreFavorite(userId, storeId);
            assertEquals(1, addResult, "添加店铺 " + storeId + " 收藏应该成功");
            
            // 模拟移除收藏
            when(favoriteService.removeStoreFavorite(userId, storeId)).thenReturn(1);
            
            // 执行移除测试
            Integer removeResult = favoriteService.removeStoreFavorite(userId, storeId);
            assertEquals(1, removeResult, "移除店铺 " + storeId + " 收藏应该成功");
        }
        
        // 验证方法被调用次数
        verify(favoriteService, times(storeIds.length)).addStoreFavorite(eq(userId), anyLong());
        verify(favoriteService, times(storeIds.length)).removeStoreFavorite(eq(userId), anyLong());
    }
}
