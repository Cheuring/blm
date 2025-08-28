-- 订单服务数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS order_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE order_db;

-- 订单表
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(64) NOT NULL UNIQUE COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    store_id BIGINT NOT NULL COMMENT '店铺ID',
    rider_id BIGINT COMMENT '骑手ID',
    address_id BIGINT NOT NULL COMMENT '收货地址ID',
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '订单总金额',
    delivery_fee DECIMAL(8,2) NOT NULL DEFAULT 0.00 COMMENT '配送费',
    discount_amount DECIMAL(8,2) NOT NULL DEFAULT 0.00 COMMENT '优惠金额',
    coupon_discount DECIMAL(8,2) NOT NULL DEFAULT 0.00 COMMENT '优惠券折扣',
    user_coupon_id BIGINT COMMENT '用户优惠券ID',
    payment_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '实际支付金额',
    payment_type VARCHAR(20) COMMENT '支付方式: ALIPAY, WECHAT, CASH',
    status VARCHAR(30) NOT NULL DEFAULT 'ORDER_CREATED' COMMENT '订单状态',
    dispatch_type VARCHAR(20) COMMENT '配送类型',
    remark VARCHAR(500) COMMENT '订单备注',
    expected_time TIMESTAMP NULL COMMENT '预计送达时间',
    actual_time TIMESTAMP NULL COMMENT '实际送达时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_store_id (store_id),
    INDEX idx_rider_id (rider_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 订单明细表
CREATE TABLE order_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL COMMENT '订单ID',
    food_id BIGINT NOT NULL COMMENT '商品ID',
    food_name VARCHAR(100) NOT NULL COMMENT '商品名称',
    food_image VARCHAR(255) COMMENT '商品图片',
    price DECIMAL(10,2) NOT NULL COMMENT '商品单价',
    quantity INT NOT NULL COMMENT '数量',
    amount DECIMAL(10,2) NOT NULL COMMENT '该项总金额',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_order_id (order_id),
    INDEX idx_food_id (food_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单明细表';

-- 购物车表
CREATE TABLE cart (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    store_id BIGINT NOT NULL COMMENT '店铺ID',
    food_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_store_food (user_id, store_id, food_id),
    INDEX idx_user_id (user_id),
    INDEX idx_store_id (store_id),
    INDEX idx_food_id (food_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车表';

-- 插入测试数据
INSERT INTO cart (user_id, store_id, food_id, quantity) VALUES
(1, 1, 1, 2),
(1, 1, 2, 1),
(2, 2, 3, 1),
(2, 2, 4, 2);

INSERT INTO orders (order_no, user_id, store_id, address_id, total_amount, delivery_fee, discount_amount, payment_amount, payment_type, status, expected_time) VALUES
('ORDER_1732781234567_abc12345', 1, 1, 1, 68.00, 5.00, 0.00, 73.00, 'ALIPAY', 'PAID', DATE_ADD(NOW(), INTERVAL 45 MINUTE)),
('ORDER_1732781234568_def67890', 2, 2, 2, 45.00, 3.00, 0.00, 48.00, 'WECHAT', 'CONFIRMED', DATE_ADD(NOW(), INTERVAL 40 MINUTE));

INSERT INTO order_detail (order_id, food_id, food_name, food_image, price, quantity, amount) VALUES
(1, 1, '宫保鸡丁', '/images/food1.jpg', 28.00, 2, 56.00),
(1, 2, '麻婆豆腐', '/images/food2.jpg', 12.00, 1, 12.00),
(2, 3, '红烧肉', '/images/food3.jpg', 32.00, 1, 32.00),
(2, 4, '青椒土豆丝', '/images/food4.jpg', 13.00, 1, 13.00);
