package com.shenzhewei.quiz.mapper;

import com.shenzhewei.quiz.pojo.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 题目Mapper接口
 */
@Mapper
public interface QuestionMapper {

        /**
         * 新增题目
         */
        @Insert("INSERT INTO question(question, option_a, option_b, option_c, option_d, answer, deleted) " +
                        "VALUES(#{question}, #{optionA}, #{optionB}, #{optionC}, #{optionD}, #{answer}, 0)")
        @Options(useGeneratedKeys = true, keyProperty = "id")
        void insert(Question question);

        /**
         * 根据id查询题目
         */
        @Select("SELECT id, question, option_a, option_b, option_c, option_d, answer, created_time, deleted FROM question WHERE id = #{id} AND deleted = 0")
        @Results({
                        @Result(column = "created_time", property = "createdTime")
        })
        Question findById(Long id);

        /**
         * 根据id逻辑删除题目
         */
        @Update("UPDATE question SET deleted = 1 WHERE id = #{id}")
        void deleteById(Long id);

        /**
         * 分页查询题目（支持题目关键词模糊查询）
         */
        @Select("<script>" +
                        "SELECT id, question, option_a, option_b, option_c, option_d, answer, created_time, deleted FROM question WHERE deleted = 0 "
                        +
                        "<if test='keyword != null and keyword != \"\"'>" +
                        "AND question LIKE CONCAT('%', #{keyword}, '%') " +
                        "</if>" +
                        "ORDER BY created_time DESC " +
                        "LIMIT #{pageSize} OFFSET #{offset}" +
                        "</script>")
        @Results({
                        @Result(column = "created_time", property = "createdTime")
        })
        List<Question> findByPage(@Param("keyword") String keyword,
                        @Param("pageSize") Integer pageSize,
                        @Param("offset") Integer offset);

        /**
         * 统计题目总数（支持题目关键词模糊查询）
         */
        @Select("<script>" +
                        "SELECT COUNT(*) FROM question WHERE deleted = 0 " +
                        "<if test='keyword != null and keyword != \"\"'>" +
                        "AND question LIKE CONCAT('%', #{keyword}, '%') " +
                        "</if>" +
                        "</script>")
        Long count(@Param("keyword") String keyword);

        /**
         * 更新题目信息
         */
        @Update("UPDATE question SET question = #{question}, option_a = #{optionA}, option_b = #{optionB}, " +
                        "option_c = #{optionC}, option_d = #{optionD}, answer = #{answer} WHERE id = #{id}")
        void update(Question question);

        /**
         * 获取所有未删除的题目（用户端使用）
         */
        @Select("SELECT id, question, option_a, option_b, option_c, option_d, answer, created_time, deleted FROM question WHERE deleted = 0 ORDER BY id")
        @Results({
                        @Result(column = "created_time", property = "createdTime")
        })
        List<Question> findAll();
}
