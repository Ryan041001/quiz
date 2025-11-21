package com.shenzhewei.quiz.service;

import com.shenzhewei.quiz.pojo.PageBean;
import com.shenzhewei.quiz.pojo.Result;
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
     * 用户注册（用户端专用，默认为普通用户）
     */
    Result<String> register(String username, String password, String checkPassword);

    /**
     * 添加用户（管理端专用，可指定角色）
     */
    Result<String> addUser(String username, String password, String checkPassword, String role);

    /**
     * 重置密码
     */
    void resetPassword(Long id);

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
