# Eureka注册中心迁移说明

## 概述
本项目已从Nacos注册中心成功迁移到Eureka注册中心。

## 架构变更

### 原架构（Nacos）
- 注册中心：Nacos Server (端口: 8848)
- 配置中心：Nacos Config

### 新架构（Eureka）
- 注册中心：Eureka Server (端口: 8761)
- 配置中心：移除（使用本地配置文件）

## 服务配置变更

### 1. 新增Eureka Server模块
- 端口：8761
- 功能：服务注册与发现
- 启动类：`com.blm.eureka.EurekaServerApplication`

### 2. 各微服务依赖变更
**移除：**
```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

**新增：**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

### 3. 配置文件变更
**移除 Nacos 配置：**
```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        namespace: blm-microservices
      config:
        server-addr: localhost:8848
        namespace: blm-microservices
        file-extension: yml
```

**新增 Eureka 配置：**
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    register-with-eureka: true
    fetch-registry: true
  instance:
    prefer-ip-address: true
    lease-renewal-interval-in-seconds: 10
    lease-expiration-duration-in-seconds: 30
```

## 服务端口分配

| 服务名称 | 端口 | 说明 |
|---------|------|------|
| eureka-server | 8761 | Eureka注册中心 |
| api-gateway | 8080 | API网关 |
| user-service | 8081 | 用户服务 |
| auth-center | 8082 | 认证中心 |
| order-service | 8084 | 订单服务 |
| store-service | 8085 | 商家服务 |
| rider-service | 8085 | 骑手服务 |

## 启动顺序

1. **Eureka Server** (必须第一个启动)
2. **Auth Center** (认证中心)
3. **API Gateway** (网关)
4. **其他微服务** (用户、订单、商家、骑手服务)

## 启动脚本

### 基础服务启动
```bash
start-eureka-services.bat
```
启动Eureka、认证中心、网关和用户服务。

### 全部服务启动
```bash
start-all-eureka-services.bat
```
启动所有微服务，包括订单、商家、骑手服务。

## 访问地址

- **Eureka控制台**: http://localhost:8761
- **API网关**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html

## 注意事项

1. **启动顺序很重要**：Eureka Server必须先启动，其他服务才能正常注册。

2. **健康检查**：所有服务都配置了健康检查，在Eureka控制台可以看到服务状态。

3. **IP地址偏好**：配置了`prefer-ip-address: true`，服务间通信使用IP地址而非主机名。

4. **服务发现延迟**：Eureka有服务发现延迟（默认30秒），新服务注册后可能需要等待一段时间才能被其他服务发现。

5. **配置中心移除**：不再使用集中式配置管理，所有配置都在各服务的application.yml文件中。

## 迁移验证

1. 启动Eureka Server，访问http://localhost:8761确认控制台正常。
2. 依次启动其他服务，在Eureka控制台确认服务注册成功。
3. 访问API网关的Swagger文档，确认服务间调用正常。
4. 测试各个API接口，确认业务功能正常。

## 故障排除

### 常见问题：

1. **服务注册失败**
   - 检查Eureka Server是否启动
   - 检查网络连接和端口
   - 查看服务日志中的错误信息

2. **服务发现失败**
   - 确认目标服务已在Eureka注册
   - 检查服务名称是否正确
   - 等待服务发现缓存刷新

3. **负载均衡问题**
   - 确认引入了spring-cloud-starter-loadbalancer依赖
   - 检查Feign客户端配置
