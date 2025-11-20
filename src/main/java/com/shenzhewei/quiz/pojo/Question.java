package com.shenzhewei.quiz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 题目实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private Long id;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String answer;
    private LocalDateTime createdTime;
    private Integer deleted;
}
