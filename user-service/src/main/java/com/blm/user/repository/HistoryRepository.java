package com.blm.user.repository;

import com.blm.common.entity.History;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface HistoryRepository {

    @Select("SELECT * FROM history WHERE id = #{id} AND user_id = #{userId}")
    Optional<History> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Insert("INSERT INTO history (user_id, target_id, type) VALUES (#{userId}, #{targetId}, #{type})")
    int insert(@Param("userId") Long userId, @Param("targetId") Long targetId, @Param("type") String type);

    @Select("SELECT * FROM history WHERE user_id = #{userId} AND type = #{type} ORDER BY created_at DESC")
    List<History> findByUserIdOrderedDesc(@Param("userId") Long userId, @Param("type") String type);

    @Delete("DELETE FROM history WHERE id = #{historyId}")
    int deleteById(@Param("historyId") Long historyId);


    @Select("SELECT * FROM history " +
            "WHERE user_id = #{userId} " +
            "AND type = #{type} " +
            "AND created_at >= #{earliest} " +
            "ORDER BY created_at DESC " +
            "LIMIT 1")
    Optional<History> findRecordInTheDay(@Param("userId") Long userId, @Param("type") String type, @Param("earliest") LocalDateTime earliest);

    @Update("update history set created_at = #{now} where id = #{id}")
    int updateVisitedTime(@Param("id") Long id, @Param("now") LocalDateTime now);
}