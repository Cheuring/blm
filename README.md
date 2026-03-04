# 饱了么 (BLM) —— 外卖平台后端

> 本项目是「饱了么」外卖平台的后端系统。最初基于单体架构（[原始仓库](https://gitee.com/shao-tan/buaa-rg-13--backend)），后重构为 **Spring Cloud 微服务架构**，并支持通过 **Kubernetes** 进行容器化部署与管理。

---

## 目录

- [项目简介](#项目简介)
- [技术栈](#技术栈)
- [系统架构](#系统架构)
- [模块说明](#模块说明)
- [数据库设计](#数据库设计)
- [API 接口概览](#api-接口概览)
- [快速开始](#快速开始)
  - [环境要求](#环境要求)
  - [方式一：Docker Compose 本地部署](#方式一docker-compose-本地部署)
  - [方式二：Kubernetes 部署](#方式二kubernetes-部署)
- [默认账号](#默认账号)
- [测试](#测试)

---

## 项目简介

「饱了么」是一个面向用户、商家、骑手和平台管理员的外卖订餐平台，覆盖以下业务场景：

- **用户端**：注册登录、浏览店铺、下单支付、查看订单状态、收藏与浏览历史
- **商家端**：店铺管理、商品管理、订单接单与处理、促销活动管理、数据统计
- **骑手端**：注册认证、抢单/接单、实时位置上报、配送流程管理、收入统计
- **管理后台**：用户/商家/骑手管理、订单监控、店铺审核、商品审核、平台数据统计

---

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 21 | 开发语言 |
| Spring Boot | 3.3.3 | 基础框架 |
| Spring Cloud | 2023.0.3 | 微服务套件 |
| Spring Cloud Eureka | 4.1.3 | 服务注册与发现 |
| Spring Cloud Gateway | 4.1.4 | API 网关 |
| Spring Cloud OpenFeign | 4.1.3 | 服务间 HTTP 调用 |
| Spring Cloud LoadBalancer | 4.1.4 | 客户端负载均衡 |
| Spring Cloud Config | - | 集中配置管理 |
| MySQL | 8.0 | 关系型数据库 |
| Redis | 7.0 | 缓存 |
| MyBatis | 3.0.4 | ORM 框架 |
| PageHelper | 2.1.0 | 分页插件 |
| JJWT | 0.11.5 | JWT 认证 |
| MinIO | 8.5.17 | 对象存储（文件服务） |
| Thumbnailator | 0.4.20 | 图片压缩处理 |
| Lombok | 1.18.30 | 代码简化 |
| SpringDoc OpenAPI | 2.2.0 | Swagger API 文档 |
| Docker | - | 容器化 |
| Kubernetes | - | 容器编排与部署 |

---

## 系统架构

```
                        ┌─────────────────┐
                        │   客户端请求     │
                        └────────┬────────┘
                                 │
                        ┌────────▼────────┐
                        │   API Gateway   │  :8080
                        │  (Spring Cloud  │  JWT 鉴权 & 路由转发
                        │   Gateway)      │
                        └────────┬────────┘
                                 │
          ┌──────────────────────┼──────────────────────┐
          │                      │                       │
   ┌──────▼──────┐  ┌───────────▼──────┐  ┌────────────▼────────┐
   │ auth-center │  │  user-service    │  │   order-service     │
   │    :8082    │  │     :8083        │  │      :8084          │
   │  登录/注册   │  │  用户信息/地址   │  │  购物车/订单管理    │
   │  JWT 生成   │  │  收藏/历史       │  │  评价/骑手位置      │
   └─────────────┘  └──────────────────┘  └─────────────────────┘
          │                      │                       │
   ┌──────▼──────┐  ┌───────────▼──────┐  ┌────────────▼────────┐
   │store-service│  │  rider-service   │  │   admin-service     │
   │    :8085    │  │     :8086        │  │      :8087          │
   │  店铺/商品  │  │  骑手注册/接单   │  │  平台管理/统计      │
   │  促销管理   │  │  位置更新/统计   │  │  审核/数据监控      │
   └─────────────┘  └──────────────────┘  └─────────────────────┘
          │
   ┌──────▼──────┐  ┌───────────────────┐  ┌─────────────────────┐
   │file-service │  │  config-server    │  │   eureka-server     │
   │    :8088    │  │      :8081        │  │       :8761         │
   │  图片上传   │  │  集中配置管理     │  │  服务注册与发现     │
   │  (MinIO)    │  │                   │  │                     │
   └─────────────┘  └───────────────────┘  └─────────────────────┘
          │                      │
   ┌──────▼──────────────────────▼──────┐
   │         MySQL  &  Redis            │
   │    (user_db / store_db /           │
   │     order_db / rider_db)           │
   └────────────────────────────────────┘
```

### 服务间调用关系

服务间通过 **OpenFeign** 进行内部 HTTP 调用，内部接口走 `/internal/**` 路径，不经过 JWT 鉴权：

- **API Gateway** → 所有业务服务（路由转发，提取 JWT 中的 `userId` 注入请求头）
- **order-service** → `user-service`、`store-service`、`rider-service`（创建订单时校验信息）
- **store-service** → `order-service`（商家更新订单状态）
- **rider-service** → `order-service`（骑手接单/取餐/送达）
- **admin-service** → 所有业务服务（聚合管理数据）
- **auth-center** → `user-service`（登录时查询/注册用户）

---

## 模块说明

| 模块 | 端口 | 说明 |
|------|------|------|
| `common` | — | 公共模块，包含共享实体、DTO、VO、Feign 客户端、工具类、统一响应格式 |
| `eureka-server` | 8761 | Eureka 服务注册中心 |
| `config-server` | 8081 | Spring Cloud Config 集中配置服务 |
| `api-gateway` | 8080 | API 网关，负责路由转发与 JWT 认证过滤 |
| `auth-center` | 8082 | 认证中心，处理登录、注册、Token 生成与校验 |
| `user-service` | 8083 | 用户服务，管理用户信息、地址、收藏、浏览历史 |
| `order-service` | 8084 | 订单服务，处理购物车与订单全生命周期 |
| `store-service` | 8085 | 商店服务，支持用户浏览和商家管理店铺、商品、促销 |
| `rider-service` | 8086 | 骑手服务，管理骑手认证、接单配送、位置统计 |
| `admin-service` | 8087 | 管理服务，平台管理员对所有业务的统一管控 |
| `file-service` | 8088 | 文件服务，基于 MinIO 实现图片上传与管理 |

---

## 数据库设计

系统采用**按服务拆分数据库**的策略，各业务域独立维护数据：

### user_db
| 表名 | 说明 |
|------|------|
| `user` | 用户基本信息（角色：`USER` / `MERCHANT` / `RIDER` / `ADMIN`） |
| `user_address` | 用户收货地址 |
| `favorite` | 收藏记录（店铺/商品） |
| `history` | 浏览历史（店铺/商品） |

### store_db
| 表名 | 说明 |
|------|------|
| `store_category` | 店铺类别（快餐、中式料理等） |
| `store` | 商家店铺信息，包含地理位置、营业状态（`PENDING` / `OPEN` / `CLOSED` / `SUSPENDED`） |
| `food_category` | 店铺内商品分类 |
| `food` | 商品信息，含状态（`PENDING` / `ON_SHELF` / `OFF_SHELF` / `SUSPENDED`） |
| `promotion` | 促销活动（折扣类型：`PERCENT` / `AMOUNT` / `SPECIAL`） |

### order_db
| 表名 | 说明 |
|------|------|
| `cart` | 购物车条目 |
| `order` | 订单主表，包含完整的状态流转 |
| `order_detail` | 订单商品明细 |
| `order_tracking` | 订单轨迹记录 |
| `review` | 订单评价 |

### rider_db
| 表名 | 说明 |
|------|------|
| `rider` | 骑手信息，包含实时位置和工作状态（`ONLINE` / `OFFLINE` / `SUSPENDED`） |
| `rider_stats` | 骑手每日统计数据（订单数、收入、在线时长） |

### 订单状态流转

```
ORDER_CREATED（已下单）
    └─► PENDING（待接单，支付后）
        └─► MERCHANT_CONFIRMED（商家已接单）
            └─► COOKING（制作中）
                └─► READY_WAITING_RIDER（待取餐）
                    └─► RIDER_ASSIGNED（骑手已接单）
                        └─► FOOD_PICKED（已取餐）
                            └─► DELIVERING（配送中）
                                └─► DELIVERED（已送达）
                                    └─► COMPLETED（已完成）
（任意阶段可流向）CANCELLED（已取消）
```

---

## API 接口概览

所有接口均通过 API Gateway（`:8080`）统一入口访问。

### 认证接口（无需 Token）
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/login` | 用户登录 |
| POST | `/api/auth/register` | 用户注册 |
| GET | `/api/auth/logout` | 登出 |

### 用户接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/users/profile` | 获取个人资料 |
| PUT | `/api/users/profile` | 更新个人资料 |
| PUT | `/api/users/password` | 修改密码 |
| GET/POST/PUT/DELETE | `/api/addresses/**` | 收货地址管理 |
| GET/POST/DELETE | `/api/favorites/stores` | 店铺收藏管理 |
| GET/POST/DELETE | `/api/favorites/foods` | 商品收藏管理 |
| GET/POST/DELETE | `/api/history/**` | 浏览历史管理 |

### 店铺与商品接口（用户浏览）
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/stores` | 搜索店铺（支持关键词、分类、排序） |
| GET | `/api/stores/recommended` | 推荐店铺 |
| GET | `/api/stores/{id}` | 店铺详情 |
| GET | `/api/stores/{id}/foods` | 店铺商品列表 |
| GET | `/api/categories` | 店铺分类列表 |
| GET | `/api/foods/{id}` | 商品详情 |

### 购物车与订单接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET/POST/PUT/DELETE | `/api/cart/**` | 购物车操作 |
| POST | `/api/orders` | 创建订单 |
| GET | `/api/orders` | 我的订单列表 |
| GET | `/api/orders/{id}` | 订单详情 |
| POST | `/api/orders/pay` | 模拟支付 |
| PUT | `/api/orders/{id}/cancel` | 取消订单 |
| PUT | `/api/orders/{id}/confirm` | 确认收货 |
| POST | `/api/orders/{id}/urge` | 催单 |
| POST | `/api/orders/{id}/reviews` | 评价订单 |
| GET | `/api/orders/{id}/rider/location` | 骑手实时位置 |

### 商家接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET/POST/PUT | `/api/merchant/stores/**` | 店铺管理 |
| GET/POST/PUT/DELETE | `/api/merchant/stores/{id}/foods/**` | 商品管理 |
| GET/POST/PUT/DELETE | `/api/merchant/stores/{id}/promotions/**` | 促销活动管理 |
| GET/PUT | `/api/merchant/orders/{storeId}/**` | 订单管理（接单/制作） |
| GET | `/api/merchant/stores/statics/{storeId}` | 店铺统计数据 |

### 骑手接口
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/rider/register` | 骑手注册认证 |
| GET/PUT | `/api/rider/status` | 工作状态管理 |
| PUT | `/api/rider/location` | 上报实时位置 |
| GET | `/api/rider/orders/available` | 待接订单列表 |
| GET | `/api/rider/orders` | 我的订单 |
| POST | `/api/rider/orders/{id}/accept` | 接单 |
| POST | `/api/rider/orders/{id}/pickup` | 取餐 |
| POST | `/api/rider/orders/{id}/delivered` | 送达 |
| GET | `/api/rider/stats` | 骑手统计数据 |

### 管理员接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET/PUT | `/api/admin/users/**` | 用户管理（列表/启禁） |
| GET/PUT | `/api/admin/riders/**` | 骑手管理 |
| GET/PUT | `/api/admin/stores/**` | 店铺管理与审核 |
| GET/POST/PUT/DELETE | `/api/admin/stores/category/**` | 店铺分类管理 |
| GET | `/api/admin/orders/**` | 订单管理 |
| GET/PUT | `/api/admin/foods/**` | 商品审核 |
| GET | `/api/admin/reviews/**` | 评价管理 |
| GET | `/api/admin/statistics` | 平台统计数据 |

### 文件接口
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/files/image` | 上传图片，返回访问 URL |
| DELETE | `/api/files/image` | 删除指定图片 |

> 各服务 Swagger UI 文档地址：`http://localhost:{port}/swagger-ui.html`

---

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- Docker & Docker Compose（本地部署）
- kubectl（K8s 部署）

### 方式一：Docker Compose 本地部署

这是最简便的本地运行方式，一键启动所有服务。

**1. 构建并启动所有服务**

```bash
docker-compose up --build -d
```

**2. 查看服务状态**

```bash
docker-compose ps
```

**3. 查看日志**

```bash
# 查看所有服务日志
docker-compose logs -f

# 查看指定服务日志
docker-compose logs -f api-gateway
```

**4. 停止服务**

```bash
docker-compose down
```

**服务启动顺序**（已在 `docker-compose.yml` 中通过 `depends_on` 保证）：

```
MySQL & Redis → Eureka Server → Config Server → 各业务服务 → API Gateway
```

> 首次启动时 `db/init-microservices.sql` 会自动初始化数据库结构及测试数据。

**服务访问地址：**

| 服务 | 访问地址 |
|------|---------|
| API Gateway（统一入口） | http://localhost:8080 |
| Eureka 控制台 | http://localhost:8761 |
| Config Server | http://localhost:8081 |
| 认证中心 Swagger | http://localhost:8082/swagger-ui.html |
| 用户服务 Swagger | http://localhost:8083/swagger-ui.html |
| 订单服务 Swagger | http://localhost:8084/swagger-ui.html |
| 商店服务 Swagger | http://localhost:8085/swagger-ui.html |
| 骑手服务 Swagger | http://localhost:8086/swagger-ui.html |
| 管理服务 Swagger | http://localhost:8087/swagger-ui.html |
| 文件服务 Swagger | http://localhost:8088/swagger-ui.html |

### 方式二：Kubernetes 部署

**1. 创建命名空间**

```bash
kubectl apply -f k8s/namespace.yaml
```

**2. 部署基础设施（MySQL & Redis）**

```bash
kubectl apply -f k8s/mysql-deployment.yaml
kubectl apply -f k8s/redis-deployment.yaml
```

**3. 部署配置与注册中心**

```bash
kubectl apply -f k8s/eureka-server.yaml
kubectl apply -f k8s/config-server.yaml
```

**4. 部署所有业务服务**

```bash
kubectl apply -f k8s/auth-center.yaml
kubectl apply -f k8s/user-service.yaml
kubectl apply -f k8s/order-service.yaml
kubectl apply -f k8s/store-service.yaml
kubectl apply -f k8s/rider-service.yaml
kubectl apply -f k8s/admin-service.yaml
kubectl apply -f k8s/file-service.yaml
kubectl apply -f k8s/api-gateway.yaml
```

**5. 查看部署状态**

```bash
kubectl get pods -n blm-microservices
kubectl get services -n blm-microservices
```

**6. 访问服务**

API Gateway 以 `NodePort` 方式暴露，NodePort 为 `30080`：

```
http://<NodeIP>:30080
```

K8s 部署使用 `ghcr.io/cheuring/blm` 仓库的镜像，镜像拉取使用 `github-registry-secret`：

```bash
kubectl create secret docker-registry github-registry-secret \
  --docker-server=ghcr.io \
  --docker-username=<your-github-username> \
  --docker-password=<your-github-token> \
  -n blm-microservices
```

**7. 每个服务的资源限制（生产建议值）**

| 服务 | Memory Request | Memory Limit | CPU Request | CPU Limit |
|------|---------------|-------------|------------|----------|
| API Gateway | 512Mi | 1Gi | 250m | 500m |
| 业务服务（各） | 512Mi | 1Gi | 250m | 500m |

---

## 默认账号

> 密码统一为：`123456`（已 BCrypt 加密存储）

| 角色 | 用户名 | 手机号 |
|------|--------|--------|
| 管理员 | admin | 13800000001 |
| 普通用户 | testuser | 13800000002 |
| 商家 | merchant1 | 13800000003 |
| 骑手 | rider1 | 13800000004 |
| 骑手 | rider2 | 13800000005 |

---

## 测试

项目提供了完整的 Postman 测试集合，位于 `postman_test/` 目录：

| 文件 | 说明 |
|------|------|
| `USER-TEST.postman_collection.json` | 用户端接口测试（登录、下单、查询订单等） |
| `MERCHANT-TEST.postman_collection.json` | 商家端接口测试（店铺管理、商品管理、接单等） |
| `ADMIN-TEST.postman_collection.json` | 管理员端接口测试（用户管理、审核、统计等） |

导入方式：

1. 打开 Postman → **Import**
2. 分别导入对应的 `collection.json` 和 `environment.json`
3. 切换到对应的环境（environment）后运行

> 环境变量文件中已配置好 `baseUrl` 和自动提取 Token 的脚本，可直接运行集合中的请求。

---

## 项目演进说明

本项目从单体架构演进为微服务架构的主要改进点：

| 方面 | 单体架构 | 微服务架构 |
|------|---------|----------|
| 服务拆分 | 所有业务在一个 Spring Boot 应用 | 按业务域拆分为 9 个独立服务 |
| 数据库 | 单一数据库 | 按服务独立数据库（user_db / store_db / order_db / rider_db） |
| 服务通信 | 内部方法调用 | OpenFeign HTTP 调用（走内部 `/internal/**` 接口） |
| 认证 | 单体内 JWT 校验 | 独立 auth-center + API Gateway 统一鉴权 |
| 配置管理 | 各自 `application.yml` | Spring Cloud Config Server 集中管理 |
| 服务发现 | — | Eureka Server 注册中心 |
| 部署方式 | 单 JAR 包部署 | Docker 容器化 + Kubernetes 编排 |
| 负载均衡 | — | Spring Cloud LoadBalancer |

---

## 项目结构

```
blm/
├── common/                 # 公共模块（实体、DTO、VO、Feign 客户端、工具）
├── eureka-server/          # 服务注册中心
├── config-server/          # 集中配置服务
│   └── src/main/resources/config-repo/  # 各服务配置文件存放目录
├── api-gateway/            # API 网关
├── auth-center/            # 认证中心
├── user-service/           # 用户服务
├── order-service/          # 订单服务
├── store-service/          # 商店服务
├── rider-service/          # 骑手服务
├── admin-service/          # 管理服务
├── file-service/           # 文件服务（MinIO）
├── db/
│   └── init-microservices.sql  # 数据库初始化脚本（含测试数据）
├── k8s/                    # Kubernetes 部署配置
│   ├── namespace.yaml
│   ├── mysql.yaml / mysql-deployment.yaml
│   ├── redis-deployment.yaml
│   ├── eureka-server.yaml
│   ├── config-server.yaml
│   ├── auth-center.yaml
│   ├── user-service.yaml
│   ├── order-service.yaml
│   ├── store-service.yaml
│   ├── rider-service.yaml
│   ├── admin-service.yaml
│   ├── file-service.yaml
│   └── api-gateway.yaml
├── postman_test/           # Postman 测试集合
├── docker-compose.yml      # Docker Compose 本地部署配置
└── pom.xml                 # Maven 父工程
```
