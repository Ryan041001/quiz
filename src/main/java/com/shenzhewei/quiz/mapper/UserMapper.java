package com.shenzhewei.quiz.mapper;

import com.shenzhewei.quiz.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {

        /**
         * 新增用户
         */
        @Insert("INSERT INTO `user`(username, password, address, deleted) VALUES(#{username}, #{password}, #{address}, 0)")
        void insert(User user);

        /**
         * 根据用户名查询用户
         */
        @Select("SELECT id, username, password, create_time, update_time, address, deleted FROM `user` WHERE username = #{username} AND deleted = 0")
        @Results({
                        @Result(column = "create_time", property = "createTime"),
                        @Result(column = "update_time", property = "updateTime")
        })
        User findByUsername(String username);

        /**
         * 根据用户名和密码查询用户（用于登录）
         */
        @Select("SELECT id, username, password, create_time, update_time, address, deleted FROM `user` WHERE username = #{username} AND password = #{password} AND deleted = 0")
        @Results({
                        @Result(column = "create_time", property = "createTime"),
                        @Result(column = "update_time", property = "updateTime")
        })
        User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

        /**
         * 根据id查询用户
         */
        @Select("SELECT id, username, password, create_time, update_time, address, deleted FROM `user` WHERE id = #{id} AND deleted = 0")
        @Results({
                        @Result(column = "create_time", property = "createTime"),
                        @Result(column = "update_time", property = "updateTime")
        })
        User findById(Long id);

        /**
         * 根据id逻辑删除用户
         */
        @Update("UPDATE `user` SET deleted = 1 WHERE id = #{id}")
        void deleteById(Long id);

        /**
         * 根据username逻辑删除用户
         */
        @Update("UPDATE `user` SET deleted = 1 WHERE username = #{username}")
        void deleteByUsername(String username);

        /**
         * 分页查询用户（支持用户名模糊查询）
         */
        @Select("<script>" +
                        "SELECT id, username, password, create_time, update_time, address, deleted FROM `user` WHERE deleted = 0 "
                        +
                        "<if test='username != null and username != \"\"'>" +
                        "AND username LIKE CONCAT('%', #{username}, '%') " +
                        "</if>" +
                        "ORDER BY id ASC " +
                        "LIMIT #{pageSize} OFFSET #{offset}" +
                        "</script>")
        @Results({
                        @Result(column = "create_time", property = "createTime"),
                        @Result(column = "update_time", property = "updateTime")
        })
        List<User> findByPage(@Param("username") String username,
                        @Param("pageSize") Integer pageSize,
                        @Param("offset") Integer offset);

        /**
         * 统计用户总数（支持用户名模糊查询）
         */
        @Select("<script>" +
                        "SELECT COUNT(*) FROM `user` WHERE deleted = 0 " +
                        "<if test='username != null and username != \"\"'>" +
                        "AND username LIKE CONCAT('%', #{username}, '%') " +
                        "</if>" +
                        "</script>")
        Long count(@Param("username") String username);

        /**
         * 更新用户信息
         */
        @Update("UPDATE `user` SET username = #{username}, password = #{password}, address = #{address} WHERE id = #{id}")
        void update(User user);
}
