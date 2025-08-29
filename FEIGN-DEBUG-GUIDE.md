# Feign 调试指南

## 概述
本指南介绍如何在微服务项目中查看 Feign 发出的请求和响应，方便进行调试。

## 配置说明

### 1. 全局 Feign 配置类
已在 `common/src/main/java/com/blm/common/config/FeignConfig.java` 中创建了全局配置：

```java
@Configuration
public class FeignConfig {
    
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;  // 记录完整的请求和响应信息
    }
}
```

### 2. 日志级别说明
Feign 支持四种日志级别：

- **NONE**: 不记录任何日志（默认）
- **BASIC**: 记录请求方法、URL、响应状态码和执行时间
- **HEADERS**: 在 BASIC 基础上记录请求和响应的头信息
- **FULL**: 记录请求和响应的头信息、正文和元数据（推荐用于调试）

### 3. application.yml 配置
每个服务的 `application.yml` 已配置如下：

```yaml
logging:
  level:
    com.blm: debug
    com.blm.common.feign: debug
    # 启用 Feign 客户端的日志
    org.springframework.cloud.openfeign: debug
    feign: debug
    root: warn

feign:
  client:
    config:
      default:
        # 启用详细日志
        loggerLevel: full
        # 连接超时时间（毫秒）
        connectTimeout: 10000
        # 读取超时时间（毫秒）
        readTimeout: 60000
```

## 如何查看 Feign 调试日志

### 1. 启动服务
确保相关服务已启动：
```bash
# 启动 Eureka 服务注册中心
cd eureka-server && mvn spring-boot:run

# 启动需要调试的服务
cd user-service && mvn spring-boot:run
cd order-service && mvn spring-boot:run
# ... 其他服务
```

### 2. 观察日志输出
当 Feign 客户端发起请求时，你会在控制台看到类似以下的日志：

```log
2025-08-29 10:30:15.123 DEBUG 12345 --- [nio-8084-exec-1] c.b.c.feign.UserServiceClient  : [UserServiceClient#getUserById] ---> GET http://user-service/internal/users/1 HTTP/1.1
2025-08-29 10:30:15.124 DEBUG 12345 --- [nio-8084-exec-1] c.b.c.feign.UserServiceClient  : [UserServiceClient#getUserById] Content-Type: application/json
2025-08-29 10:30:15.124 DEBUG 12345 --- [nio-8084-exec-1] c.b.c.feign.UserServiceClient  : [UserServiceClient#getUserById] Accept: application/json
2025-08-29 10:30:15.124 DEBUG 12345 --- [nio-8084-exec-1] c.b.c.feign.UserServiceClient  : [UserServiceClient#getUserById] 
2025-08-29 10:30:15.234 DEBUG 12345 --- [nio-8084-exec-1] c.b.c.feign.UserServiceClient  : [UserServiceClient#getUserById] <--- HTTP/1.1 200  (110ms)
2025-08-29 10:30:15.235 DEBUG 12345 --- [nio-8084-exec-1] c.b.c.feign.UserServiceClient  : [UserServiceClient#getUserById] content-type: application/json
2025-08-29 10:30:15.235 DEBUG 12345 --- [nio-8084-exec-1] c.b.c.feign.UserServiceClient  : [UserServiceClient#getUserById] date: Thu, 29 Aug 2025 02:30:15 GMT
2025-08-29 10:30:15.235 DEBUG 12345 --- [nio-8084-exec-1] c.b.c.feign.UserServiceClient  : [UserServiceClient#getUserById] 
2025-08-29 10:30:15.235 DEBUG 12345 --- [nio-8084-exec-1] c.b.c.feign.UserServiceClient  : [UserServiceClient#getUserById] {"id":1,"username":"testuser","email":"test@example.com","phone":"1234567890","status":"ACTIVE","createdAt":"2025-08-29T10:25:00"}
```

### 3. 日志信息解读

#### 请求日志格式：
```
[ClientName#methodName] ---> HTTP_METHOD URL HTTP_VERSION
[ClientName#methodName] Header: Value
[ClientName#methodName] 
[ClientName#methodName] Request Body (如果有)
```

#### 响应日志格式：
```
[ClientName#methodName] <--- HTTP_VERSION STATUS_CODE (耗时)
[ClientName#methodName] response-header: value
[ClientName#methodName] 
[ClientName#methodName] Response Body
```

## 针对特定服务的调试配置

### 如果只想查看特定服务的 Feign 日志：

```yaml
logging:
  level:
    # 只查看用户服务相关的 Feign 调用
    com.blm.common.feign.UserServiceClient: debug
    # 只查看订单服务相关的 Feign 调用
    com.blm.common.feign.OrderServiceClient: debug
```

### 如果需要更精细的控制：

```yaml
feign:
  client:
    config:
      # 针对特定服务的配置
      user-service:
        loggerLevel: full
        connectTimeout: 5000
        readTimeout: 30000
      order-service:
        loggerLevel: headers
        connectTimeout: 10000
        readTimeout: 60000
```

## 常见问题排查

### 1. 看不到 Feign 日志
- 检查日志级别是否设置为 `debug`
- 确认 `loggerLevel` 设置为 `full`
- 检查包名是否正确

### 2. 请求超时
- 检查 `connectTimeout` 和 `readTimeout` 配置
- 确认目标服务是否正常运行
- 检查网络连接

### 3. 服务发现问题
- 确认 Eureka 服务注册中心正常运行
- 检查服务是否成功注册到 Eureka
- 查看服务的健康检查状态

## 生产环境注意事项

1. **性能影响**: FULL 级别的日志会记录完整的请求和响应体，在高并发环境下可能影响性能
2. **敏感信息**: 注意不要在生产环境日志中暴露敏感信息（如密码、token等）
3. **日志存储**: 考虑日志的存储空间和轮转策略

建议生产环境使用：
```yaml
feign:
  client:
    config:
      default:
        loggerLevel: basic  # 生产环境使用基础日志级别
```

## 相关文件位置

- Feign 配置类: `common/src/main/java/com/blm/common/config/FeignConfig.java`
- Feign 客户端接口: `common/src/main/java/com/blm/common/feign/`
- 各服务配置: `*/src/main/resources/application.yml`

通过以上配置，你现在可以清楚地看到所有 Feign 客户端发出的 HTTP 请求和接收到的响应，方便进行调试和问题排查。
