package com.shenzhewei.quiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shenzhewei.quiz.pojo.User;
import com.shenzhewei.quiz.service.UserService;
import com.shenzhewei.quiz.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserController 集成测试
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private String token;

    @BeforeEach
    void setUpToken() {
        this.token = JwtUtil.generateToken("test_user1");
    }

    /**
     * 测试：新增用户
     */
    @Test
    public void testAddUser() throws Exception {
        User newUser = new User();
        newUser.setUsername("new_test_user");
        newUser.setPassword("newpassword123");
        newUser.setAddress("New Test Address");

        String requestBody = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token", token)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data").value("添加用户成功"));

        // 验证用户确实被添加到数据库
        boolean exists = userService.usernameExists("new_test_user");
        assertTrue(exists);
    }

    /**
     * 测试：添加重复用户名应失败
     */
    @Test
    public void testAddUserWithDuplicateUsername() throws Exception {
        User user = new User();
        user.setUsername("test_user1"); // 已存在的用户名
        user.setPassword("password");
        user.setAddress("Some Address");

        String requestBody = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token", token)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("用户名已存在"));
    }

    /**
     * 测试：检查用户名是否存在
     */
    @Test
    public void testUsernameExists() throws Exception {
        // 测试存在的用户名
        mockMvc.perform(get("/user/exists/test_user1").header("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data").value(true));

        // 测试不存在的用户名
        mockMvc.perform(get("/user/exists/nonexistent_user").header("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data").value(false));
    }

    /**
     * 测试：根据 ID 删除用户
     */
    @Test
    public void testDeleteUserById() throws Exception {
        mockMvc.perform(delete("/deleteById").param("id", "1").header("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data").value("删除用户成功"));

        // 验证用户确实被逻辑删除
        User deletedUser = userService.findById(1L);
        assertNull(deletedUser);
    }

    /**
     * 测试：根据用户名删除用户
     */
    @Test
    public void testDeleteUserByUsername() throws Exception {
        mockMvc.perform(delete("/user/username/test_user2").header("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data").value("删除用户成功"));

        // 验证用户确实被逻辑删除
        boolean exists = userService.usernameExists("test_user2");
        assertFalse(exists);
    }

    /**
     * 测试：分页查询用户（无过滤条件）
     */
    @Test
    public void testFindUsersByPage() throws Exception {
        mockMvc.perform(get("/users").param("page", "1").param("pageSize", "10").header("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data.total").isNumber())
                .andExpect(jsonPath("$.data.rows", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.data.rows[0].username").exists());
    }

    /**
     * 测试：分页查询用户（带用户名过滤）
     */
    @Test
    public void testFindUsersByPageWithFilter() throws Exception {
        mockMvc.perform(get("/users").param("page", "1").param("pageSize", "10").param("username", "test_user").header("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data.rows", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.data.rows[0].username").value(containsString("test_user")));
    }

    /**
     * 测试：根据 ID 查询用户
     */
    @Test
    public void testFindUserById() throws Exception {
        mockMvc.perform(get("/user/1").header("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").exists());
    }

    /**
     * 测试：查询不存在的用户
     */
    @Test
    public void testFindUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/user/9999").header("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("用户不存在"));
    }

    /**
     * 测试：更新用户信息
     */
    @Test
    public void testUpdateUser() throws Exception {
        User updateUser = new User();
        updateUser.setId(1L);
        updateUser.setUsername("test_user1_updated");
        updateUser.setPassword("newpassword");
        updateUser.setAddress("Updated Address");

        String requestBody = objectMapper.writeValueAsString(updateUser);

        mockMvc.perform(put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token", token)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data").value("更新用户成功"));

        // 验证更新成功
        User updatedUser = userService.findById(1L);
        assertNotNull(updatedUser);
        assertEquals("test_user1_updated", updatedUser.getUsername());
    }
}
