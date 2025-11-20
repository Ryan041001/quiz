package com.shenzhewei.quiz.service.impl;

import com.shenzhewei.quiz.mapper.UserMapper;
import com.shenzhewei.quiz.pojo.PageBean;
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
}
