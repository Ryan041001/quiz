package com.shenzhewei.quiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shenzhewei.quiz.pojo.Question;
import com.shenzhewei.quiz.service.QuestionService;
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
 * QuestionController 集成测试
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class QuestionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QuestionService questionService;

    private String token;

    @BeforeEach
    void setUpToken() {
        this.token = JwtUtil.generateToken("test_user1");
    }

    /**
     * 测试：新增题目
     */
    @Test
    public void testAddQuestion() throws Exception {
        Question newQuestion = new Question();
        newQuestion.setQuestion("New test question?");
        newQuestion.setOptionA("A. Option A");
        newQuestion.setOptionB("B. Option B");
        newQuestion.setOptionC("C. Option C");
        newQuestion.setOptionD("D. Option D");
        newQuestion.setAnswer("A");

        String requestBody = objectMapper.writeValueAsString(newQuestion);

        mockMvc.perform(post("/addQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token", token)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data").value("添加题目成功"));
    }

    /**
     * 测试：根据 ID 删除题目
     */
    @Test
    public void testDeleteQuestionById() throws Exception {
        mockMvc.perform(get("/delQuestion").param("id", "1").header("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data").value("删除题目成功"));

        // 验证题目确实被逻辑删除
        Question deletedQuestion = questionService.findById(1L);
        assertNull(deletedQuestion);
    }

    /**
     * 测试：分页查询题目（无过滤条件）
     */
    @Test
    public void testFindQuestionsByPage() throws Exception {
        mockMvc.perform(get("/questions").param("page", "1").param("pageSize", "10").header("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.total").isNumber())
                .andExpect(jsonPath("$.data.rows", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.data.rows[0].question").exists());
    }

    /**
     * 测试：分页查询题目（带关键词过滤）
     */
    @Test
    public void testFindQuestionsByPageWithKeyword() throws Exception {
        mockMvc.perform(get("/questions").param("page", "1").param("pageSize", "10").param("keyword", "capital").header("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.rows", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.data.rows[0].question").value(containsString("capital")));
    }

    /**
     * 测试：分页查询题目（关键词不匹配）
     */
    @Test
    public void testFindQuestionsByPageWithNoMatch() throws Exception {
        mockMvc.perform(get("/questions").param("page", "1").param("pageSize", "10").param("keyword", "nonexistent").header("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.rows", hasSize(0)));
    }

    /**
     * 测试：根据 ID 查询题目
     */
    @Test
    public void testFindQuestionById() throws Exception {
        mockMvc.perform(get("/question/1").header("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.question").exists())
                .andExpect(jsonPath("$.data.optionA").exists())
                .andExpect(jsonPath("$.data.optionB").exists())
                .andExpect(jsonPath("$.data.optionC").exists())
                .andExpect(jsonPath("$.data.optionD").exists())
                .andExpect(jsonPath("$.data.answer").exists());
    }

    /**
     * 测试：查询不存在的题目
     */
    @Test
    public void testFindQuestionByIdNotFound() throws Exception {
        mockMvc.perform(get("/question/9999").header("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("题目不存在"));
    }

    /**
     * 测试：更新题目信息
     */
    @Test
    public void testUpdateQuestion() throws Exception {
        Question updateQuestion = new Question();
        updateQuestion.setId(1L);
        updateQuestion.setQuestion("Updated question?");
        updateQuestion.setOptionA("A. Updated Option A");
        updateQuestion.setOptionB("B. Updated Option B");
        updateQuestion.setOptionC("C. Updated Option C");
        updateQuestion.setOptionD("D. Updated Option D");
        updateQuestion.setAnswer("C");

        String requestBody = objectMapper.writeValueAsString(updateQuestion);

        mockMvc.perform(put("/updateQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token", token)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data").value("更新题目成功"));

        // 验证更新成功
        Question updatedQuestion = questionService.findById(1L);
        assertNotNull(updatedQuestion);
        assertEquals("Updated question?", updatedQuestion.getQuestion());
        assertEquals("C", updatedQuestion.getAnswer());
    }

    /**
     * 测试：获取所有题目（用户端接口，无分页）
     */
    @Test
    public void testGetAllQuestions() throws Exception {
        mockMvc.perform(get("/question/all").header("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.data[0].question").exists())
                .andExpect(jsonPath("$.data[0].optionA").exists())
                .andExpect(jsonPath("$.data[0].answer").exists());
    }
}
