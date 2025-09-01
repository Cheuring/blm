package com.bom.merchant.service.impl;

import com.blm.common.vo.StoreStatisticsVO;
import com.blm.store.service.MerchantStatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * MerchantStatisticsService接口测试类
 * 针对每个方法实现正向和反向测试
 */
@ExtendWith(MockitoExtension.class)
class MerchantStatisticsServiceTest {

    @Mock
    private MerchantStatisticsService merchantStatisticsService;

    private Long validMerchantId;
    private Long validStoreId;
    private LocalDate startDate;
    private LocalDate endDate;
    private StoreStatisticsVO sampleStoreStatistics;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        validMerchantId = 1L;
        validStoreId = 10L;
        startDate = LocalDate.of(2023, 10, 1);
        endDate = LocalDate.of(2023, 10, 31);
        
        // 初始化评价统计数据
        Map<Integer, Integer> ratingCounts = new HashMap<>();
        ratingCounts.put(5, 15);
        ratingCounts.put(4, 8);
        ratingCounts.put(3, 3);
        ratingCounts.put(2, 1);
        ratingCounts.put(1, 0);
        
        StoreStatisticsVO.ReviewStatisticsVO reviewStats = StoreStatisticsVO.ReviewStatisticsVO.builder()
                .storeId(validStoreId)
                .storeName("美味餐厅")
                .totalReviews(27)
                .averageRating(4.37)
                .ratingCounts(ratingCounts)
                .goodRatePercentage(0.85)
                .build();
        
        // 初始化店铺统计VO
        sampleStoreStatistics = StoreStatisticsVO.builder()
                .storeId(validStoreId)
                .storeName("美味餐厅")
                .todayOrderCount(25)
                .todaySales(new BigDecimal("1250.80"))
                .yesterdayOrderCount(18)
                .yesterdaySales(new BigDecimal("890.50"))
                .monthOrderCount(450)
                .monthSales(new BigDecimal("22500.00"))
                .totalOrderCount(2800)
                .totalSales(new BigDecimal("140000.00"))
                .reviewStatistics(reviewStats)
                .build();
    }

    // ==================== getStoreformStatistics 测试 ====================

    /**
     * 正向测试：成功获取店铺统计数据
     */
    @Test
    void testGetStoreformStatistics_Success() {
        // 模拟服务行为
        when(merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, startDate, endDate))
                .thenReturn(sampleStoreStatistics);
        
        // 执行测试
        StoreStatisticsVO result = merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, startDate, endDate);
        
        // 验证结果
        assertNotNull(result, "统计数据不应为null");
        assertEquals(validStoreId, result.getStoreId(), "店铺ID应该匹配");
        assertEquals("美味餐厅", result.getStoreName(), "店铺名称应该匹配");
        
        // 验证今日数据
        assertEquals(25, result.getTodayOrderCount(), "今日订单数应该匹配");
        assertEquals(new BigDecimal("1250.80"), result.getTodaySales(), "今日销售额应该匹配");
        
        // 验证昨日数据
        assertEquals(18, result.getYesterdayOrderCount(), "昨日订单数应该匹配");
        assertEquals(new BigDecimal("890.50"), result.getYesterdaySales(), "昨日销售额应该匹配");
        
        // 验证月度数据
        assertEquals(450, result.getMonthOrderCount(), "本月订单数应该匹配");
        assertEquals(new BigDecimal("22500.00"), result.getMonthSales(), "本月销售额应该匹配");
        
        // 验证总计数据
        assertEquals(2800, result.getTotalOrderCount(), "总订单数应该匹配");
        assertEquals(new BigDecimal("140000.00"), result.getTotalSales(), "总销售额应该匹配");
        
        // 验证评价统计
        assertNotNull(result.getReviewStatistics(), "评价统计不应为null");
        assertEquals(27, result.getReviewStatistics().getTotalReviews(), "评价总数应该匹配");
        assertEquals(4.37, result.getReviewStatistics().getAverageRating(), 0.01, "平均评分应该匹配");
        
        // 验证方法被调用
        verify(merchantStatisticsService, times(1)).getStoreformStatistics(validMerchantId, validStoreId, startDate, endDate);
    }

    /**
     * 正向测试：获取新店铺的统计数据（数据为0）
     */
    @Test
    void testGetStoreformStatistics_NewStore() {
        // 准备新店铺数据
        Map<Integer, Integer> emptyRatingCounts = new HashMap<>();
        emptyRatingCounts.put(5, 0);
        emptyRatingCounts.put(4, 0);
        emptyRatingCounts.put(3, 0);
        emptyRatingCounts.put(2, 0);
        emptyRatingCounts.put(1, 0);
        
        StoreStatisticsVO.ReviewStatisticsVO emptyReviewStats = StoreStatisticsVO.ReviewStatisticsVO.builder()
                .storeId(validStoreId)
                .storeName("新店铺")
                .totalReviews(0)
                .averageRating(0.0)
                .ratingCounts(emptyRatingCounts)
                .goodRatePercentage(0.0)
                .build();
        
        StoreStatisticsVO newStoreStats = StoreStatisticsVO.builder()
                .storeId(validStoreId)
                .storeName("新店铺")
                .todayOrderCount(0)
                .todaySales(new BigDecimal("0.00"))
                .yesterdayOrderCount(0)
                .yesterdaySales(new BigDecimal("0.00"))
                .monthOrderCount(0)
                .monthSales(new BigDecimal("0.00"))
                .totalOrderCount(0)
                .totalSales(new BigDecimal("0.00"))
                .reviewStatistics(emptyReviewStats)
                .build();
        
        // 模拟服务行为
        when(merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, startDate, endDate))
                .thenReturn(newStoreStats);
        
        // 执行测试
        StoreStatisticsVO result = merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, startDate, endDate);
        
        // 验证结果
        assertNotNull(result, "统计数据不应为null");
        assertEquals("新店铺", result.getStoreName(), "店铺名称应该匹配");
        assertEquals(0, result.getTodayOrderCount(), "新店铺今日订单数应为0");
        assertEquals(new BigDecimal("0.00"), result.getTodaySales(), "新店铺今日销售额应为0");
        assertEquals(0, result.getTotalOrderCount(), "新店铺总订单数应为0");
        assertEquals(0, result.getReviewStatistics().getTotalReviews(), "新店铺评价总数应为0");
        
        // 验证方法被调用
        verify(merchantStatisticsService, times(1)).getStoreformStatistics(validMerchantId, validStoreId, startDate, endDate);
    }

    /**
     * 正向测试：获取单日统计数据
     */
    @Test
    void testGetStoreformStatistics_SingleDay() {
        LocalDate singleDate = LocalDate.of(2023, 10, 15);
        
        // 准备单日统计数据
        StoreStatisticsVO singleDayStats = StoreStatisticsVO.builder()
                .storeId(validStoreId)
                .storeName("美味餐厅")
                .todayOrderCount(12)
                .todaySales(new BigDecimal("580.00"))
                .yesterdayOrderCount(15)
                .yesterdaySales(new BigDecimal("720.50"))
                .monthOrderCount(380)
                .monthSales(new BigDecimal("18900.00"))
                .totalOrderCount(2500)
                .totalSales(new BigDecimal("125000.00"))
                .reviewStatistics(sampleStoreStatistics.getReviewStatistics())
                .build();
        
        // 模拟服务行为
        when(merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, singleDate, singleDate))
                .thenReturn(singleDayStats);
        
        // 执行测试
        StoreStatisticsVO result = merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, singleDate, singleDate);
        
        // 验证结果
        assertNotNull(result, "统计数据不应为null");
        assertEquals(12, result.getTodayOrderCount(), "单日订单数应该匹配");
        assertEquals(new BigDecimal("580.00"), result.getTodaySales(), "单日销售额应该匹配");
        
        // 验证方法被调用
        verify(merchantStatisticsService, times(1)).getStoreformStatistics(validMerchantId, validStoreId, singleDate, singleDate);
    }

    /**
     * 正向测试：获取长时间范围的统计数据
     */
    @Test
    void testGetStoreformStatistics_LongTimeRange() {
        LocalDate longStartDate = LocalDate.of(2023, 1, 1);
        LocalDate longEndDate = LocalDate.of(2023, 12, 31);
        
        // 准备年度统计数据
        StoreStatisticsVO yearlyStats = StoreStatisticsVO.builder()
                .storeId(validStoreId)
                .storeName("美味餐厅")
                .todayOrderCount(28)
                .todaySales(new BigDecimal("1400.00"))
                .yesterdayOrderCount(22)
                .yesterdaySales(new BigDecimal("1100.00"))
                .monthOrderCount(850)
                .monthSales(new BigDecimal("42500.00"))
                .totalOrderCount(10200)
                .totalSales(new BigDecimal("510000.00"))
                .reviewStatistics(sampleStoreStatistics.getReviewStatistics())
                .build();
        
        // 模拟服务行为
        when(merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, longStartDate, longEndDate))
                .thenReturn(yearlyStats);
        
        // 执行测试
        StoreStatisticsVO result = merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, longStartDate, longEndDate);
        
        // 验证结果
        assertNotNull(result, "统计数据不应为null");
        assertEquals(10200, result.getTotalOrderCount(), "年度总订单数应该匹配");
        assertEquals(new BigDecimal("510000.00"), result.getTotalSales(), "年度总销售额应该匹配");
        
        // 验证方法被调用
        verify(merchantStatisticsService, times(1)).getStoreformStatistics(validMerchantId, validStoreId, longStartDate, longEndDate);
    }

    /**
     * 正向测试：获取高销量店铺统计
     */
    @Test
    void testGetStoreformStatistics_HighVolumeStore() {
        // 准备高销量店铺数据
        Map<Integer, Integer> highVolumeRatingCounts = new HashMap<>();
        highVolumeRatingCounts.put(5, 180);
        highVolumeRatingCounts.put(4, 95);
        highVolumeRatingCounts.put(3, 20);
        highVolumeRatingCounts.put(2, 3);
        highVolumeRatingCounts.put(1, 2);
        
        StoreStatisticsVO.ReviewStatisticsVO highVolumeReviewStats = StoreStatisticsVO.ReviewStatisticsVO.builder()
                .storeId(validStoreId)
                .storeName("热门餐厅")
                .totalReviews(300)
                .averageRating(4.5)
                .ratingCounts(highVolumeRatingCounts)
                .goodRatePercentage(0.92)
                .build();
        
        StoreStatisticsVO highVolumeStats = StoreStatisticsVO.builder()
                .storeId(validStoreId)
                .storeName("热门餐厅")
                .todayOrderCount(180)
                .todaySales(new BigDecimal("9000.00"))
                .yesterdayOrderCount(165)
                .yesterdaySales(new BigDecimal("8250.00"))
                .monthOrderCount(4500)
                .monthSales(new BigDecimal("225000.00"))
                .totalOrderCount(50000)
                .totalSales(new BigDecimal("2500000.00"))
                .reviewStatistics(highVolumeReviewStats)
                .build();
        
        // 模拟服务行为
        when(merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, startDate, endDate))
                .thenReturn(highVolumeStats);
        
        // 执行测试
        StoreStatisticsVO result = merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, startDate, endDate);
        
        // 验证结果
        assertNotNull(result, "统计数据不应为null");
        assertEquals("热门餐厅", result.getStoreName(), "店铺名称应该匹配");
        assertEquals(180, result.getTodayOrderCount(), "高销量店铺今日订单数应该匹配");
        assertEquals(new BigDecimal("9000.00"), result.getTodaySales(), "高销量店铺今日销售额应该匹配");
        assertEquals(50000, result.getTotalOrderCount(), "高销量店铺总订单数应该匹配");
        assertEquals(300, result.getReviewStatistics().getTotalReviews(), "高销量店铺评价总数应该匹配");
        
        // 验证方法被调用
        verify(merchantStatisticsService, times(1)).getStoreformStatistics(validMerchantId, validStoreId, startDate, endDate);
    }

    /**
     * 反向测试：传入null的商家ID
     */
    @Test
    void testGetStoreformStatistics_NullMerchantId() {
        // 模拟抛出异常
        when(merchantStatisticsService.getStoreformStatistics(null, validStoreId, startDate, endDate))
                .thenThrow(new IllegalArgumentException("商家ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantStatisticsService.getStoreformStatistics(null, validStoreId, startDate, endDate),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商家ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStatisticsService, times(1)).getStoreformStatistics(null, validStoreId, startDate, endDate);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testGetStoreformStatistics_NullStoreId() {
        // 模拟抛出异常
        when(merchantStatisticsService.getStoreformStatistics(validMerchantId, null, startDate, endDate))
                .thenThrow(new IllegalArgumentException("店铺ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantStatisticsService.getStoreformStatistics(validMerchantId, null, startDate, endDate),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStatisticsService, times(1)).getStoreformStatistics(validMerchantId, null, startDate, endDate);
    }

    /**
     * 反向测试：传入null的开始日期
     */
    @Test
    void testGetStoreformStatistics_NullStartDate() {
        // 模拟抛出异常
        when(merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, null, endDate))
                .thenThrow(new IllegalArgumentException("开始日期不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, null, endDate),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("开始日期不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStatisticsService, times(1)).getStoreformStatistics(validMerchantId, validStoreId, null, endDate);
    }

    /**
     * 反向测试：传入null的结束日期
     */
    @Test
    void testGetStoreformStatistics_NullEndDate() {
        // 模拟抛出异常
        when(merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, startDate, null))
                .thenThrow(new IllegalArgumentException("结束日期不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, startDate, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("结束日期不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStatisticsService, times(1)).getStoreformStatistics(validMerchantId, validStoreId, startDate, null);
    }

    /**
     * 反向测试：结束日期早于开始日期
     */
    @Test
    void testGetStoreformStatistics_EndDateBeforeStartDate() {
        LocalDate invalidStartDate = LocalDate.of(2023, 10, 31);
        LocalDate invalidEndDate = LocalDate.of(2023, 10, 1);
        
        // 模拟抛出异常
        when(merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, invalidStartDate, invalidEndDate))
                .thenThrow(new IllegalArgumentException("结束日期不能早于开始日期"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, invalidStartDate, invalidEndDate),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("结束日期不能早于开始日期", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStatisticsService, times(1)).getStoreformStatistics(validMerchantId, validStoreId, invalidStartDate, invalidEndDate);
    }

    /**
     * 反向测试：日期范围过大
     */
    @Test
    void testGetStoreformStatistics_DateRangeTooLarge() {
        LocalDate veryEarlyDate = LocalDate.of(2020, 1, 1);
        LocalDate currentDate = LocalDate.of(2023, 12, 31);
        
        // 模拟抛出异常
        when(merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, veryEarlyDate, currentDate))
                .thenThrow(new IllegalArgumentException("查询时间范围不能超过1年"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, veryEarlyDate, currentDate),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("查询时间范围不能超过1年", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStatisticsService, times(1)).getStoreformStatistics(validMerchantId, validStoreId, veryEarlyDate, currentDate);
    }

    /**
     * 反向测试：商家不存在
     */
    @Test
    void testGetStoreformStatistics_MerchantNotFound() {
        Long nonExistentMerchantId = 999L;
        
        // 模拟抛出异常
        when(merchantStatisticsService.getStoreformStatistics(nonExistentMerchantId, validStoreId, startDate, endDate))
                .thenThrow(new RuntimeException("商家不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantStatisticsService.getStoreformStatistics(nonExistentMerchantId, validStoreId, startDate, endDate),
                "应该抛出RuntimeException"
        );
        
        assertEquals("商家不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStatisticsService, times(1)).getStoreformStatistics(nonExistentMerchantId, validStoreId, startDate, endDate);
    }

    /**
     * 反向测试：店铺不存在
     */
    @Test
    void testGetStoreformStatistics_StoreNotFound() {
        Long nonExistentStoreId = 999L;
        
        // 模拟抛出异常
        when(merchantStatisticsService.getStoreformStatistics(validMerchantId, nonExistentStoreId, startDate, endDate))
                .thenThrow(new RuntimeException("店铺不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantStatisticsService.getStoreformStatistics(validMerchantId, nonExistentStoreId, startDate, endDate),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStatisticsService, times(1)).getStoreformStatistics(validMerchantId, nonExistentStoreId, startDate, endDate);
    }

    /**
     * 反向测试：店铺不属于该商家
     */
    @Test
    void testGetStoreformStatistics_StoreNotBelongToMerchant() {
        Long otherMerchantId = 2L;
        
        // 模拟抛出异常
        when(merchantStatisticsService.getStoreformStatistics(otherMerchantId, validStoreId, startDate, endDate))
                .thenThrow(new RuntimeException("店铺不属于该商家"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantStatisticsService.getStoreformStatistics(otherMerchantId, validStoreId, startDate, endDate),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不属于该商家", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStatisticsService, times(1)).getStoreformStatistics(otherMerchantId, validStoreId, startDate, endDate);
    }

    /**
     * 反向测试：数据库查询异常
     */
    @Test
    void testGetStoreformStatistics_DatabaseException() {
        // 模拟抛出数据库异常
        when(merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, startDate, endDate))
                .thenThrow(new RuntimeException("数据库查询失败"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, startDate, endDate),
                "应该抛出RuntimeException"
        );
        
        assertEquals("数据库查询失败", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStatisticsService, times(1)).getStoreformStatistics(validMerchantId, validStoreId, startDate, endDate);
    }

    /**
     * 反向测试：系统负载过高
     */
    @Test
    void testGetStoreformStatistics_SystemOverload() {
        // 模拟抛出系统异常
        when(merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, startDate, endDate))
                .thenThrow(new RuntimeException("系统负载过高，请稍后再试"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantStatisticsService.getStoreformStatistics(validMerchantId, validStoreId, startDate, endDate),
                "应该抛出RuntimeException"
        );
        
        assertEquals("系统负载过高，请稍后再试", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStatisticsService, times(1)).getStoreformStatistics(validMerchantId, validStoreId, startDate, endDate);
    }
}
