# 订单服务API测试指南

## 服务概述

订单服务（order-service）负责处理订单和购物车相关的业务逻辑，包括：

- 购物车管理（添加商品、更新数量、移除商品、清空购物车）
- 订单管理（创建订单、查询订单、取消订单、支付订单、确认收货）
- 内部服务调用接口（供商家服务和骑手服务调用）

## 服务端口和地址

- **服务端口**: 8084
- **服务名称**: order-service
- **API文档**: http://localhost:8084/swagger-ui.html
- **健康检查**: http://localhost:8084/actuator/health

## 主要API接口

### 1. 购物车管理

#### 1.1 获取购物车
```http
GET /api/cart?storeId=1
Headers:
  X-User-Id: 1
  X-User-Role: USER
```

#### 1.2 添加商品到购物车
```http
POST /api/cart
Headers:
  X-User-Id: 1
  X-User-Role: USER
Content-Type: application/json

{
  "storeId": 1,
  "foodId": 1,
  "quantity": 2
}
```

#### 1.3 更新购物车商品数量
```http
PUT /api/cart/{cartItemId}?quantity=3
Headers:
  X-User-Id: 1
  X-User-Role: USER
```

#### 1.4 移除购物车商品
```http
DELETE /api/cart/{cartItemId}
Headers:
  X-User-Id: 1
  X-User-Role: USER
```

#### 1.5 清空购物车
```http
DELETE /api/cart?storeId=1
Headers:
  X-User-Id: 1
  X-User-Role: USER
```

### 2. 订单管理

#### 2.1 创建订单
```http
POST /api/orders
Headers:
  X-User-Id: 1
  X-User-Role: USER
Content-Type: application/json

{
  "storeId": 1,
  "addressId": 1,
  "remark": "少辣"
}
```

#### 2.2 获取用户订单列表
```http
GET /api/orders?page=1&size=10
Headers:
  X-User-Id: 1
  X-User-Role: USER
```

#### 2.3 获取订单详情
```http
GET /api/orders/{orderId}
Headers:
  X-User-Id: 1
  X-User-Role: USER
```

#### 2.4 取消订单
```http
PUT /api/orders/{orderId}/cancel
Headers:
  X-User-Id: 1
  X-User-Role: USER
```

#### 2.5 支付订单
```http
POST /api/orders/payment
Headers:
  X-User-Id: 1
  X-User-Role: USER
Content-Type: application/json

{
  "orderId": 1,
  "paymentType": "ALIPAY"
}
```

#### 2.6 确认收货
```http
PUT /api/orders/{orderId}/confirm
Headers:
  X-User-Id: 1
  X-User-Role: USER
```

### 3. 内部服务调用接口

#### 3.1 商家更新订单状态
```http
PUT /api/orders/{orderId}/status?status=CONFIRMED
Headers:
  X-Store-Id: 1
  X-User-Role: STORE
```

#### 3.2 获取商家订单列表
```http
GET /api/orders/store?page=1&size=10&status=PAID
Headers:
  X-Store-Id: 1
  X-User-Role: STORE
```

#### 3.3 骑手接受订单
```http
PUT /api/orders/{orderId}/accept
Headers:
  X-Rider-Id: 1
  X-User-Role: RIDER
```

#### 3.4 完成配送
```http
PUT /api/orders/{orderId}/complete
Headers:
  X-Rider-Id: 1
  X-User-Role: RIDER
```

## 数据库配置

订单服务需要以下数据库：

```sql
-- 创建数据库
CREATE DATABASE order_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

数据库初始化脚本位于: `src/main/resources/schema.sql`

## 依赖服务

订单服务依赖以下服务：

1. **用户服务（user-service）**: 验证用户地址
2. **商家服务（store-service）**: 验证店铺和商品信息
3. **Nacos**: 服务注册与发现
4. **MySQL**: 数据存储
5. **Redis**: 缓存

## 错误码说明

| 错误码 | 描述 |
|--------|------|
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 测试流程

1. 启动所有依赖服务（Nacos、MySQL、Redis）
2. 启动 auth-center、user-service、store-service
3. 启动 order-service
4. 使用API测试工具（如Postman）进行测试

注意：所有请求都需要经过API网关（如果启用），网关会添加用户信息到请求头中。如果直接调用服务，需要手动添加相应的头信息。
