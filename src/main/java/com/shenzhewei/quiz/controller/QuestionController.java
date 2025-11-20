package com.shenzhewei.quiz.controller;

import com.shenzhewei.quiz.pojo.PageBean;
import com.shenzhewei.quiz.pojo.Question;
import com.shenzhewei.quiz.pojo.Result;
import com.shenzhewei.quiz.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 题目Controller
 */
@RestController
@RequestMapping("/api/question")
@CrossOrigin
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /**
     * 新增题目
     */
    @PostMapping("/addQuestion")
    public Result<String> addQuestion(@RequestBody Question question) {
        questionService.addQuestion(question);
        return Result.success("添加题目成功");
    }

    /**
     * 根据id删除题目（逻辑删除）
     */
    @GetMapping("/delQuestion")
    public Result<String> deleteById(@RequestParam Long id) {
        questionService.deleteById(id);
        return Result.success("删除题目成功");
    }

    /**
     * 分页查询题目
     * 
     * @param page     页码（默认1）
     * @param pageSize 每页数量（默认10）
     * @param keyword  题目关键词（可选，模糊查询）
     */
    @GetMapping("/questions")
    public Result<PageBean<Question>> findByPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        PageBean<Question> pageBean = questionService.findByPage(page, pageSize, keyword);
        return Result.success(pageBean);
    }

    /**
     * 关键词搜索题目
     */
    @GetMapping("/findQuestion")
    public Result<List<Question>> findQuestion(@RequestParam String keyword) {
        PageBean<Question> pageBean = questionService.findByPage(1, 100, keyword);
        return Result.success(pageBean.getRows());
    }

    /**
     * 根据id查询题目
     */
    @GetMapping("/question/{id}")
    public Result<Question> findById(@PathVariable Long id) {
        Question question = questionService.findById(id);
        if (question == null) {
            return Result.error("题目不存在");
        }
        return Result.success(question);
    }

    /**
     * 更新题目信息
     */
    @PutMapping("/updateQuestion")
    public Result<String> updateQuestion(@RequestBody Question question) {
        questionService.updateQuestion(question);
        return Result.success("更新题目成功");
    }

    /**
     * 获取所有题目（用户端使用，不分页）
     */
    @GetMapping("/question/all")
    public Result<List<Question>> getAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        return Result.success(questions);
    }
}
