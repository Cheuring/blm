package com.blm.order.repository;

import com.blm.common.vo.PlatformStatsVO;
import com.blm.common.vo.StoreStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单明细Repository接口
 */
@Mapper
public interface OrderItemRepository {

    /**
     * 查询店铺指定日期范围内的热销商品（按销量排序）
     * @param storeId 店铺ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param limit 查询数量限制
     * @return 热销商品列表，包含商品ID和销售数量
     */
//    @Select("SELECT d.food_id as foodId, SUM(d.quantity) as quantity " +
//           "FROM order_detail d " +
//           "JOIN orders o ON d.order_id = o.id " +
//           "WHERE o.store_id = #{storeId} " +
//           "AND o.payment_status = 1 " +
//           "AND DATE(o.created_at) BETWEEN #{startDate} AND #{endDate} " +
//           "GROUP BY d.food_id " +
//           "ORDER BY quantity DESC " +
//           "LIMIT #{limit}")
//    List<Map<String, Object>> findHotSellingFoods(
//            @Param("storeId") Long storeId,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate,
//            @Param("limit") Integer limit);

    /**
     * 查询店铺热销商品（总销量排序）
     * @param storeId 店铺ID
     * @param limit 查询数量限制
     * @return 热销商品列表，包含商品ID和销售数量
     */
//    @Select("SELECT d.food_id as foodId, SUM(d.quantity) as quantity " +
//           "FROM order_detail d " +
//           "JOIN orders o ON d.order_id = o.id " +
//           "WHERE o.store_id = #{storeId} " +
//           "AND o.payment_status = 1 " +
//           "GROUP BY d.food_id " +
//           "ORDER BY quantity DESC " +
//           "LIMIT #{limit}")
//    List<Map<String, Object>> findAllTimeHotSellingFoods(
//            @Param("storeId") Long storeId,
//            @Param("limit") Integer limit);

    /**
     * 查询销量最高的商品
     * @return 包含商品ID、商品名称、店铺名称和销售数量的对象数组列表
     */

    List<PlatformStatsVO.TopFoodItemVO> findTopSellingFoods(@Param("limit") Integer limit);


    /**
     * @param storeId
     * @return
     */
    List<StoreStatisticsVO.HotFoodVO> StoreFindTopSellingFoods(@Param("storeId") Long storeId, @Param("limit") Integer limit);
}