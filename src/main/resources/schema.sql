-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    address VARCHAR(200),
    deleted TINYINT DEFAULT 0
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 题目表
DROP TABLE IF EXISTS question;
CREATE TABLE question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question TEXT NOT NULL,
    option_a VARCHAR(200) NOT NULL,
    option_b VARCHAR(200) NOT NULL,
    option_c VARCHAR(200) NOT NULL,
    option_d VARCHAR(200) NOT NULL,
    answer VARCHAR(10) NOT NULL,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 插入示例用户数据 (密码为123456，已MD5加密: com.quiz123456)
INSERT INTO `user` (username, password, create_time, update_time, address, deleted) VALUES
('admin', 'abb283b0200e38c5676e51f4ee23f28b', '2024-01-01 10:00:00', '2024-01-01 10:00:00', '上海市普陀区金沙江路 1518 弄', 0),
('王小虎', 'abb283b0200e38c5676e51f4ee23f28b', '2016-05-02 10:00:00', '2016-05-02 10:00:00', '上海市普陀区金沙江路 1518 弄', 0),
('李明', 'abb283b0200e38c5676e51f4ee23f28b', '2016-05-04 14:30:00', '2016-05-04 14:30:00', '上海市普陀区金沙江路 1517 弄', 0),
('张三', 'abb283b0200e38c5676e51f4ee23f28b', '2016-05-01 09:15:00', '2016-05-01 09:15:00', '上海市普陀区金沙江路 1519 弄', 0);

-- 插入示例题目数据
INSERT INTO question (question, option_a, option_b, option_c, option_d, answer, deleted) VALUES
('以下哪个标签用于定义网页的导航部分？', 'A. <header>', 'B. <nav>', 'C. <section>', 'D. <aside>', 'B', 0),
('关于HTTP状态码,哪一个表示请求的资源未找到？', 'A. 200', 'B. 301', 'C. 404', 'D. 502', 'C', 0),
('下列哪种JavaScript声明方式可以创建不可重新赋值的变量？', 'A. var total = 0', 'B. let total = 0', 'C. const total = 0', 'D. function total() {}', 'C', 0),
('在CSS中,哪条语句可以让元素水平居中？', 'A. margin: 0 auto;', 'B. text-align: left;', 'C. display: inline;', 'D. float: right;', 'A', 0);
