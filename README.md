# 饱了么外卖平台微服务架构

基于Spring Boot 3.3 + Spring Cloud 2023 + Eureka的微服务架构实现。

## 项目结构

```
micro1/
├── pom.xml                    # 父工程Maven配置
├── common/                    # 公共模块
│   ├── src/main/java/com/blm/common/
│   │   ├── result/           # 统一返回结果封装
│   │   ├── entity/           # 公共实体类
│   │   ├── vo/               # 视图对象
│   │   ├── dto/              # 数据传输对象
│   │   ├── util/             # 工具类 (JWT等)
│   │   ├── feign/            # Feign客户端接口
│   │   └── exception/        # 异常处理
├── eureka-server/            # Eureka注册中心
├── api-gateway/              # API网关
├── auth-center/              # 认证中心
├── user-service/             # 用户服务
└── db/                       # 数据库脚本
    └── init-microservices.sql
```

## 微服务说明

### 1. Eureka注册中心 (eureka-server)
**职责**: 服务注册与发现
- **端口**: 8761
- **主要功能**:
  - 服务注册
  - 服务发现
  - 健康检查
  - 服务实例管理

### 2. 公共模块 (common)
- 通用的实体类、工具类、常量等
- 统一返回结果封装
- JWT工具类
- Feign客户端接口
- 异常处理

### 3. API网关 (api-gateway)
**职责**: 统一入口、路由转发、认证鉴权、限流熔断
- **端口**: 8080
- **主要功能**:
  - 请求路由转发
  - JWT认证过滤
  - 跨域处理
  - 负载均衡

### 4. 认证中心 (auth-center)
**职责**: 统一认证、Token管理
- **端口**: 8082
- **主要功能**:
  - 用户登录认证
  - JWT Token生成和验证
  - Token刷新
  - 用户登出

### 5. 用户服务 (user-service)
**职责**: 用户注册、信息管理、地址管理
- **数据库**: user_db
- **端口**: 8081
- **主要功能**:
  - 用户注册 (`POST /api/users/register`)
  - 用户信息管理 (`GET/PUT /api/users/profile`)
  - 用户地址管理 (`/api/users/addresses`)

## 技术栈

- **基础框架**: Spring Boot 3.3.3
- **微服务**: Spring Cloud 2023.0.3
- **服务注册与发现**: Eureka
- **API网关**: Spring Cloud Gateway
- **服务调用**: OpenFeign + LoadBalancer
- **数据库**: MySQL 8.0
- **ORM**: MyBatis + PageHelper
- **缓存**: Redis
- **认证**: JWT + Spring Security
- **文档**: SpringDoc OpenAPI 3
- **工具**: Lombok

## 快速开始

### 1. 环境准备
- JDK 21
- MySQL 8.0+
- Redis 6.0+

### 2. 初始化数据库
```bash
mysql -u root -p < db/init-microservices.sql
```

### 3. 启动服务

#### 方式一：使用启动脚本（推荐）
```bash
# 启动基础服务（Eureka + 认证 + 网关 + 用户服务）
start-eureka-services.bat

# 或启动全部服务
start-all-eureka-services.bat
```

#### 方式二：手动启动
按以下顺序启动各个服务：

1. **启动Eureka注册中心**
```bash
cd eureka-server
mvn spring-boot:run
```

2. **启动认证中心**
```bash
cd auth-center
mvn spring-boot:run
```

3. **启动API网关**
```bash
cd api-gateway
mvn spring-boot:run
```

4. **启动用户服务**
```bash
cd user-service
mvn spring-boot:run
```

### 4. 访问地址
- **Eureka控制台**: http://localhost:8761
- **API网关**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html

### 4. 启动服务
按以下顺序启动各个服务：

1. **启动用户服务**
```bash
cd user-service
mvn spring-boot:run
```

2. **启动认证中心**
```bash
cd auth-center
mvn spring-boot:run
```

3. **启动API网关**
```bash
cd api-gateway
mvn spring-boot:run
```

### 5. 验证服务
- API网关: http://localhost:8080
- 认证中心: http://localhost:8082
- 用户服务: http://localhost:8081

## API文档

各服务的API文档地址：
- 认证中心: http://localhost:8082/swagger-ui.html
- 用户服务: http://localhost:8081/swagger-ui.html

通过网关访问：
- http://localhost:8080/swagger-ui.html

## 配置说明

### Nacos配置
所有服务都使用Nacos作为服务注册与发现中心，命名空间为 `blm-microservices`。

### JWT配置
- 访问Token有效期: 2小时
- 刷新Token有效期: 7天
- 密钥可在各服务的 `application.yml` 中配置

### Redis配置
- API网关使用 database 0
- 认证中心使用 database 1
- 用户服务使用 database 2

## 服务间调用

使用OpenFeign进行服务间调用，Feign客户端接口统一放在 `common` 模块中：

```java
@FeignClient(name = "user-service", path = "/api/users")
public interface UserServiceClient {
    @GetMapping("/{userId}")
    Result<UserVO> getUserById(@PathVariable("userId") Long userId);
}
```

## 认证流程

1. 用户通过API网关访问认证中心进行登录
2. 认证中心调用用户服务验证用户信息
3. 认证成功后生成JWT Token返回
4. 后续请求携带Token通过API网关
5. API网关验证Token有效性并提取用户信息
6. 将用户ID添加到请求头转发给后端服务

## 扩展指南

### 添加新的微服务

1. 在父工程 `pom.xml` 中添加新模块
2. 创建服务模块，继承父工程
3. 添加相应的依赖（Nacos、OpenFeign等）
4. 在API网关中配置路由规则
5. 在 `common` 模块中添加Feign客户端接口

### 数据库设计
每个微服务拥有独立的数据库，实现数据隔离：
- `user_db`: 用户相关数据
- `store_db`: 商家相关数据（待实现）
- `order_db`: 订单相关数据（待实现）
- `rider_db`: 骑手相关数据（待实现）

## 注意事项

1. 服务启动顺序：确保Nacos先启动，然后按依赖关系启动各服务
2. 数据库连接：确保各服务能连接到对应的数据库
3. Redis连接：确保Redis服务正常运行
4. 端口冲突：确保各服务端口不冲突
5. JWT密钥：生产环境请修改JWT密钥

## 下一步计划

1. 实现商家服务 (store-service)
2. 实现订单服务 (order-service)
3. 实现骑手服务 (rider-service)
4. 实现管理后台服务 (admin-service)
5. 添加消息队列 (RabbitMQ/RocketMQ)
6. 添加分布式事务处理
7. 添加服务监控 (Micrometer + Prometheus)
8. 添加链路追踪 (Zipkin/Jaeger)
9. 容器化部署 (Docker + Kubernetes)
