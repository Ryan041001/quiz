package com.shenzhewei.quiz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private Integer userRole; // 用户角色：0-普通用户，1-管理员
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String address;
    private Integer deleted;
}
