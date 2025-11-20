package com.shenzhewei.quiz.controller;

import com.shenzhewei.quiz.pojo.PageBean;
import com.shenzhewei.quiz.pojo.Result;
import com.shenzhewei.quiz.pojo.User;
import com.shenzhewei.quiz.service.UserService;
import com.shenzhewei.quiz.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户Controller
 */
@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        if (StringUtils.isAnyBlank(username, password)) {
            return Result.error("用户名或密码为空");
        }

        User userResult = userService.login(username, password);
        if (userResult != null) {
            // 生成 JWT token
            Claims claims = Jwts.claims();
            claims.put("id", userResult.getId());
            claims.put("username", userResult.getUsername());

            String token = JwtUtil.generateTokenWithClaims(claims);
            return Result.success("用户登录成功", token);
        } else {
            return Result.error("用户登录失败");
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody Map<String, String> registerData) {
        String username = registerData.get("username");
        String password = registerData.get("password");
        String checkPassword = registerData.get("checkpassword");

        if (StringUtils.isAnyBlank(username, password, checkPassword)) {
            return Result.error("用户名或密码为空");
        }

        if (!password.equals(checkPassword)) {
            return Result.error("两次密码不一致");
        }

        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            userService.addUser(user);
            return Result.success("注册成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 新增用户
     */
    @PostMapping("/user")
    public Result<String> addUser(@RequestBody User user) {
        try {
            userService.addUser(user);
            return Result.success("添加用户成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/user/exists/{username}")
    public Result<Boolean> usernameExists(@PathVariable String username) {
        boolean exists = userService.usernameExists(username);
        return Result.success(exists);
    }

    /**
     * 根据id删除用户（逻辑删除）
     */
    @DeleteMapping("/deleteById")
    public Result<String> deleteById(@RequestParam Long id) {
        userService.deleteById(id);
        return Result.success("删除用户成功");
    }

    /**
     * 根据username删除用户（逻辑删除）
     */
    @DeleteMapping("/user/username/{username}")
    public Result<String> deleteByUsername(@PathVariable String username) {
        userService.deleteByUsername(username);
        return Result.success("删除用户成功");
    }

    /**
     * 分页查询用户
     * 
     * @param page     页码（默认1）
     * @param pageSize 每页数量（默认10）
     * @param username 用户名（可选，模糊查询）
     */
    @GetMapping("/users")
    public Result<PageBean<User>> findByPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username) {
        PageBean<User> pageBean = userService.findByPage(page, pageSize, username);
        // 不返回密码到前端，防止被泄露
        if (pageBean != null && pageBean.getRows() != null) {
            for (User u : pageBean.getRows()) {
                if (u != null) {
                    u.setPassword(null);
                }
            }
        }
        return Result.success(pageBean);
    }

    /**
     * 关键词搜索用户
     */
    @GetMapping("/findUser")
    public Result<PageBean<User>> findUser(@RequestParam String keyword) {
        PageBean<User> pageBean = userService.findByPage(1, 100, keyword);
        if (pageBean != null && pageBean.getRows() != null) {
            for (User u : pageBean.getRows()) {
                if (u != null) {
                    u.setPassword(null);
                }
            }
        }
        return Result.success(pageBean);
    }

    /**
     * 根据id查询用户
     */
    @GetMapping("/user/{id}")
    public Result<User> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        // 不返回密码字段
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/user")
    public Result<String> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return Result.success("更新用户成功");
    }
}
