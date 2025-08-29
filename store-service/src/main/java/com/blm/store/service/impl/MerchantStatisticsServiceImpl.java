package com.blm.store.service.impl;

import com.blm.common.feign.OrderServiceClient;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.entity.Order;
import com.blm.common.entity.Store;
import com.blm.common.exception.CommonException;
import com.blm.store.service.MerchantReviewService;
import com.blm.store.service.MerchantStatisticsService;
import com.blm.store.service.StoreService;
import com.blm.common.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商家统计服务实现类
 */
@Slf4j
@Service
public class MerchantStatisticsServiceImpl implements MerchantStatisticsService {

    @Autowired
    private OrderServiceClient orderService;
    
    @Autowired
    private MerchantReviewService reviewService;

    @Autowired
    private StoreService storeService;

    @Override
    public StoreStatisticsVO getStoreformStatistics(Long merchantId, Long storeId, LocalDate startDate, LocalDate endDate) { // todo: 这里的startDate和endDate参数目前未使用

        Store store = storeService.getStoreById(storeId);
        if (!store.getMerchantId().equals(merchantId)) {
            throw new CommonException(ExceptionConstant.STORE_UNAUTHORIZED);
        }

        // 获取今天的开始和结束时间
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        // 今日订单数和交易额
        long todayOrders = orderService.countStoreByStatusAndCreatedAtBetween(Order.OrderStatus.COMPLETED, todayStart, todayEnd, storeId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));
        BigDecimal todaySales = orderService.sumStoreTotalByStatusAndCreatedAtBetween(Order.OrderStatus.COMPLETED, todayStart, todayEnd, storeId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));

        // 获取昨日的开始和结束时间
        LocalDateTime yesterdayStart = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
        LocalDateTime yesterdayEnd = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX);

        // 昨日订单数和交易额
        long yesterdayOrders = orderService.countStoreByStatusAndCreatedAtBetween(Order.OrderStatus.COMPLETED, yesterdayStart, yesterdayEnd, storeId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));
        BigDecimal yesterdaySales = orderService.sumStoreTotalByStatusAndCreatedAtBetween(Order.OrderStatus.COMPLETED, yesterdayStart, yesterdayEnd, storeId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));

        // 本月订单数和销售额
        LocalDateTime monthStart = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        long monthOrders = orderService.countStoreByStatusAndCreatedAtBetween(Order.OrderStatus.COMPLETED, monthStart, todayEnd,  storeId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));
        BigDecimal monthSales = orderService.sumStoreTotalByStatusAndCreatedAtBetween(Order.OrderStatus.COMPLETED, monthStart, todayEnd, storeId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));

        // 累计订单数和交易额
        long totalOrders = orderService.countStoreByStatus(Order.OrderStatus.COMPLETED, storeId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));
        BigDecimal totalSales = orderService.sumStoreTotalByStatus(Order.OrderStatus.COMPLETED, storeId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));

        // 获取最近30天的每日订单数趋势
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        List<LocalDate> dateList= new ArrayList<>();
        dateList.add(thirtyDaysAgo);
        while(!thirtyDaysAgo.equals(LocalDate.now())){
            //计算指定日期的后一天
            thirtyDaysAgo=thirtyDaysAgo.plusDays(1);
            dateList.add(thirtyDaysAgo);
        }

        //存放每天的营业额
        List<Double> turnoverList = new ArrayList<>();
        // 查询营业额统计
        for (LocalDate date : dateList) {
            //查询date日期对应的营业额，营业额为状态为"COMPLETED"的订单金额之和
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Double turnover = orderService.sumStoreTotalByStatusAndCreatedAtBetween(Order.OrderStatus.COMPLETED, beginTime, endTime, storeId)
                    .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR)).doubleValue();
            turnoverList.add(turnover);
        }
        StoreTurnoverReportVO dailyTurnoverTrendVO = new StoreTurnoverReportVO(StringUtils.join(dateList, ","),  StringUtils.join(turnoverList, ","));

        //存放每天的订单总量
        List<Integer> totalOrderList = new ArrayList<>();
        //存放每天的订单新增量
        List<Integer> newOrderList = new ArrayList<>();

        // 初始化前一天的订单总量为0
        int previousTotalOrders = 0;

        // 查询每天订单总量统计和新增统计
        for (LocalDate date : dateList) {
            //查询date日期对应的营业额，营业额为状态为"COMPLETED"的订单金额之和
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            // 计算新增订单量
            Integer totalOrders_temp = orderService.countStoreByStatusAndCreatedAtBetween(Order.OrderStatus.COMPLETED, LocalDateTime.of(LocalDate.MIN, LocalTime.MIN), endTime, storeId)
                    .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR)).intValue();
            int newOrders = totalOrders_temp  - previousTotalOrders;
            totalOrderList.add(totalOrders_temp);
            newOrderList.add(newOrders);
        }
        StoreOrderReportVO dailyOrderTrendVO = new StoreOrderReportVO(StringUtils.join(dateList, ","), StringUtils.join(totalOrderList, ","), StringUtils.join(newOrderList, ","));

        // 获取热门商品排行
        List<StoreStatisticsVO.HotFoodVO> topFoodsData = orderService.getTopSellingFoods(storeId, 10)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));
        System.out.println(topFoodsData);
        List<StoreStatisticsVO.HotFoodVO> topFoods = topFoodsData.stream()
                .map(item -> {
                    Long foodId = item.getFoodId();
                    String foodName = item.getFoodName();
                    String foodImage = item.getFoodImage();
                    Integer salesCount = item.getSaleCount();
                    BigDecimal salesAmount = item.getSaleAmount();
                    return new StoreStatisticsVO.HotFoodVO(foodId, foodName, foodImage, salesCount, salesAmount);
                })
                .collect(Collectors.toList());

        StoreStatisticsVO.ReviewStatisticsVO statistics = reviewService.getReviewStatistics(merchantId, storeId);


        // 构建并返回统计数据
        return StoreStatisticsVO.builder()
                .storeId(storeId)
                .storeName(store.getName())
                .todayOrderCount(todayOrders)
                .todaySales(todaySales)
                .totalSales(totalSales)
                .yesterdayOrderCount(yesterdayOrders)
                .yesterdaySales(yesterdaySales)
                .monthOrderCount(monthOrders)
                .monthSales(monthSales)
                .totalOrderCount(totalOrders)
                .totalSales(totalSales)
                .dailySalesTrend(dailyTurnoverTrendVO)
                .dailyOrderTrend(dailyOrderTrendVO)
                .hotFoods(topFoods)
                .reviewStatistics(statistics)
                .build();
    }
}
