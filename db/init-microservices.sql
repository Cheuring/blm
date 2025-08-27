-- 创建用户服务数据库
CREATE DATABASE IF NOT EXISTS user_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建商家服务数据库
CREATE DATABASE IF NOT EXISTS store_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建订单服务数据库
CREATE DATABASE IF NOT EXISTS order_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建骑手服务数据库
CREATE DATABASE IF NOT EXISTS rider_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 用户服务表结构
USE user_db;

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    full_name VARCHAR(100),
    avatar VARCHAR(255),
    role VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '角色：USER/MERCHANT/RIDER/ADMIN',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_phone (phone)
) COMMENT '用户表';

-- 用户地址表
CREATE TABLE IF NOT EXISTS user_address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    receiver VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    province VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    district VARCHAR(50) NOT NULL,
    detail_address VARCHAR(200) NOT NULL,
    is_default TINYINT DEFAULT 0 COMMENT '是否默认地址：0-否，1-是',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_user_default (user_id, is_default)
) COMMENT '用户地址表';

-- 插入测试数据
INSERT INTO user (username, password, phone, email, full_name, role, status) VALUES
('admin', '$2a$10$KdDkGThGCEXpfTAYugLMpuAwIU/4eg0cqtlUOSj1QZBLVvGNzgv8u', '13800000001', 'admin@blm.com', '管理员', 'ADMIN', 1),
('testuser', '$2a$10$KdDkGThGCEXpfTAYugLMpuAwIU/4eg0cqtlUOSj1QZBLVvGNzgv8u', '13800000002', 'user@blm.com', '测试用户', 'USER', 1);

INSERT INTO user_address (user_id, receiver, phone, province, city, district, detail_address, is_default) VALUES
(2, '张三', '13812345678', '北京市', '北京市', '海淀区', '中关村大街1号', 1),
(2, '李四', '13887654321', '上海市', '上海市', '浦东新区', '陆家嘴金融中心', 0);
