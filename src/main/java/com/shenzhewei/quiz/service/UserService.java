package com.shenzhewei.quiz.service;

import com.shenzhewei.quiz.pojo.PageBean;
import com.shenzhewei.quiz.pojo.User;

/**
 * 用户Service接口
 */
public interface UserService {

    /**
     * 用户登录
     */
    User login(String username, String password);

    /**
     * 新增用户
     */
    void addUser(User user);

    /**
     * 检查用户名是否存在
     */
    boolean usernameExists(String username);

    /**
     * 根据id逻辑删除用户
     */
    void deleteById(Long id);

    /**
     * 根据username逻辑删除用户
     */
    void deleteByUsername(String username);

    /**
     * 分页查询用户
     */
    PageBean<User> findByPage(Integer page, Integer pageSize, String username);

    /**
     * 根据id查询用户
     */
    User findById(Long id);

    /**
     * 更新用户信息
     */
    void updateUser(User user);
}
