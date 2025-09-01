package com.blm.admin.service.impl;

import com.blm.admin.service.AdminStoreService;
import com.blm.common.vo.StoreVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * AdminStoreService 单元测试
 * 测试管理员商店服务的所有方法
 */
@ExtendWith(MockitoExtension.class)
class AdminStoreServiceTest {

    @Mock
    private AdminStoreService adminStoreService;

    @BeforeEach
    void setUp() {
        // 在每个测试方法执行前的准备工作
    }

    // ==================== listStores() 方法测试 ====================

    /**
     * 正向测试：成功获取商店列表
     */
    @Test
    void testListStores_Success() {
        // 准备测试数据
        StoreVO storeVO1 = new StoreVO();
        storeVO1.setId(1L);
        storeVO1.setName("美味餐厅");
        storeVO1.setDescription("精致美味的中式餐厅");
        storeVO1.setPhone("13800138001");
        
        StoreVO storeVO2 = new StoreVO();
        storeVO2.setId(2L);
        storeVO2.setName("快乐小吃");
        storeVO2.setDescription("快乐美味的街头小吃");
        storeVO2.setPhone("13800138002");
        
        List<StoreVO> expectedStores = Arrays.asList(storeVO1, storeVO2);
        
        // 模拟服务行为
        when(adminStoreService.listStores()).thenReturn(expectedStores);
        
        // 执行测试
        List<StoreVO> actualStores = adminStoreService.listStores();
        
        // 验证结果
        assertNotNull(actualStores, "商店列表不应为null");
        assertEquals(2, actualStores.size(), "商店列表大小应为2");
        assertEquals("美味餐厅", actualStores.get(0).getName(), "第一个商店名应该匹配");
        assertEquals("快乐小吃", actualStores.get(1).getName(), "第二个商店名应该匹配");
        assertEquals("精致美味的中式餐厅", actualStores.get(0).getDescription(), "第一个描述应该匹配");
        assertEquals("快乐美味的街头小吃", actualStores.get(1).getDescription(), "第二个描述应该匹配");
        assertEquals("13800138001", actualStores.get(0).getPhone(), "第一个电话应该匹配");
        assertEquals("13800138002", actualStores.get(1).getPhone(), "第二个电话应该匹配");
        
        // 验证方法被调用
        verify(adminStoreService, times(1)).listStores();
    }

    /**
     * 正向测试：获取空商店列表
     */
    @Test
    void testListStores_EmptyList() {
        // 准备测试数据
        List<StoreVO> expectedStores = Collections.emptyList();
        
        // 模拟服务行为
        when(adminStoreService.listStores()).thenReturn(expectedStores);
        
        // 执行测试
        List<StoreVO> actualStores = adminStoreService.listStores();
        
        // 验证结果
        assertNotNull(actualStores, "商店列表不应为null");
        assertTrue(actualStores.isEmpty(), "商店列表应为空");
        
        // 验证方法被调用
        verify(adminStoreService, times(1)).listStores();
    }

    /**
     * 反向测试：listStores抛出异常
     */
    @Test
    void testListStores_ThrowsException() {
        // 模拟服务抛出异常
        when(adminStoreService.listStores()).thenThrow(new RuntimeException("数据库连接失败"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminStoreService.listStores();
        });
        
        assertEquals("数据库连接失败", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminStoreService, times(1)).listStores();
    }

    /**
     * 反向测试：listStores返回null
     */
    @Test
    void testListStores_ReturnsNull() {
        // 模拟服务返回null
        when(adminStoreService.listStores()).thenReturn(null);
        
        // 执行测试
        List<StoreVO> actualStores = adminStoreService.listStores();
        
        // 验证结果
        assertNull(actualStores, "应该返回null");
        
        // 验证方法被调用
        verify(adminStoreService, times(1)).listStores();
    }

    /**
     * 正向测试：获取大量商店列表
     */
    @Test
    void testListStores_LargeList() {
        // 准备测试数据 - 模拟大量商店
        List<StoreVO> expectedStores = Collections.nCopies(500, new StoreVO());
        
        // 模拟服务行为
        when(adminStoreService.listStores()).thenReturn(expectedStores);
        
        // 执行测试
        List<StoreVO> actualStores = adminStoreService.listStores();
        
        // 验证结果
        assertNotNull(actualStores, "商店列表不应为null");
        assertEquals(500, actualStores.size(), "商店列表大小应为500");
        
        // 验证方法被调用
        verify(adminStoreService, times(1)).listStores();
    }

    // ==================== updateStatus() 方法测试 ====================

    /**
     * 正向测试：成功更新商店状态
     */
    @Test
    void testUpdateStatus_Success() {
        // 准备测试数据
        Long storeId = 1L;
        Integer status = 1; // 营业状态
        
        // 模拟服务行为（void方法不需要返回值）
        doNothing().when(adminStoreService).updateStatus(storeId, status);
        
        // 执行测试
        assertDoesNotThrow(() -> {
            adminStoreService.updateStatus(storeId, status);
        }, "更新商店状态不应该抛出异常");
        
        // 验证方法被调用
        verify(adminStoreService, times(1)).updateStatus(storeId, status);
    }

    /**
     * 正向测试：更新不同的商店状态
     */
    @Test
    void testUpdateStatus_DifferentStatuses() {
        // 测试多种状态
        Integer[] statuses = {0, 1, 2}; // 0-停业, 1-营业, 2-审核中
        Long storeId = 1L;
        
        for (Integer status : statuses) {
            // 模拟服务行为
            doNothing().when(adminStoreService).updateStatus(storeId, status);
            
            // 执行测试
            assertDoesNotThrow(() -> {
                adminStoreService.updateStatus(storeId, status);
            }, "更新商店状态为 " + status + " 不应该抛出异常");
        }
        
        // 验证方法被调用次数
        verify(adminStoreService, times(statuses.length)).updateStatus(eq(storeId), anyInt());
    }

    /**
     * 反向测试：storeId为null
     */
    @Test
    void testUpdateStatus_NullStoreId() {
        // 准备测试数据
        Long storeId = null;
        Integer status = 1;
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("商店ID不能为null"))
                .when(adminStoreService).updateStatus(storeId, status);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminStoreService.updateStatus(storeId, status);
        });
        
        assertEquals("商店ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminStoreService, times(1)).updateStatus(storeId, status);
    }

    /**
     * 反向测试：status为null
     */
    @Test
    void testUpdateStatus_NullStatus() {
        // 准备测试数据
        Long storeId = 1L;
        Integer status = null;
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("状态不能为null"))
                .when(adminStoreService).updateStatus(storeId, status);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminStoreService.updateStatus(storeId, status);
        });
        
        assertEquals("状态不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminStoreService, times(1)).updateStatus(storeId, status);
    }

    /**
     * 反向测试：storeId为负数
     */
    @Test
    void testUpdateStatus_NegativeStoreId() {
        // 准备测试数据
        Long storeId = -1L;
        Integer status = 1;
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("商店ID必须为正数"))
                .when(adminStoreService).updateStatus(storeId, status);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminStoreService.updateStatus(storeId, status);
        });
        
        assertEquals("商店ID必须为正数", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminStoreService, times(1)).updateStatus(storeId, status);
    }

    /**
     * 反向测试：无效的状态值
     */
    @Test
    void testUpdateStatus_InvalidStatus() {
        // 准备测试数据
        Long storeId = 1L;
        Integer status = 999; // 无效状态
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("无效的状态值: " + status))
                .when(adminStoreService).updateStatus(storeId, status);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminStoreService.updateStatus(storeId, status);
        });
        
        assertEquals("无效的状态值: " + status, exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminStoreService, times(1)).updateStatus(storeId, status);
    }

    /**
     * 反向测试：负数状态值
     */
    @Test
    void testUpdateStatus_NegativeStatus() {
        // 准备测试数据
        Long storeId = 1L;
        Integer status = -1; // 负数状态
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("状态值不能为负数"))
                .when(adminStoreService).updateStatus(storeId, status);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminStoreService.updateStatus(storeId, status);
        });
        
        assertEquals("状态值不能为负数", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminStoreService, times(1)).updateStatus(storeId, status);
    }

    /**
     * 反向测试：商店不存在
     */
    @Test
    void testUpdateStatus_StoreNotFound() {
        // 准备测试数据
        Long storeId = 999L;
        Integer status = 1;
        
        // 模拟服务抛出异常
        doThrow(new RuntimeException("商店不存在"))
                .when(adminStoreService).updateStatus(storeId, status);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminStoreService.updateStatus(storeId, status);
        });
        
        assertEquals("商店不存在", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminStoreService, times(1)).updateStatus(storeId, status);
    }

    /**
     * 反向测试：数据库操作失败
     */
    @Test
    void testUpdateStatus_DatabaseException() {
        // 准备测试数据
        Long storeId = 1L;
        Integer status = 1;
        
        // 模拟服务抛出异常
        doThrow(new RuntimeException("数据库操作失败"))
                .when(adminStoreService).updateStatus(storeId, status);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminStoreService.updateStatus(storeId, status);
        });
        
        assertEquals("数据库操作失败", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminStoreService, times(1)).updateStatus(storeId, status);
    }

    /**
     * 正向测试：批量更新多个商店状态
     */
    @Test
    void testUpdateStatus_MultipleStores() {
        // 准备测试数据
        Long[] storeIds = {1L, 2L, 3L};
        Integer status = 0; // 停业状态
        
        for (Long storeId : storeIds) {
            // 模拟服务行为
            doNothing().when(adminStoreService).updateStatus(storeId, status);
            
            // 执行测试
            assertDoesNotThrow(() -> {
                adminStoreService.updateStatus(storeId, status);
            }, "更新商店 " + storeId + " 的状态不应该抛出异常");
        }
        
        // 验证方法被调用次数
        verify(adminStoreService, times(storeIds.length)).updateStatus(anyLong(), eq(status));
    }

    /**
     * 正向测试：零ID的商店更新
     */
    @Test
    void testUpdateStatus_ZeroStoreId() {
        // 准备测试数据
        Long storeId = 0L;
        Integer status = 1;
        
        // 模拟服务抛出异常（通常0是无效ID）
        doThrow(new IllegalArgumentException("商店ID必须为正数"))
                .when(adminStoreService).updateStatus(storeId, status);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminStoreService.updateStatus(storeId, status);
        });
        
        assertEquals("商店ID必须为正数", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminStoreService, times(1)).updateStatus(storeId, status);
    }

    /**
     * 正向测试：边界状态值测试
     */
    @Test
    void testUpdateStatus_BoundaryStatusValues() {
        // 准备测试数据
        Long storeId = 1L;
        Integer[] boundaryStatuses = {0, 1, 2}; // 假设有效状态范围是0-2
        
        for (Integer status : boundaryStatuses) {
            // 模拟服务行为
            doNothing().when(adminStoreService).updateStatus(storeId, status);
            
            // 执行测试
            assertDoesNotThrow(() -> {
                adminStoreService.updateStatus(storeId, status);
            }, "边界状态值 " + status + " 应该有效");
        }
        
        // 验证方法被调用次数
        verify(adminStoreService, times(boundaryStatuses.length)).updateStatus(eq(storeId), anyInt());
    }
}
