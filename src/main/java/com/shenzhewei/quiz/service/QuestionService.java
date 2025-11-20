package com.shenzhewei.quiz.service;

import com.shenzhewei.quiz.pojo.PageBean;
import com.shenzhewei.quiz.pojo.Question;

import java.util.List;

/**
 * 题目Service接口
 */
public interface QuestionService {

    /**
     * 新增题目
     */
    void addQuestion(Question question);

    /**
     * 根据id逻辑删除题目
     */
    void deleteById(Long id);

    /**
     * 分页查询题目
     */
    PageBean<Question> findByPage(Integer page, Integer pageSize, String keyword);

    /**
     * 根据id查询题目
     */
    Question findById(Long id);

    /**
     * 更新题目信息
     */
    void updateQuestion(Question question);

    /**
     * 获取所有题目（用户端使用）
     */
    List<Question> getAllQuestions();
}
