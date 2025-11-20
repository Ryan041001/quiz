-- Test database schema - Using H2 compatible SQL
-- Reset tables for each test context to avoid duplicate data errors
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS question;

-- User table
CREATE TABLE IF NOT EXISTS `user` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    address VARCHAR(200),
    deleted TINYINT DEFAULT 0
);

-- Question table
CREATE TABLE IF NOT EXISTS question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question TEXT NOT NULL,
    option_a VARCHAR(200) NOT NULL,
    option_b VARCHAR(200) NOT NULL,
    option_c VARCHAR(200) NOT NULL,
    option_d VARCHAR(200) NOT NULL,
    answer VARCHAR(10) NOT NULL,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);

-- Test data - Users
INSERT INTO `user` (username, password, address, deleted) VALUES
('test_user1', 'password123', 'Test Address 1', 0),
('test_user2', 'password456', 'Test Address 2', 0),
('test_user3', 'password789', 'Test Address 3', 0);

-- Test data - Questions
INSERT INTO question (question, option_a, option_b, option_c, option_d, answer, deleted) VALUES
('What is the capital of France?', 'A. London', 'B. Paris', 'C. Berlin', 'D. Madrid', 'B', 0),
('What is 2 + 2?', 'A. 3', 'B. 4', 'C. 5', 'D. 6', 'B', 0),
('What color is the sky?', 'A. Blue', 'B. Green', 'C. Red', 'D. Yellow', 'A', 0);
