# Spring Cloud Config 配置中心

## 概述

本项目使用 Spring Cloud Config 作为统一配置中心，实现配置的集中管理和动态刷新。

## 架构设计

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Config        │    │   Eureka        │    │   各个微服务     │
│   Server        │    │   Server        │    │   (Config       │
│   (8888)        │    │   (8761)        │    │   Client)       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                ┌─────────────────┼─────────────────┐
                │                                  │
    ┌─────────────────┐                ┌─────────────────┐
    │   本地配置       │                │   动态配置刷新   │
    │   仓库          │                │   机制          │
    └─────────────────┘                └─────────────────┘
```

## 配置中心特性

1. **集中配置管理**: 所有微服务的配置统一存储在配置中心
2. **环境隔离**: 支持不同环境(dev、test、prod)的配置
3. **动态刷新**: 支持配置的动态更新，无需重启服务
4. **版本控制**: 配置变更有历史记录
5. **安全性**: 支持配置加密

## 服务端口分配

| 服务名称 | 端口 | 说明 |
|---------|------|------|
| eureka-server | 8761 | 服务注册中心 |
| config-server | 8888 | 配置中心 |
| api-gateway | 8080 | API网关 |
| auth-center | 8082 | 认证中心 |
| user-service | 8081 | 用户服务 |
| store-service | 8083 | 商家服务 |
| order-service | 8084 | 订单服务 |
| rider-service | 8085 | 骑手服务 |
| admin-service | 8086 | 管理服务 |

## 配置文件结构

```
config-server/src/main/resources/config-repo/
├── user-service.yml      # 用户服务配置
├── order-service.yml     # 订单服务配置
├── store-service.yml     # 商家服务配置
├── rider-service.yml     # 骑手服务配置
├── admin-service.yml     # 管理服务配置
├── auth-center.yml       # 认证中心配置
└── api-gateway.yml       # API网关配置
```

## 启动顺序

1. **Eureka Server** (8761) - 服务注册中心
2. **Config Server** (8888) - 配置中心
3. **Auth Center** (8082) - 认证中心
4. **API Gateway** (8080) - API网关
5. **业务服务** - 用户、商家、订单、骑手、管理服务

## 使用方法

### 1. 启动所有服务
```bash
# Windows
start-all-services-with-config.bat

# 或者手动逐个启动
mvn spring-boot:run
```

### 2. 验证配置中心
访问 http://localhost:8888/user-service/default 查看用户服务配置

### 3. 访问服务
- Eureka控制台: http://localhost:8761
- 配置中心健康检查: http://localhost:8888/actuator/health
- API网关: http://localhost:8080
- 各服务Swagger文档: http://localhost:{port}/swagger-ui.html

## 配置刷新

### 手动刷新
```bash
# 刷新指定服务的配置
curl -X POST http://localhost:{service-port}/actuator/refresh
```

### 自动刷新
配置中心支持配置变更的自动推送，服务会自动获取最新配置。

## 配置优先级

1. bootstrap.yml (最高优先级)
2. Config Server 配置
3. application.yml (最低优先级)

## 故障处理

### 配置中心不可用
- 服务会使用本地缓存的配置启动
- 可以设置 `spring.cloud.config.fail-fast=false` 降级处理

### 配置加载失败
- 检查配置中心连接状态
- 验证服务名称和配置文件名称匹配
- 查看服务启动日志

## 最佳实践

1. **配置分层**: 公共配置提取到 application.yml，特殊配置放到各服务配置文件
2. **敏感信息加密**: 数据库密码等敏感信息进行加密存储
3. **环境隔离**: 不同环境使用不同的配置分支或文件夹
4. **配置验证**: 重要配置变更前先在测试环境验证
5. **监控告警**: 监控配置中心的可用性和性能

## 开发调试

### 查看配置
```bash
# 查看服务配置
curl http://localhost:8888/{service-name}/default

# 查看配置中心状态
curl http://localhost:8888/actuator/health
```

### 配置热更新
在需要支持热更新的配置类上添加 `@RefreshScope` 注解:

```java
@RestController
@RefreshScope
public class ConfigController {
    @Value("${custom.message:default}")
    private String message;
}
```

## 扩展功能

1. **配置加密**: 支持对敏感配置进行加密
2. **配置审计**: 记录配置变更历史
3. **多环境支持**: 支持开发、测试、生产环境配置隔离
4. **集群部署**: 支持配置中心的高可用部署
