package com.shenzhewei.quiz.service.impl;

import com.shenzhewei.quiz.mapper.UserMapper;
import com.shenzhewei.quiz.pojo.PageBean;
import com.shenzhewei.quiz.pojo.Result;
import com.shenzhewei.quiz.pojo.User;
import com.shenzhewei.quiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    // 密码加密盐值
    private static final String SALT = "com.quiz";

    @Override
    public User login(String username, String password) {
        // 对密码进行加密
        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        return userMapper.findByUsernameAndPassword(username, encryptedPassword);
    }

    @Override
    public void addUser(User user) {
        // 检查用户名是否已存在
        if (usernameExists(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        // 对密码进行加密
        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + user.getPassword()).getBytes());
        user.setPassword(encryptedPassword);
        userMapper.insert(user);
    }

    @Override
    public boolean usernameExists(String username) {
        User user = userMapper.findByUsername(username);
        return user != null;
    }

    @Override
    public void deleteById(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public void deleteByUsername(String username) {
        userMapper.deleteByUsername(username);
    }

    @Override
    public PageBean<User> findByPage(Integer page, Integer pageSize, String username) {
        // 计算偏移量
        Integer offset = (page - 1) * pageSize;

        // 查询数据
        List<User> users = userMapper.findByPage(username, pageSize, offset);

        // 查询总记录数
        Long total = userMapper.count(username);

        // 封装分页结果
        PageBean<User> pageBean = new PageBean<>();
        pageBean.setTotal(total);
        pageBean.setRows(users);

        return pageBean;
    }

    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public void updateUser(User user) {
        userMapper.update(user);
    }

    @Override
    public Result<String> register(String username, String password, String checkPassword) {
        // 验证输入
        if (username == null || password == null || checkPassword == null) {
            return Result.error("用户名或密码为空");
        }
        
        if (!password.equals(checkPassword)) {
            return Result.error("两次密码不一致");
        }
        
        // 检查用户名是否已存在
        if (usernameExists(username)) {
            return Result.error("用户名已存在");
        }
        
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setUserRole(0); // 普通用户
            addUser(user);
            return Result.success("注册成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @Override
    public Result<String> addUser(String username, String password, String checkPassword, String role) {
        // 验证输入
        if (username == null || password == null || checkPassword == null || role == null) {
            return Result.error("用户名、密码或角色为空");
        }
        
        if (!password.equals(checkPassword)) {
            return Result.error("两次密码不一致");
        }
        
        // 检查用户名是否已存在
        if (usernameExists(username)) {
            return Result.error("用户名已存在");
        }
        
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setUserRole(Integer.parseInt(role)); // 设置用户角色
            addUser(user);
            return Result.success("添加用户成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @Override
    public void resetPassword(Long id) {
        User user = userMapper.findById(id);
        if (user != null) {
            // 重置密码为 123456
            String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + "123456").getBytes());
            user.setPassword(encryptedPassword);
            userMapper.update(user);
        }
    }
}
