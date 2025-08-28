package com.blm.order.repository;

import com.blm.common.entity.Review;
import lombok.Data;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 评价Repository接口
 */
@Mapper
public interface ReviewRepository {

    @Data
    class RatingAggregation {
        private Integer rating;
        private Long count;
    }

    /**
     * 添加/更新评价
     */
    int save(Review review);

    /**
     * 根据ID查询评价
     */
    @Select("SELECT * FROM review WHERE id = #{id}")
    Optional<Review> findById(Long id);

    /**
     * 查询foodId对应的评价列表
     */
    @Select("SELECT r.* FROM review AS r join order_detail AS o on r.order_id = o.order_id WHERE food_id = #{foodId} ORDER BY r.created_at DESC")
    List<Review> findByFoodId(Long foodId);

    /**
     * 根据用户ID查询评价列表
     */
    @Select("SELECT * FROM review WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Review> findByUserId(Long userId);

    /**
     * 根据店铺ID查询评价列表
     */
    @Select("SELECT * FROM review WHERE store_id = #{storeId} ORDER BY created_at DESC")
    List<Review> findByStoreId(Long storeId);
    
    /**
     * 根据店铺ID和评分查询评价列表（支持PageHelper）
     */
    List<Review> findByStoreIdAndRating(@Param("storeId") Long storeId, @Param("storeRating") Integer storeRating);
    
    /**
     * 根据店铺ID统计评价数量
     */
    @Select("SELECT COUNT(*) FROM review WHERE store_id = #{storeId}")
    Integer countByStoreId(Long storeId);
    
    /**
     * 根据店铺ID和评分统计评价数量
     */
    Integer countByStoreIdAndRating(@Param("storeId") Long storeId, @Param("rating") Integer rating);

    /**
     * 根据订单ID查询评价
     */
    @Select("SELECT * FROM review WHERE order_id = #{orderId}")
    Optional<Review> findByOrderId(Long orderId);

    /**
     * 检查订单是否已评价
     */
    @Select("SELECT COUNT(*) FROM review WHERE order_id = #{orderId}")
    boolean existsByOrderId(Long orderId);

    /**
     * 更新评价
     */
    @Update("UPDATE review SET content = #{content}, store_rating = #{rating}, images = #{images} " +
            "WHERE id = #{id} AND user_id = #{userId}")
    int update(Review review);

    /**
     * 删除评价
     */
//    @Delete("DELETE FROM review WHERE id = #{id} AND user_id = #{userId}")
//    int delete(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 计算店铺平均评分
     */
    @Select("SELECT AVG(store_rating) FROM review WHERE store_id = #{storeId}")
    Double calculateAverageRating(Long storeId);
    
    // 添加管理员服务所需的方法
    
    /**
     * 总评价数量
     */
    @Select("SELECT COUNT(*) FROM review")
    long count();
    
    /**
     * 根据时间区间统计评价数量
     */
    @Select("SELECT COUNT(*) FROM review WHERE created_at BETWEEN #{start} AND #{end}")
    long countByCreatedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    /**
     * 管理员查询评价列表（带条件筛选和分页）
     */
    List<Review> findByConditions(
            @Param("rating") Integer rating, 
            @Param("keyword") String keyword,
            @Param("offset") int offset, 
            @Param("limit") int limit);
    
    /**
     * 统计符合条件的评价数量
     */
    long countByConditions(
            @Param("rating") Integer rating, 
            @Param("keyword") String keyword);
            
    /**
     * 管理员删除评价
     */
    @Delete("DELETE FROM review WHERE id = #{id}")
    int deleteById(Long id);
    
    /**
     * 更新评价状态（如隐藏或显示）
     */
//    @Update("UPDATE review SET status = #{status}, updated_at = #{updatedAt} WHERE id = #{id}")
//    int updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("updatedAt") LocalDateTime updatedAt);

    /**
     * 管理员查询评价列表（支持PageHelper分页）
     */
    List<Review> findByConditionsForAdmin(
            @Param("userId") Long userId,
            @Param("storeId") Long storeId,
            @Param("foodId") Long foodId,
            @Param("rating") Integer rating);
    
    /**
     * 删除评价（管理员使用）
     */
    @Delete("DELETE FROM review WHERE id = #{id}")
    int delete(Review review);

    /**
     * 根据店铺ID聚合评分统计
     */
    @Select("SELECT store_rating AS rating, COUNT(*) AS count " +
            "FROM review WHERE store_id = #{storeId} " +
            "GROUP BY store_rating")
    List<RatingAggregation> aggregateRatingsByStoreId(Long storeId);
}