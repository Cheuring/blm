-- 商家服务数据库表结构
USE store_db;

-- 店铺分类表
CREATE TABLE IF NOT EXISTS store_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    icon VARCHAR(255),
    sort INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) COMMENT '店铺分类表';

-- 商家店铺表
CREATE TABLE IF NOT EXISTS store (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    merchant_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    logo VARCHAR(255),
    description TEXT,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(255) NOT NULL,
    longitude DECIMAL(10,7) COMMENT '经度',
    latitude DECIMAL(10,7) COMMENT '纬度',
    business_hours VARCHAR(100) COMMENT '营业时间',
    delivery_fee DECIMAL(10,2) DEFAULT 0,
    min_order_amount DECIMAL(10,2) DEFAULT 0,
    average_delivery_time INT COMMENT '平均配送时间(分钟)',
    category_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL COMMENT '状态：PENDING/SUSPENDED/OPEN/CLOSED',
    rating DECIMAL(2,1) DEFAULT 5.0,
    monthly_sales INT DEFAULT 0 COMMENT '月销量',
    license_img VARCHAR(255) COMMENT '营业执照图片',
    permit_img VARCHAR(255) COMMENT '许可证图片',
    reject_reason VARCHAR(255) COMMENT '拒绝原因',
    is_featured TINYINT DEFAULT 0 COMMENT '是否推荐：0-否，1-是',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES store_category(id),
    INDEX idx_merchant_id (merchant_id),
    INDEX idx_location (longitude, latitude),
    INDEX idx_rating (rating),
    INDEX idx_sales (monthly_sales)
) COMMENT '商家店铺表';

-- 商品分类表
CREATE TABLE IF NOT EXISTS food_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    sort INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (store_id) REFERENCES store(id) ON DELETE CASCADE,
    INDEX idx_store_id (store_id)
) COMMENT '商品分类表';

-- 商品表
CREATE TABLE IF NOT EXISTS food (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id BIGINT NOT NULL,
    category_id BIGINT COMMENT '商品分类ID',
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    original_price DECIMAL(10,2),
    description TEXT,
    image VARCHAR(255),
    sales INT DEFAULT 0 COMMENT '销量',
    stock INT DEFAULT 0 COMMENT '库存',
    status VARCHAR(20) NOT NULL COMMENT '状态：OFF_SHELF/ON_SHELF/SUSPENDED/PENDING',
    reject_reason VARCHAR(255) COMMENT '拒绝原因',
    is_featured TINYINT DEFAULT 0 COMMENT '是否推荐：0-否，1-是',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (store_id) REFERENCES store(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES food_category(id) ON DELETE SET NULL,
    INDEX idx_store_id (store_id),
    INDEX idx_category_id (category_id),
    INDEX idx_status (status)
) COMMENT '商品表';

-- 促销活动表
CREATE TABLE IF NOT EXISTS promotion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    discount_type VARCHAR(20) NOT NULL COMMENT '折扣类型：PERCENT/AMOUNT/SPECIAL',
    discount_value DECIMAL(10,2) NOT NULL COMMENT '折扣值',
    min_order_amount DECIMAL(10,2) DEFAULT 0,
    status TINYINT DEFAULT 1 COMMENT '状态：0-未开始，1-进行中，2-已结束',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (store_id) REFERENCES store(id) ON DELETE CASCADE,
    INDEX idx_store_id (store_id),
    INDEX idx_time (start_time, end_time)
) COMMENT '促销活动表';

-- 插入测试数据
INSERT INTO store_category (name, icon, sort) VALUES
('快餐简餐', '/icons/fast-food.png', 1),
('中式料理', '/icons/chinese.png', 2),
('西式料理', '/icons/western.png', 3),
('日韩料理', '/icons/japanese.png', 4),
('甜品饮品', '/icons/dessert.png', 5);

INSERT INTO store (merchant_id, name, logo, description, phone, address, longitude, latitude, business_hours, delivery_fee, min_order_amount, category_id, status) VALUES
(1, '川味小厨', '/logos/store1.jpg', '正宗川菜，香辣美味', '010-12345678', '北京市朝阳区建国路88号', 116.4074, 39.9042, '09:00-22:00', 5.00, 20.00, 2, 'OPEN'),
(1, '汉堡王', '/logos/store2.jpg', '美味汉堡，快速送达', '010-87654321', '北京市海淀区中关村大街1号', 116.3112, 39.9991, '10:00-23:00', 3.00, 15.00, 1, 'OPEN');

INSERT INTO food_category (store_id, name, sort) VALUES
(1, '招牌菜', 1),
(1, '汤类', 2),
(1, '素食', 3),
(2, '汉堡类', 1),
(2, '小食', 2),
(2, '饮品', 3);

INSERT INTO food (store_id, category_id, name, price, original_price, description, image, stock, status) VALUES
(1, 1, '宫保鸡丁', 28.00, 32.00, '经典川菜，鸡肉嫩滑，花生酥脆', '/foods/gongbao.jpg', 100, 'ON_SHELF'),
(1, 1, '麻婆豆腐', 18.00, 20.00, '麻辣鲜香，嫩滑豆腐', '/foods/mapo.jpg', 80, 'ON_SHELF'),
(1, 2, '酸辣汤', 12.00, 15.00, '开胃酸辣汤', '/foods/soup.jpg', 50, 'ON_SHELF'),
(2, 1, '经典牛肉堡', 25.00, 28.00, '新鲜牛肉饼配新鲜蔬菜', '/foods/burger1.jpg', 200, 'ON_SHELF'),
(2, 1, '鸡肉汉堡', 22.00, 25.00, '香嫩鸡胸肉汉堡', '/foods/burger2.jpg', 150, 'ON_SHELF'),
(2, 3, '可口可乐', 8.00, 10.00, '经典可乐', '/foods/cola.jpg', 300, 'ON_SHELF');

INSERT INTO promotion (store_id, name, description, start_time, end_time, discount_type, discount_value, min_order_amount, status) VALUES
(1, '周末特惠', '周末全场8折', '2024-01-01 00:00:00', '2024-12-31 23:59:59', 'PERCENT', 0.80, 30.00, 1),
(2, '新用户优惠', '新用户立减10元', '2024-01-01 00:00:00', '2024-12-31 23:59:59', 'AMOUNT', 10.00, 25.00, 1);
