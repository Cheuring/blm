package com.blm.user.repository;

import com.blm.common.entity.Favorite;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FavoriteRepository {

    @Select("SELECT COUNT(1) FROM favorite WHERE user_id = #{userId} AND target_id = #{targetId} AND type = #{type}")
    int countByUserIdAndTargetId(@Param("userId") Long userId, @Param("targetId") Long targetId, @Param("type") String type);

    @Insert("INSERT INTO favorite(user_id, target_id, type, created_at) VALUES(#{userId}, #{targetId}, #{type}, #{createdAt})")
    int insert(Favorite favorite);

    @Delete("DELETE FROM favorite WHERE user_id = #{userId} AND target_id = #{targetId} AND type = #{type}")
    int deleteByUserIdAndTargetId(@Param("userId") Long userId, @Param("targetId") Long targetId, @Param("type") String type);

    @Select("SELECT * FROM favorite WHERE user_id = #{userId} AND type = #{type}")
    List<Favorite> findAllByUserId(@Param("userId") Long userId, @Param("type") String type);
}