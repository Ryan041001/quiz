package com.shenzhewei.quiz.service.impl;

import com.shenzhewei.quiz.mapper.QuestionMapper;
import com.shenzhewei.quiz.pojo.PageBean;
import com.shenzhewei.quiz.pojo.Question;
import com.shenzhewei.quiz.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 题目Service实现类
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public void addQuestion(Question question) {
        questionMapper.insert(question);
    }

    @Override
    public void deleteById(Long id) {
        questionMapper.deleteById(id);
    }

    @Override
    public PageBean<Question> findByPage(Integer page, Integer pageSize, String keyword) {
        // 计算偏移量
        Integer offset = (page - 1) * pageSize;

        // 查询数据
        List<Question> questions = questionMapper.findByPage(keyword, pageSize, offset);

        // 查询总记录数
        Long total = questionMapper.count(keyword);

        // 封装分页结果
        PageBean<Question> pageBean = new PageBean<>();
        pageBean.setTotal(total);
        pageBean.setRows(questions);

        return pageBean;
    }

    @Override
    public Question findById(Long id) {
        return questionMapper.findById(id);
    }

    @Override
    public void updateQuestion(Question question) {
        questionMapper.update(question);
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionMapper.findAll();
    }
}
