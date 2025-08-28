-- 骑手服务数据库表结构
USE rider_db;

-- 骑手信息表
CREATE TABLE IF NOT EXISTS rider (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE COMMENT '用户ID',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    id_card VARCHAR(20) NOT NULL COMMENT '身份证号',
    id_card_front VARCHAR(255) COMMENT '身份证正面照片URL',
    id_card_back VARCHAR(255) COMMENT '身份证背面照片URL',
    vehicle_type VARCHAR(20) NOT NULL COMMENT '车辆类型：BIKE-自行车，ELECTRIC-电动车，MOTORCYCLE-摩托车',
    vehicle_number VARCHAR(20) COMMENT '车辆号码',
    status VARCHAR(20) DEFAULT 'OFFLINE' COMMENT '工作状态：ONLINE-在线，OFFLINE-离线，SUSPENDED-暂停',
    longitude DECIMAL(11,8) COMMENT '经度',
    latitude DECIMAL(10,8) COMMENT '纬度',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_location (longitude, latitude)
) COMMENT '骑手信息表';

-- 骑手统计数据表
CREATE TABLE IF NOT EXISTS rider_stats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rider_id BIGINT NOT NULL COMMENT '骑手ID',
    date DATE NOT NULL COMMENT '统计日期',
    orders_count INT DEFAULT 0 COMMENT '总订单数',
    completed_orders INT DEFAULT 0 COMMENT '完成订单数',
    canceled_orders INT DEFAULT 0 COMMENT '取消订单数',
    total_income DECIMAL(10,2) DEFAULT 0.00 COMMENT '总收入',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (rider_id) REFERENCES rider(id) ON DELETE CASCADE,
    UNIQUE KEY uk_rider_date (rider_id, date),
    INDEX idx_rider_id (rider_id),
    INDEX idx_date (date)
) COMMENT '骑手统计数据表';

-- 插入测试数据
INSERT INTO rider (user_id, real_name, id_card, vehicle_type, vehicle_number, status, longitude, latitude) VALUES
(4, '李四', '123456789012345678', 'ELECTRIC', '京A12345', 'ONLINE', 116.4074, 39.9042),
(5, '王五', '123456789012345679', 'MOTORCYCLE', '京B67890', 'OFFLINE', 116.3112, 39.9991);

-- 插入统计数据
INSERT INTO rider_stats (rider_id, date, orders_count, completed_orders, canceled_orders, total_income) VALUES
(1, CURDATE(), 5, 4, 1, 25.50),
(1, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 3, 3, 0, 18.75),
(2, CURDATE(), 8, 7, 1, 42.30),
(2, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 6, 5, 1, 31.20);
