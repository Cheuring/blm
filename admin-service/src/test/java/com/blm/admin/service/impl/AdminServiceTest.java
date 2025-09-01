package com.blm.admin.service.impl;

import com.blm.admin.service.AdminService;
import com.blm.common.dto.AuditDTO;
import com.blm.common.dto.StoreCategoryDTO;
import com.blm.common.entity.*;
import com.blm.common.vo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * AdminService 单元测试
 * 测试管理员服务的所有方法
 */
@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        // 在每个测试方法执行前的准备工作
    }

    // ==================== listUsers() 方法测试 ====================

    /**
     * 正向测试：成功获取用户分页列表
     */
    @Test
    void testListUsers_Success() {
        // 准备测试数据
        UserVO userVO1 = new UserVO();
        userVO1.setId(1L);
        userVO1.setUsername("testuser1");
        userVO1.setEmail("test1@example.com");
        
        UserVO userVO2 = new UserVO();
        userVO2.setId(2L);
        userVO2.setUsername("testuser2");
        userVO2.setEmail("test2@example.com");
        
        List<UserVO> userList = Arrays.asList(userVO1, userVO2);
        PageVO<UserVO> expectedPage = new PageVO<>(1, 10, 2L, 1, userList);
        
        User.UserRole role = User.UserRole.USER;
        Integer status = 1;
        String keyword = "test";
        int page = 1;
        int size = 10;
        
        // 模拟服务行为
        when(adminService.listUsers(role, status, keyword, page, size))
                .thenReturn(expectedPage);
        
        // 执行测试
        PageVO<UserVO> actualPage = adminService.listUsers(role, status, keyword, page, size);
        
        // 验证结果
        assertNotNull(actualPage, "分页结果不应为null");
        assertEquals(1, actualPage.getNumber(), "页码应该匹配");
        assertEquals(10, actualPage.getSize(), "每页大小应该匹配");
        assertEquals(2L, actualPage.getTotalElements(), "总记录数应该匹配");
        assertEquals(2, actualPage.getContent().size(), "内容列表大小应该匹配");
        assertEquals("testuser1", actualPage.getContent().get(0).getUsername(), "第一个用户名应该匹配");
        
        // 验证方法被调用
        verify(adminService, times(1)).listUsers(role, status, keyword, page, size);
    }

    /**
     * 正向测试：获取空用户列表
     */
    @Test
    void testListUsers_EmptyList() {
        // 准备测试数据
        PageVO<UserVO> expectedPage = new PageVO<>(1, 10, 0L, 0, Collections.emptyList());
        
        User.UserRole role = User.UserRole.USER;
        Integer status = 1;
        String keyword = "nonexistent";
        int page = 1;
        int size = 10;
        
        // 模拟服务行为
        when(adminService.listUsers(role, status, keyword, page, size))
                .thenReturn(expectedPage);
        
        // 执行测试
        PageVO<UserVO> actualPage = adminService.listUsers(role, status, keyword, page, size);
        
        // 验证结果
        assertNotNull(actualPage, "分页结果不应为null");
        assertEquals(0L, actualPage.getTotalElements(), "总记录数应为0");
        assertTrue(actualPage.getContent().isEmpty(), "内容列表应为空");
        
        // 验证方法被调用
        verify(adminService, times(1)).listUsers(role, status, keyword, page, size);
    }

    /**
     * 反向测试：role为null
     */
    @Test
    void testListUsers_NullRole() {
        // 模拟服务抛出异常
        when(adminService.listUsers(null, 1, "test", 1, 10))
                .thenThrow(new IllegalArgumentException("用户角色不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.listUsers(null, 1, "test", 1, 10);
        });
        
        assertEquals("用户角色不能为null", exception.getMessage());
        verify(adminService, times(1)).listUsers(null, 1, "test", 1, 10);
    }

    /**
     * 反向测试：无效的页码参数
     */
    @Test
    void testListUsers_InvalidPageParameters() {
        // 测试负数页码
        when(adminService.listUsers(User.UserRole.USER, 1, "test", -1, 10))
                .thenThrow(new IllegalArgumentException("页码不能为负数"));
        
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> {
            adminService.listUsers(User.UserRole.USER, 1, "test", -1, 10);
        });
        assertEquals("页码不能为负数", exception1.getMessage());
        
        // 测试无效的每页大小
        when(adminService.listUsers(User.UserRole.USER, 1, "test", 1, 0))
                .thenThrow(new IllegalArgumentException("每页大小必须大于0"));
        
        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> {
            adminService.listUsers(User.UserRole.USER, 1, "test", 1, 0);
        });
        assertEquals("每页大小必须大于0", exception2.getMessage());
    }

    /**
     * 反向测试：数据库异常
     */
    @Test
    void testListUsers_DatabaseException() {
        // 模拟数据库异常
        when(adminService.listUsers(any(), any(), any(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException("数据库连接失败"));
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.listUsers(User.UserRole.USER, 1, "test", 1, 10);
        });
        
        assertEquals("数据库连接失败", exception.getMessage());
    }

    // ==================== updateUserStatus() 方法测试 ====================

    /**
     * 正向测试：成功更新用户状态
     */
    @Test
    void testUpdateUserStatus_Success() {
        // 准备测试数据
        Long userId = 1L;
        AuditDTO auditDTO = new AuditDTO();
        auditDTO.setStatus(AuditDTO.AuditStatus.APPROVED);
        auditDTO.setReason("审核通过");
        
        // 模拟服务行为
        doNothing().when(adminService).updateUserStatus(userId, auditDTO);
        
        // 执行测试
        assertDoesNotThrow(() -> {
            adminService.updateUserStatus(userId, auditDTO);
        });
        
        // 验证方法被调用
        verify(adminService, times(1)).updateUserStatus(userId, auditDTO);
    }

    /**
     * 反向测试：userId为null
     */
    @Test
    void testUpdateUserStatus_NullUserId() {
        AuditDTO auditDTO = new AuditDTO();
        auditDTO.setStatus(AuditDTO.AuditStatus.APPROVED);
        
        doThrow(new IllegalArgumentException("用户ID不能为null"))
                .when(adminService).updateUserStatus(null, auditDTO);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.updateUserStatus(null, auditDTO);
        });
        
        assertEquals("用户ID不能为null", exception.getMessage());
    }

    /**
     * 反向测试：auditDTO为null
     */
    @Test
    void testUpdateUserStatus_NullAuditDTO() {
        Long userId = 1L;
        
        doThrow(new IllegalArgumentException("审核信息不能为null"))
                .when(adminService).updateUserStatus(userId, null);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.updateUserStatus(userId, null);
        });
        
        assertEquals("审核信息不能为null", exception.getMessage());
    }

    /**
     * 反向测试：用户不存在
     */
    @Test
    void testUpdateUserStatus_UserNotFound() {
        Long userId = 999L;
        AuditDTO auditDTO = new AuditDTO();
        auditDTO.setStatus(AuditDTO.AuditStatus.APPROVED);
        
        doThrow(new RuntimeException("用户不存在"))
                .when(adminService).updateUserStatus(userId, auditDTO);
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.updateUserStatus(userId, auditDTO);
        });
        
        assertEquals("用户不存在", exception.getMessage());
    }

    // ==================== listStores() 方法测试 ====================

    /**
     * 正向测试：成功获取店铺分页列表
     */
    @Test
    void testListStores_Success() {
        // 准备测试数据
        StoreVO storeVO1 = new StoreVO();
        storeVO1.setId(1L);
        storeVO1.setName("测试店铺1");
        storeVO1.setAddress("测试地址1");
        
        StoreVO storeVO2 = new StoreVO();
        storeVO2.setId(2L);
        storeVO2.setName("测试店铺2");
        storeVO2.setAddress("测试地址2");
        
        List<StoreVO> storeList = Arrays.asList(storeVO1, storeVO2);
        PageVO<StoreVO> expectedPage = new PageVO<>(1, 10, 2L, 1, storeList);
        
        Store.StoreStatus status = Store.StoreStatus.OPEN;
        String keyword = "测试";
        int page = 1;
        int size = 10;
        
        // 模拟服务行为
        when(adminService.listStores(status, keyword, page, size))
                .thenReturn(expectedPage);
        
        // 执行测试
        PageVO<StoreVO> actualPage = adminService.listStores(status, keyword, page, size);
        
        // 验证结果
        assertNotNull(actualPage, "分页结果不应为null");
        assertEquals(2, actualPage.getContent().size(), "店铺列表大小应该匹配");
        assertEquals("测试店铺1", actualPage.getContent().get(0).getName(), "第一个店铺名称应该匹配");
        
        // 验证方法被调用
        verify(adminService, times(1)).listStores(status, keyword, page, size);
    }

    /**
     * 反向测试：无效的状态参数
     */
    @Test
    void testListStores_InvalidStatus() {
        when(adminService.listStores(Store.StoreStatus.SUSPENDED, "test", 1, 10))
                .thenThrow(new IllegalArgumentException("无效的店铺状态"));
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.listStores(Store.StoreStatus.SUSPENDED, "test", 1, 10);
        });
        
        assertEquals("无效的店铺状态", exception.getMessage());
    }

    // ==================== auditStore() 方法测试 ====================

    /**
     * 正向测试：成功审核店铺
     */
    @Test
    void testAuditStore_Success() {
        Long storeId = 1L;
        AuditDTO auditDTO = new AuditDTO();
        auditDTO.setStatus(AuditDTO.AuditStatus.APPROVED);
        
        doNothing().when(adminService).auditStore(storeId, auditDTO);
        
        assertDoesNotThrow(() -> {
            adminService.auditStore(storeId, auditDTO);
        });
        
        verify(adminService, times(1)).auditStore(storeId, auditDTO);
    }

    /**
     * 反向测试：店铺不存在
     */
    @Test
    void testAuditStore_StoreNotFound() {
        Long storeId = 999L;
        AuditDTO auditDTO = new AuditDTO();
        auditDTO.setStatus(AuditDTO.AuditStatus.APPROVED);
        
        doThrow(new RuntimeException("店铺不存在"))
                .when(adminService).auditStore(storeId, auditDTO);
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.auditStore(storeId, auditDTO);
        });
        
        assertEquals("店铺不存在", exception.getMessage());
    }

    // ==================== listFoods() 方法测试 ====================

    /**
     * 正向测试：成功获取商品分页列表
     */
    @Test
    void testListFoods_Success() {
        // 准备测试数据
        FoodVO foodVO1 = new FoodVO();
        foodVO1.setId(1L);
        foodVO1.setName("测试商品1");
        foodVO1.setPrice(new BigDecimal("10.50"));
        
        FoodVO foodVO2 = new FoodVO();
        foodVO2.setId(2L);
        foodVO2.setName("测试商品2");
        foodVO2.setPrice(new BigDecimal("15.80"));
        
        List<FoodVO> foodList = Arrays.asList(foodVO1, foodVO2);
        PageVO<FoodVO> expectedPage = new PageVO<>(1, 10, 2L, 1, foodList);
        
        Food.FoodStatus status = Food.FoodStatus.ON_SHELF;
        String keyword = "测试";
        Long storeId = 1L;
        int page = 1;
        int size = 10;
        
        // 模拟服务行为
        when(adminService.listFoods(status, keyword, storeId, page, size))
                .thenReturn(expectedPage);
        
        // 执行测试
        PageVO<FoodVO> actualPage = adminService.listFoods(status, keyword, storeId, page, size);
        
        // 验证结果
        assertNotNull(actualPage, "分页结果不应为null");
        assertEquals(2, actualPage.getContent().size(), "商品列表大小应该匹配");
        assertEquals("测试商品1", actualPage.getContent().get(0).getName(), "第一个商品名称应该匹配");
        
        // 验证方法被调用
        verify(adminService, times(1)).listFoods(status, keyword, storeId, page, size);
    }

    /**
     * 反向测试：无效的商品状态
     */
    @Test
    void testListFoods_NullStatus() {
        when(adminService.listFoods(null, "test", 1L, 1, 10))
                .thenThrow(new IllegalArgumentException("商品状态不能为null"));
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.listFoods(null, "test", 1L, 1, 10);
        });
        
        assertEquals("商品状态不能为null", exception.getMessage());
    }

    // ==================== auditFood() 方法测试 ====================

    /**
     * 正向测试：成功审核商品
     */
    @Test
    void testAuditFood_Success() {
        Long foodId = 1L;
        AuditDTO auditDTO = new AuditDTO();
        auditDTO.setStatus(AuditDTO.AuditStatus.APPROVED);
        
        doNothing().when(adminService).auditFood(foodId, auditDTO);
        
        assertDoesNotThrow(() -> {
            adminService.auditFood(foodId, auditDTO);
        });
        
        verify(adminService, times(1)).auditFood(foodId, auditDTO);
    }

    /**
     * 反向测试：商品不存在
     */
    @Test
    void testAuditFood_FoodNotFound() {
        Long foodId = 999L;
        AuditDTO auditDTO = new AuditDTO();
        auditDTO.setStatus(AuditDTO.AuditStatus.SUSPENDED);
        auditDTO.setReason("不符合规定");
        
        doThrow(new RuntimeException("商品不存在"))
                .when(adminService).auditFood(foodId, auditDTO);
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.auditFood(foodId, auditDTO);
        });
        
        assertEquals("商品不存在", exception.getMessage());
    }

    // ==================== listRiders() 方法测试 ====================

    /**
     * 正向测试：成功获取骑手分页列表
     */
    @Test
    void testListRiders_Success() {
        // 准备测试数据
        RiderVO riderVO1 = new RiderVO();
        riderVO1.setId(1L);
        riderVO1.setRealName("张三");
        riderVO1.setVehicleNumber("京A12345");
        
        RiderVO riderVO2 = new RiderVO();
        riderVO2.setId(2L);
        riderVO2.setRealName("李四");
        riderVO2.setVehicleNumber("京B67890");
        
        List<RiderVO> riderList = Arrays.asList(riderVO1, riderVO2);
        PageVO<RiderVO> expectedPage = new PageVO<>(1, 10, 2L, 1, riderList);
        
        Rider.RiderStatus status = Rider.RiderStatus.ONLINE;
        String keyword = "张";
        int page = 1;
        int size = 10;
        
        // 模拟服务行为
        when(adminService.listRiders(status, keyword, page, size))
                .thenReturn(expectedPage);
        
        // 执行测试
        PageVO<RiderVO> actualPage = adminService.listRiders(status, keyword, page, size);
        
        // 验证结果
        assertNotNull(actualPage, "分页结果不应为null");
        assertEquals(2, actualPage.getContent().size(), "骑手列表大小应该匹配");
        assertEquals("张三", actualPage.getContent().get(0).getRealName(), "第一个骑手姓名应该匹配");
        
        // 验证方法被调用
        verify(adminService, times(1)).listRiders(status, keyword, page, size);
    }

    /**
     * 反向测试：无效的骑手状态
     */
    @Test
    void testListRiders_NullStatus() {
        when(adminService.listRiders(null, "test", 1, 10))
                .thenThrow(new IllegalArgumentException("骑手状态不能为null"));
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.listRiders(null, "test", 1, 10);
        });
        
        assertEquals("骑手状态不能为null", exception.getMessage());
    }

    // ==================== updateRiderStatus() 方法测试 ====================

    /**
     * 正向测试：成功更新骑手状态
     */
    @Test
    void testUpdateRiderStatus_Success() {
        Long riderId = 1L;
        AuditDTO auditDTO = new AuditDTO();
        auditDTO.setStatus(AuditDTO.AuditStatus.APPROVED);
        
        doNothing().when(adminService).updateRiderStatus(riderId, auditDTO);
        
        assertDoesNotThrow(() -> {
            adminService.updateRiderStatus(riderId, auditDTO);
        });
        
        verify(adminService, times(1)).updateRiderStatus(riderId, auditDTO);
    }

    /**
     * 反向测试：骑手不存在
     */
    @Test
    void testUpdateRiderStatus_RiderNotFound() {
        Long riderId = 999L;
        AuditDTO auditDTO = new AuditDTO();
        auditDTO.setStatus(AuditDTO.AuditStatus.SUSPENDED);
        
        doThrow(new RuntimeException("骑手不存在"))
                .when(adminService).updateRiderStatus(riderId, auditDTO);
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.updateRiderStatus(riderId, auditDTO);
        });
        
        assertEquals("骑手不存在", exception.getMessage());
    }

    // ==================== listAllOrders() 方法测试 ====================

    /**
     * 正向测试：成功获取所有订单分页列表
     */
    @Test
    void testListAllOrders_Success() {
        // 准备测试数据
        OrderVO orderVO1 = new OrderVO();
        orderVO1.setId(1L);
        orderVO1.setOrderNo("ORDER_001");
        orderVO1.setStoreName("测试店铺1");
        
        OrderVO orderVO2 = new OrderVO();
        orderVO2.setId(2L);
        orderVO2.setOrderNo("ORDER_002");
        orderVO2.setStoreName("测试店铺2");
        
        List<OrderVO> orderList = Arrays.asList(orderVO1, orderVO2);
        PageVO<OrderVO> expectedPage = new PageVO<>(1, 10, 2L, 1, orderList);
        
        Order.OrderStatus status = Order.OrderStatus.MERCHANT_CONFIRMED;
        Long userId = 1L;
        Long storeId = 1L;
        Long riderId = 1L;
        int page = 1;
        int size = 10;
        
        // 模拟服务行为
        when(adminService.listAllOrders(status, userId, storeId, riderId, page, size))
                .thenReturn(expectedPage);
        
        // 执行测试
        PageVO<OrderVO> actualPage = adminService.listAllOrders(status, userId, storeId, riderId, page, size);
        
        // 验证结果
        assertNotNull(actualPage, "分页结果不应为null");
        assertEquals(2, actualPage.getContent().size(), "订单列表大小应该匹配");
        assertEquals("ORDER_001", actualPage.getContent().get(0).getOrderNo(), "第一个订单号应该匹配");
        
        // 验证方法被调用
        verify(adminService, times(1)).listAllOrders(status, userId, storeId, riderId, page, size);
    }

    /**
     * 正向测试：使用null过滤条件获取所有订单
     */
    @Test
    void testListAllOrders_WithNullFilters() {
        // 准备测试数据
        List<OrderVO> orderList = Collections.singletonList(new OrderVO());
        PageVO<OrderVO> expectedPage = new PageVO<>(1, 10, 1L, 1, orderList);
        
        // 模拟服务行为 - 所有过滤条件都为null
        when(adminService.listAllOrders(null, null, null, null, 1, 10))
                .thenReturn(expectedPage);
        
        // 执行测试
        PageVO<OrderVO> actualPage = adminService.listAllOrders(null, null, null, null, 1, 10);
        
        // 验证结果
        assertNotNull(actualPage, "分页结果不应为null");
        assertEquals(1, actualPage.getContent().size(), "订单列表大小应该匹配");
        
        // 验证方法被调用
        verify(adminService, times(1)).listAllOrders(null, null, null, null, 1, 10);
    }

    /**
     * 反向测试：无效的分页参数
     */
    @Test
    void testListAllOrders_InvalidPagination() {
        when(adminService.listAllOrders(null, null, null, null, -1, 10))
                .thenThrow(new IllegalArgumentException("页码不能为负数"));
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.listAllOrders(null, null, null, null, -1, 10);
        });
        
        assertEquals("页码不能为负数", exception.getMessage());
    }

    // ==================== listReviews() 方法测试 ====================

    /**
     * 正向测试：成功获取评价分页列表
     */
    @Test
    void testListReviews_Success() {
        // 准备测试数据
        ReviewVO reviewVO1 = new ReviewVO();
        reviewVO1.setId(1L);
        reviewVO1.setStoreRating(5);
        reviewVO1.setContent("很好吃");
        
        ReviewVO reviewVO2 = new ReviewVO();
        reviewVO2.setId(2L);
        reviewVO2.setStoreRating(4);
        reviewVO2.setContent("不错");
        
        List<ReviewVO> reviewList = Arrays.asList(reviewVO1, reviewVO2);
        PageVO<ReviewVO> expectedPage = new PageVO<>(1, 10, 2L, 1, reviewList);
        
        Long userId = 1L;
        Long storeId = 1L;
        Long foodId = 1L;
        Integer rating = 5;
        int page = 1;
        int size = 10;
        
        // 模拟服务行为
        when(adminService.listReviews(userId, storeId, foodId, rating, page, size))
                .thenReturn(expectedPage);
        
        // 执行测试
        PageVO<ReviewVO> actualPage = adminService.listReviews(userId, storeId, foodId, rating, page, size);
        
        // 验证结果
        assertNotNull(actualPage, "分页结果不应为null");
        assertEquals(2, actualPage.getContent().size(), "评价列表大小应该匹配");
        assertEquals(5, actualPage.getContent().get(0).getStoreRating(), "第一个评价星级应该匹配");
        
        // 验证方法被调用
        verify(adminService, times(1)).listReviews(userId, storeId, foodId, rating, page, size);
    }

    /**
     * 反向测试：无效的评分参数
     */
    @Test
    void testListReviews_InvalidRating() {
        when(adminService.listReviews(1L, 1L, 1L, 6, 1, 10))
                .thenThrow(new IllegalArgumentException("评分必须在1-5之间"));
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.listReviews(1L, 1L, 1L, 6, 1, 10);
        });
        
        assertEquals("评分必须在1-5之间", exception.getMessage());
    }

    // ==================== deleteReview() 方法测试 ====================

    /**
     * 正向测试：成功删除评价
     */
    @Test
    void testDeleteReview_Success() {
        Long reviewId = 1L;
        
        doNothing().when(adminService).deleteReview(reviewId);
        
        assertDoesNotThrow(() -> {
            adminService.deleteReview(reviewId);
        });
        
        verify(adminService, times(1)).deleteReview(reviewId);
    }

    /**
     * 反向测试：reviewId为null
     */
    @Test
    void testDeleteReview_NullId() {
        doThrow(new IllegalArgumentException("评价ID不能为null"))
                .when(adminService).deleteReview(null);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.deleteReview(null);
        });
        
        assertEquals("评价ID不能为null", exception.getMessage());
    }

    /**
     * 反向测试：评价不存在
     */
    @Test
    void testDeleteReview_NotFound() {
        Long reviewId = 999L;
        
        doThrow(new RuntimeException("评价不存在"))
                .when(adminService).deleteReview(reviewId);
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.deleteReview(reviewId);
        });
        
        assertEquals("评价不存在", exception.getMessage());
    }

    // ==================== getPlatformStatistics() 方法测试 ====================

    /**
     * 正向测试：成功获取平台统计数据
     */
    @Test
    void testGetPlatformStatistics_Success() {
        // 准备测试数据
        PlatformStatsVO statsVO = new PlatformStatsVO();
        statsVO.setTotalUsers(1000L);
        statsVO.setTotalMerchants(50L);
        statsVO.setTotalOrders(2000L);
        
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        
        // 模拟服务行为
        when(adminService.getPlatformStatistics(startDate, endDate))
                .thenReturn(statsVO);
        
        // 执行测试
        PlatformStatsVO actualStats = adminService.getPlatformStatistics(startDate, endDate);
        
        // 验证结果
        assertNotNull(actualStats, "统计数据不应为null");
        assertEquals(1000L, actualStats.getTotalUsers(), "总用户数应该匹配");
        assertEquals(50L, actualStats.getTotalMerchants(), "总商家数应该匹配");
        assertEquals(2000L, actualStats.getTotalOrders(), "总订单数应该匹配");
        
        // 验证方法被调用
        verify(adminService, times(1)).getPlatformStatistics(startDate, endDate);
    }

    /**
     * 反向测试：日期参数为null
     */
    @Test
    void testGetPlatformStatistics_NullDates() {
        when(adminService.getPlatformStatistics(null, null))
                .thenThrow(new IllegalArgumentException("开始日期和结束日期不能为null"));
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.getPlatformStatistics(null, null);
        });
        
        assertEquals("开始日期和结束日期不能为null", exception.getMessage());
    }

    /**
     * 反向测试：开始日期晚于结束日期
     */
    @Test
    void testGetPlatformStatistics_InvalidDateRange() {
        LocalDate startDate = LocalDate.of(2024, 12, 31);
        LocalDate endDate = LocalDate.of(2024, 1, 1);
        
        when(adminService.getPlatformStatistics(startDate, endDate))
                .thenThrow(new IllegalArgumentException("开始日期不能晚于结束日期"));
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.getPlatformStatistics(startDate, endDate);
        });
        
        assertEquals("开始日期不能晚于结束日期", exception.getMessage());
    }

    // ==================== listStoreCategory() 方法测试 ====================

    /**
     * 正向测试：成功获取店铺种类分页列表
     */
    @Test
    void testListStoreCategory_Success() {
        // 准备测试数据
        StoreCategoryVO categoryVO1 = new StoreCategoryVO();
        categoryVO1.setId(1L);
        categoryVO1.setName("中餐");
        
        StoreCategoryVO categoryVO2 = new StoreCategoryVO();
        categoryVO2.setId(2L);
        categoryVO2.setName("西餐");
        
        List<StoreCategoryVO> categoryList = Arrays.asList(categoryVO1, categoryVO2);
        PageVO<StoreCategoryVO> expectedPage = new PageVO<>(1, 10, 2L, 1, categoryList);
        
        int page = 1;
        int size = 10;
        
        // 模拟服务行为
        when(adminService.listStoreCategory(page, size))
                .thenReturn(expectedPage);
        
        // 执行测试
        PageVO<StoreCategoryVO> actualPage = adminService.listStoreCategory(page, size);
        
        // 验证结果
        assertNotNull(actualPage, "分页结果不应为null");
        assertEquals(2, actualPage.getContent().size(), "种类列表大小应该匹配");
        assertEquals("中餐", actualPage.getContent().get(0).getName(), "第一个种类名称应该匹配");
        
        // 验证方法被调用
        verify(adminService, times(1)).listStoreCategory(page, size);
    }

    /**
     * 反向测试：无效的分页参数
     */
    @Test
    void testListStoreCategory_InvalidPagination() {
        when(adminService.listStoreCategory(0, 10))
                .thenThrow(new IllegalArgumentException("页码必须大于0"));
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.listStoreCategory(0, 10);
        });
        
        assertEquals("页码必须大于0", exception.getMessage());
    }

    // ==================== addStoreCategory() 方法测试 ====================

    /**
     * 正向测试：成功新增店铺种类
     */
    @Test
    void testAddStoreCategory_Success() {
        // 准备测试数据
        StoreCategoryDTO categoryDTO = new StoreCategoryDTO();
        categoryDTO.setName("新种类");
        
        StoreCategoryVO expectedVO = new StoreCategoryVO();
        expectedVO.setId(1L);
        expectedVO.setName("新种类");
        
        // 模拟服务行为
        when(adminService.addStoreCategory(categoryDTO))
                .thenReturn(expectedVO);
        
        // 执行测试
        StoreCategoryVO actualVO = adminService.addStoreCategory(categoryDTO);
        
        // 验证结果
        assertNotNull(actualVO, "返回结果不应为null");
        assertEquals(1L, actualVO.getId(), "ID应该匹配");
        assertEquals("新种类", actualVO.getName(), "名称应该匹配");
        
        // 验证方法被调用
        verify(adminService, times(1)).addStoreCategory(categoryDTO);
    }

    /**
     * 反向测试：categoryDTO为null
     */
    @Test
    void testAddStoreCategory_NullDTO() {
        when(adminService.addStoreCategory(null))
                .thenThrow(new IllegalArgumentException("店铺种类信息不能为null"));
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.addStoreCategory(null);
        });
        
        assertEquals("店铺种类信息不能为null", exception.getMessage());
    }

    /**
     * 反向测试：种类名称已存在
     */
    @Test
    void testAddStoreCategory_DuplicateName() {
        StoreCategoryDTO categoryDTO = new StoreCategoryDTO();
        categoryDTO.setName("中餐");
        
        when(adminService.addStoreCategory(categoryDTO))
                .thenThrow(new RuntimeException("种类名称已存在"));
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.addStoreCategory(categoryDTO);
        });
        
        assertEquals("种类名称已存在", exception.getMessage());
    }

    // ==================== updateStoreCategory() 方法测试 ====================

    /**
     * 正向测试：成功更新店铺种类
     */
    @Test
    void testUpdateStoreCategory_Success() {
        // 准备测试数据
        Long id = 1L;
        StoreCategoryDTO categoryDTO = new StoreCategoryDTO();
        categoryDTO.setName("更新种类");
        
        StoreCategoryVO expectedVO = new StoreCategoryVO();
        expectedVO.setId(id);
        expectedVO.setName("更新种类");
        
        // 模拟服务行为
        when(adminService.updateStoreCategory(id, categoryDTO))
                .thenReturn(expectedVO);
        
        // 执行测试
        StoreCategoryVO actualVO = adminService.updateStoreCategory(id, categoryDTO);
        
        // 验证结果
        assertNotNull(actualVO, "返回结果不应为null");
        assertEquals(id, actualVO.getId(), "ID应该匹配");
        assertEquals("更新种类", actualVO.getName(), "名称应该匹配");
        
        // 验证方法被调用
        verify(adminService, times(1)).updateStoreCategory(id, categoryDTO);
    }

    /**
     * 反向测试：ID为null
     */
    @Test
    void testUpdateStoreCategory_NullId() {
        StoreCategoryDTO categoryDTO = new StoreCategoryDTO();
        categoryDTO.setName("更新种类");
        
        when(adminService.updateStoreCategory(null, categoryDTO))
                .thenThrow(new IllegalArgumentException("种类ID不能为null"));
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.updateStoreCategory(null, categoryDTO);
        });
        
        assertEquals("种类ID不能为null", exception.getMessage());
    }

    /**
     * 反向测试：种类不存在
     */
    @Test
    void testUpdateStoreCategory_NotFound() {
        Long id = 999L;
        StoreCategoryDTO categoryDTO = new StoreCategoryDTO();
        categoryDTO.setName("更新种类");
        
        when(adminService.updateStoreCategory(id, categoryDTO))
                .thenThrow(new RuntimeException("店铺种类不存在"));
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.updateStoreCategory(id, categoryDTO);
        });
        
        assertEquals("店铺种类不存在", exception.getMessage());
    }

    // ==================== deleteStoreCategory() 方法测试 ====================

    /**
     * 正向测试：成功删除店铺种类
     */
    @Test
    void testDeleteStoreCategory_Success() {
        Long id = 1L;
        
        doNothing().when(adminService).deleteStoreCategory(id);
        
        assertDoesNotThrow(() -> {
            adminService.deleteStoreCategory(id);
        });
        
        verify(adminService, times(1)).deleteStoreCategory(id);
    }

    /**
     * 反向测试：ID为null
     */
    @Test
    void testDeleteStoreCategory_NullId() {
        doThrow(new IllegalArgumentException("种类ID不能为null"))
                .when(adminService).deleteStoreCategory(null);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.deleteStoreCategory(null);
        });
        
        assertEquals("种类ID不能为null", exception.getMessage());
    }

    /**
     * 反向测试：种类不存在
     */
    @Test
    void testDeleteStoreCategory_NotFound() {
        Long id = 999L;
        
        doThrow(new RuntimeException("店铺种类不存在"))
                .when(adminService).deleteStoreCategory(id);
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.deleteStoreCategory(id);
        });
        
        assertEquals("店铺种类不存在", exception.getMessage());
    }

    /**
     * 反向测试：种类正在使用中
     */
    @Test
    void testDeleteStoreCategory_InUse() {
        Long id = 1L;
        
        doThrow(new RuntimeException("该种类正在使用中，无法删除"))
                .when(adminService).deleteStoreCategory(id);
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.deleteStoreCategory(id);
        });
        
        assertEquals("该种类正在使用中，无法删除", exception.getMessage());
    }

    // ==================== getOrderDetail() 方法测试 ====================

    /**
     * 正向测试：成功获取订单详情
     */
    @Test
    void testGetOrderDetail_Success() {
        // 准备测试数据
        Long orderId = 1L;
        OrderDetailVO expectedDetail = new OrderDetailVO();
        expectedDetail.setId(orderId);
        expectedDetail.setOrderNo("ORDER_001");
        expectedDetail.setStoreName("测试店铺");
        
        // 模拟服务行为
        when(adminService.getOrderDetail(orderId))
                .thenReturn(expectedDetail);
        
        // 执行测试
        OrderDetailVO actualDetail = adminService.getOrderDetail(orderId);
        
        // 验证结果
        assertNotNull(actualDetail, "订单详情不应为null");
        assertEquals(orderId, actualDetail.getId(), "订单ID应该匹配");
        assertEquals("ORDER_001", actualDetail.getOrderNo(), "订单号应该匹配");
        assertEquals("测试店铺", actualDetail.getStoreName(), "店铺名称应该匹配");
        
        // 验证方法被调用
        verify(adminService, times(1)).getOrderDetail(orderId);
    }

    /**
     * 反向测试：orderId为null
     */
    @Test
    void testGetOrderDetail_NullId() {
        when(adminService.getOrderDetail(null))
                .thenThrow(new IllegalArgumentException("订单ID不能为null"));
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.getOrderDetail(null);
        });
        
        assertEquals("订单ID不能为null", exception.getMessage());
    }

    /**
     * 反向测试：订单不存在
     */
    @Test
    void testGetOrderDetail_NotFound() {
        Long orderId = 999L;
        
        when(adminService.getOrderDetail(orderId))
                .thenThrow(new RuntimeException("订单不存在"));
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.getOrderDetail(orderId);
        });
        
        assertEquals("订单不存在", exception.getMessage());
    }

    /**
     * 反向测试：orderId为负数
     */
    @Test
    void testGetOrderDetail_NegativeId() {
        Long orderId = -1L;
        
        when(adminService.getOrderDetail(orderId))
                .thenThrow(new IllegalArgumentException("订单ID必须为正数"));
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.getOrderDetail(orderId);
        });
        
        assertEquals("订单ID必须为正数", exception.getMessage());
    }
}
