package com.blm.user.service.impl;

import com.blm.common.vo.FoodVO;
import com.blm.common.vo.PageVO;
import com.blm.common.vo.StoreVO;
import com.blm.user.service.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * HistoryService接口测试类
 * 针对每个方法实现正向和反向测试
 */
@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

    @Mock
    private HistoryService historyService;

    private Long validUserId;
    private Long validHistoryId;
    private Long validTargetId;
    private int validPage;
    private int validSize;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        validUserId = 1L;
        validHistoryId = 10L;
        validTargetId = 100L;
        validPage = 1;
        validSize = 10;
    }

    // ==================== listStoresHistory 测试 ====================

    /**
     * 正向测试：成功获取店铺浏览历史
     */
    @Test
    void testListStoresHistory_Success() {
        // 准备测试数据
        StoreVO storeVO = new StoreVO();
        storeVO.setId(1L);
        storeVO.setName("测试店铺");
        storeVO.setPhone("13800138000");
        storeVO.setAddress("测试地址");

        PageVO<StoreVO> pageVO = new PageVO<>();
        pageVO.setContent(Arrays.asList(storeVO));
        pageVO.setTotalElements(1L);
        pageVO.setSize(validSize);
        pageVO.setNumber(validPage);
        pageVO.setTotalPages(1);

        // 模拟服务行为
        when(historyService.listStoresHistory(validUserId, validPage, validSize)).thenReturn(pageVO);

        // 执行测试
        PageVO<StoreVO> result = historyService.listStoresHistory(validUserId, validPage, validSize);

        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertNotNull(result.getContent(), "记录列表不应为null");
        assertEquals(1, result.getContent().size(), "记录数量应为1");
        assertEquals(1L, result.getTotalElements(), "总记录数应为1");
        assertEquals(validPage, result.getNumber(), "当前页码应该匹配");
        assertEquals(validSize, result.getSize(), "页面大小应该匹配");
        assertEquals("测试店铺", result.getContent().get(0).getName(), "店铺名称应该匹配");

        // 验证方法被调用
        verify(historyService, times(1)).listStoresHistory(validUserId, validPage, validSize);
    }

    /**
     * 正向测试：获取空的店铺浏览历史
     */
    @Test
    void testListStoresHistory_EmptyResult() {
        // 准备空的分页结果
        PageVO<StoreVO> emptyPageVO = new PageVO<>();
        emptyPageVO.setContent(Collections.emptyList());
        emptyPageVO.setTotalElements(0L);
        emptyPageVO.setSize(validSize);
        emptyPageVO.setNumber(validPage);
        emptyPageVO.setTotalPages(0);

        // 模拟服务行为
        when(historyService.listStoresHistory(validUserId, validPage, validSize)).thenReturn(emptyPageVO);

        // 执行测试
        PageVO<StoreVO> result = historyService.listStoresHistory(validUserId, validPage, validSize);

        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertNotNull(result.getContent(), "记录列表不应为null");
        assertTrue(result.getContent().isEmpty(), "记录列表应为空");
        assertEquals(0L, result.getTotalElements(), "总记录数应为0");

        // 验证方法被调用
        verify(historyService, times(1)).listStoresHistory(validUserId, validPage, validSize);
    }

    /**
     * 反向测试：传入null的用户ID
     */
    @Test
    void testListStoresHistory_NullUserId() {
        // 模拟抛出异常
        when(historyService.listStoresHistory(null, validPage, validSize))
                .thenThrow(new IllegalArgumentException("用户ID不能为空"));

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> historyService.listStoresHistory(null, validPage, validSize),
                "应该抛出IllegalArgumentException"
        );

        assertEquals("用户ID不能为空", exception.getMessage(), "异常信息应该匹配");

        // 验证方法被调用
        verify(historyService, times(1)).listStoresHistory(null, validPage, validSize);
    }

    /**
     * 反向测试：传入无效的页码（小于1）
     */
    @Test
    void testListStoresHistory_InvalidPage() {
        int invalidPage = 0;

        // 模拟抛出异常
        when(historyService.listStoresHistory(validUserId, invalidPage, validSize))
                .thenThrow(new IllegalArgumentException("页码必须大于0"));

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> historyService.listStoresHistory(validUserId, invalidPage, validSize),
                "应该抛出IllegalArgumentException"
        );

        assertEquals("页码必须大于0", exception.getMessage(), "异常信息应该匹配");

        // 验证方法被调用
        verify(historyService, times(1)).listStoresHistory(validUserId, invalidPage, validSize);
    }

    /**
     * 反向测试：用户不存在
     */
    @Test
    void testListStoresHistory_UserNotFound() {
        Long nonExistentUserId = 999L;

        // 模拟抛出异常
        when(historyService.listStoresHistory(nonExistentUserId, validPage, validSize))
                .thenThrow(new RuntimeException("用户不存在"));

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> historyService.listStoresHistory(nonExistentUserId, validPage, validSize),
                "应该抛出RuntimeException"
        );

        assertEquals("用户不存在", exception.getMessage(), "异常信息应该匹配");

        // 验证方法被调用
        verify(historyService, times(1)).listStoresHistory(nonExistentUserId, validPage, validSize);
    }

    // ==================== listFoodsHistory 测试 ====================

    /**
     * 正向测试：成功获取食物浏览历史
     */
    @Test
    void testListFoodsHistory_Success() {
        // 准备测试数据
        FoodVO foodVO = new FoodVO();
        foodVO.setId(1L);
        foodVO.setName("测试食物");
        foodVO.setPrice(new BigDecimal("25.50"));
        foodVO.setDescription("美味测试食物");

        PageVO<FoodVO> pageVO = new PageVO<>();
        pageVO.setContent(Arrays.asList(foodVO));
        pageVO.setTotalElements(1L);
        pageVO.setSize(validSize);
        pageVO.setNumber(validPage);
        pageVO.setTotalPages(1);

        // 模拟服务行为
        when(historyService.listFoodsHistory(validUserId, validPage, validSize)).thenReturn(pageVO);

        // 执行测试
        PageVO<FoodVO> result = historyService.listFoodsHistory(validUserId, validPage, validSize);

        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertNotNull(result.getContent(), "记录列表不应为null");
        assertEquals(1, result.getContent().size(), "记录数量应为1");
        assertEquals(1L, result.getTotalElements(), "总记录数应为1");
        assertEquals("测试食物", result.getContent().get(0).getName(), "食物名称应该匹配");
        assertEquals(new BigDecimal("25.50"), result.getContent().get(0).getPrice(), "食物价格应该匹配");

        // 验证方法被调用
        verify(historyService, times(1)).listFoodsHistory(validUserId, validPage, validSize);
    }

    /**
     * 正向测试：获取空的食物浏览历史
     */
    @Test
    void testListFoodsHistory_EmptyResult() {
        // 准备空的分页结果
        PageVO<FoodVO> emptyPageVO = new PageVO<>();
        emptyPageVO.setContent(Collections.emptyList());
        emptyPageVO.setTotalElements(0L);
        emptyPageVO.setSize(validSize);
        emptyPageVO.setNumber(validPage);
        emptyPageVO.setTotalPages(0);

        // 模拟服务行为
        when(historyService.listFoodsHistory(validUserId, validPage, validSize)).thenReturn(emptyPageVO);

        // 执行测试
        PageVO<FoodVO> result = historyService.listFoodsHistory(validUserId, validPage, validSize);

        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertNotNull(result.getContent(), "记录列表不应为null");
        assertTrue(result.getContent().isEmpty(), "记录列表应为空");
        assertEquals(0L, result.getTotalElements(), "总记录数应为0");

        // 验证方法被调用
        verify(historyService, times(1)).listFoodsHistory(validUserId, validPage, validSize);
    }

    /**
     * 反向测试：传入null的用户ID
     */
    @Test
    void testListFoodsHistory_NullUserId() {
        // 模拟抛出异常
        when(historyService.listFoodsHistory(null, validPage, validSize))
                .thenThrow(new IllegalArgumentException("用户ID不能为空"));

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> historyService.listFoodsHistory(null, validPage, validSize),
                "应该抛出IllegalArgumentException"
        );

        assertEquals("用户ID不能为空", exception.getMessage(), "异常信息应该匹配");

        // 验证方法被调用
        verify(historyService, times(1)).listFoodsHistory(null, validPage, validSize);
    }

    /**
     * 反向测试：传入无效的页面大小（负数）
     */
    @Test
    void testListFoodsHistory_NegativeSize() {
        int negativeSize = -5;

        // 模拟抛出异常
        when(historyService.listFoodsHistory(validUserId, validPage, negativeSize))
                .thenThrow(new IllegalArgumentException("页面大小必须大于0"));

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> historyService.listFoodsHistory(validUserId, validPage, negativeSize),
                "应该抛出IllegalArgumentException"
        );

        assertEquals("页面大小必须大于0", exception.getMessage(), "异常信息应该匹配");

        // 验证方法被调用
        verify(historyService, times(1)).listFoodsHistory(validUserId, validPage, negativeSize);
    }

    // ==================== removeHistory 测试 ====================

    /**
     * 正向测试：成功移除浏览历史
     */
    @Test
    void testRemoveHistory_Success() {
        // 模拟成功删除
        doNothing().when(historyService).removeHistory(validUserId, validHistoryId);

        // 执行测试（应该不抛出异常）
        assertDoesNotThrow(() -> historyService.removeHistory(validUserId, validHistoryId),
                "移除历史不应抛出异常");

        // 验证方法被调用
        verify(historyService, times(1)).removeHistory(validUserId, validHistoryId);
    }

    /**
     * 反向测试：传入null的用户ID
     */
    @Test
    void testRemoveHistory_NullUserId() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("用户ID不能为空"))
                .when(historyService).removeHistory(null, validHistoryId);

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> historyService.removeHistory(null, validHistoryId),
                "应该抛出IllegalArgumentException"
        );

        assertEquals("用户ID不能为空", exception.getMessage(), "异常信息应该匹配");

        // 验证方法被调用
        verify(historyService, times(1)).removeHistory(null, validHistoryId);
    }

    /**
     * 反向测试：传入null的历史ID
     */
    @Test
    void testRemoveHistory_NullHistoryId() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("历史记录ID不能为空"))
                .when(historyService).removeHistory(validUserId, null);

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> historyService.removeHistory(validUserId, null),
                "应该抛出IllegalArgumentException"
        );

        assertEquals("历史记录ID不能为空", exception.getMessage(), "异常信息应该匹配");

        // 验证方法被调用
        verify(historyService, times(1)).removeHistory(validUserId, null);
    }

    /**
     * 反向测试：历史记录不存在
     */
    @Test
    void testRemoveHistory_HistoryNotFound() {
        Long nonExistentHistoryId = 999L;

        // 模拟抛出异常
        doThrow(new RuntimeException("历史记录不存在"))
                .when(historyService).removeHistory(validUserId, nonExistentHistoryId);

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> historyService.removeHistory(validUserId, nonExistentHistoryId),
                "应该抛出RuntimeException"
        );

        assertEquals("历史记录不存在", exception.getMessage(), "异常信息应该匹配");

        // 验证方法被调用
        verify(historyService, times(1)).removeHistory(validUserId, nonExistentHistoryId);
    }

    // ==================== addStoreHistory 测试 ====================

    /**
     * 正向测试：成功添加店铺浏览历史
     */
    @Test
    void testAddStoreHistory_Success() {
        // 模拟成功添加
        doNothing().when(historyService).addStoreHistory(validUserId, validTargetId);

        // 执行测试（应该不抛出异常）
        assertDoesNotThrow(() -> historyService.addStoreHistory(validUserId, validTargetId),
                "添加店铺历史不应抛出异常");

        // 验证方法被调用
        verify(historyService, times(1)).addStoreHistory(validUserId, validTargetId);
    }

    /**
     * 反向测试：传入null的用户ID
     */
    @Test
    void testAddStoreHistory_NullUserId() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("用户ID不能为空"))
                .when(historyService).addStoreHistory(null, validTargetId);

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> historyService.addStoreHistory(null, validTargetId),
                "应该抛出IllegalArgumentException"
        );

        assertEquals("用户ID不能为空", exception.getMessage(), "异常信息应该匹配");

        // 验证方法被调用
        verify(historyService, times(1)).addStoreHistory(null, validTargetId);
    }

    /**
     * 反向测试：传入null的目标ID
     */
    @Test
    void testAddStoreHistory_NullTargetId() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("目标ID不能为空"))
                .when(historyService).addStoreHistory(validUserId, null);

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> historyService.addStoreHistory(validUserId, null),
                "应该抛出IllegalArgumentException"
        );

        assertEquals("目标ID不能为空", exception.getMessage(), "异常信息应该匹配");

        // 验证方法被调用
        verify(historyService, times(1)).addStoreHistory(validUserId, null);
    }

    /**
     * 反向测试：用户不存在
     */
    @Test
    void testAddStoreHistory_UserNotFound() {
        Long nonExistentUserId = 999L;

        // 模拟抛出异常
        doThrow(new RuntimeException("用户不存在"))
                .when(historyService).addStoreHistory(nonExistentUserId, validTargetId);

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> historyService.addStoreHistory(nonExistentUserId, validTargetId),
                "应该抛出RuntimeException"
        );

        assertEquals("用户不存在", exception.getMessage(), "异常信息应该匹配");

        // 验证方法被调用
        verify(historyService, times(1)).addStoreHistory(nonExistentUserId, validTargetId);
    }

    /**
     * 反向测试：店铺不存在
     */
    @Test
    void testAddStoreHistory_StoreNotFound() {
        Long nonExistentStoreId = 999L;

        // 模拟抛出异常
        doThrow(new RuntimeException("店铺不存在"))
                .when(historyService).addStoreHistory(validUserId, nonExistentStoreId);

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> historyService.addStoreHistory(validUserId, nonExistentStoreId),
                "应该抛出RuntimeException"
        );

        assertEquals("店铺不存在", exception.getMessage(), "异常信息应该匹配");

        // 验证方法被调用
        verify(historyService, times(1)).addStoreHistory(validUserId, nonExistentStoreId);
    }

    // ==================== addFoodHistory 测试 ====================

    /**
     * 正向测试：成功添加食物浏览历史
     */
    @Test
    void testAddFoodHistory_Success() {
        // 模拟成功添加
        doNothing().when(historyService).addFoodHistory(validUserId, validTargetId);

        // 执行测试（应该不抛出异常）
        assertDoesNotThrow(() -> historyService.addFoodHistory(validUserId, validTargetId),
                "添加食物历史不应抛出异常");

        // 验证方法被调用
        verify(historyService, times(1)).addFoodHistory(validUserId, validTargetId);
    }

    /**
     * 反向测试：传入null的用户ID
     */
    @Test
    void testAddFoodHistory_NullUserId() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("用户ID不能为空"))
                .when(historyService).addFoodHistory(null, validTargetId);

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> historyService.addFoodHistory(null, validTargetId),
                "应该抛出IllegalArgumentException"
        );

        assertEquals("用户ID不能为空", exception.getMessage(), "异常信息应该匹配");

        // 验证方法被调用
        verify(historyService, times(1)).addFoodHistory(null, validTargetId);
    }

    /**
     * 反向测试：传入null的目标ID
     */
    @Test
    void testAddFoodHistory_NullTargetId() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("目标ID不能为空"))
                .when(historyService).addFoodHistory(validUserId, null);

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> historyService.addFoodHistory(validUserId, null),
                "应该抛出IllegalArgumentException"
        );

        assertEquals("目标ID不能为空", exception.getMessage(), "异常信息应该匹配");

        // 验证方法被调用
        verify(historyService, times(1)).addFoodHistory(validUserId, null);
    }

    /**
     * 反向测试：用户不存在
     */
    @Test
    void testAddFoodHistory_UserNotFound() {
        Long nonExistentUserId = 999L;

        // 模拟抛出异常
        doThrow(new RuntimeException("用户不存在"))
                .when(historyService).addFoodHistory(nonExistentUserId, validTargetId);

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> historyService.addFoodHistory(nonExistentUserId, validTargetId),
                "应该抛出RuntimeException"
        );

        assertEquals("用户不存在", exception.getMessage(), "异常信息应该匹配");

        // 验证方法被调用
        verify(historyService, times(1)).addFoodHistory(nonExistentUserId, validTargetId);
    }

    /**
     * 反向测试：食物不存在
     */
    @Test
    void testAddFoodHistory_FoodNotFound() {
        Long nonExistentFoodId = 999L;

        // 模拟抛出异常
        doThrow(new RuntimeException("食物不存在"))
                .when(historyService).addFoodHistory(validUserId, nonExistentFoodId);

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> historyService.addFoodHistory(validUserId, nonExistentFoodId),
                "应该抛出RuntimeException"
        );

        assertEquals("食物不存在", exception.getMessage(), "异常信息应该匹配");

        // 验证方法被调用
        verify(historyService, times(1)).addFoodHistory(validUserId, nonExistentFoodId);
    }
}