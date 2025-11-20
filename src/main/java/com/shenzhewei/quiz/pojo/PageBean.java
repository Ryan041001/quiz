package com.shenzhewei.quiz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean<T> {
    private Long total; // 总记录数
    private List<T> rows; // 当前页数据列表
}
