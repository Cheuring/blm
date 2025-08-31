# 多环境配置指南

## 概述

本项目支持多环境配置管理，包括开发环境(dev)、测试环境(test)和生产环境(prod)。通过Spring Cloud Config配置中心实现统一管理。

## 环境配置文件命名规则

配置文件采用以下命名规则：
```
{application-name}-{profile}.yml
```

例如：
- `user-service-dev.yml` - 用户服务开发环境配置
- `user-service-test.yml` - 用户服务测试环境配置
- `user-service-prod.yml` - 用户服务生产环境配置

## 配置文件结构

```
config-server/src/main/resources/config-repo/
├── user-service.yml           # 默认配置（所有环境共享）
├── user-service-dev.yml       # 开发环境配置
├── user-service-test.yml      # 测试环境配置
├── user-service-prod.yml      # 生产环境配置
├── order-service.yml          # 订单服务默认配置
├── order-service-dev.yml      # 订单服务开发环境配置
├── order-service-test.yml     # 订单服务测试环境配置
├── order-service-prod.yml     # 订单服务生产环境配置
└── ...                        # 其他服务配置
```

## 环境特性对比

| 特性 | 开发环境(dev) | 测试环境(test) | 生产环境(prod) |
|------|--------------|---------------|---------------|
| 数据库 | 本地MySQL | 测试服务器MySQL | 生产集群MySQL |
| Redis | 本地Redis | 测试Redis | 生产Redis集群 |
| 日志级别 | DEBUG | INFO | WARN |
| Swagger | 启用 | 启用 | 禁用 |
| SQL日志 | 启用 | 部分启用 | 禁用 |
| 连接池大小 | 小 | 中等 | 大 |
| 监控端点 | 全部暴露 | 部分暴露 | 受限暴露 |
| 文件上传限制 | 宽松 | 中等 | 严格 |

## 启动不同环境

### 方法一：启动参数指定
```bash
# 启动开发环境
java -jar user-service.jar --spring.profiles.active=dev

# 启动测试环境
java -jar user-service.jar --spring.profiles.active=test

# 启动生产环境
java -jar user-service.jar --spring.profiles.active=prod
```

### 方法二：环境变量指定
```bash
# Windows
set SPRING_PROFILES_ACTIVE=prod
java -jar user-service.jar

# Linux/Mac
export SPRING_PROFILES_ACTIVE=prod
java -jar user-service.jar
```

### 方法三：Maven启动时指定
```bash
# 开发环境
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 生产环境
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### 方法四：Docker环境变量
```dockerfile
# Dockerfile
ENV SPRING_PROFILES_ACTIVE=prod

# 或者docker run时指定
docker run -e SPRING_PROFILES_ACTIVE=prod user-service:latest
```

## 配置优先级

Spring Boot配置加载优先级（从高到低）：
1. 命令行参数
2. 环境变量
3. bootstrap.yml中的配置
4. Config Server中的{application}-{profile}.yml
5. Config Server中的{application}.yml
6. 本地application-{profile}.yml
7. 本地application.yml

## 动态配置刷新

### 手动刷新
```bash
# 刷新指定服务配置
curl -X POST http://localhost:8081/actuator/refresh

# 刷新所有服务配置（通过消息总线）
curl -X POST http://localhost:8888/actuator/bus-refresh
```

### 自动刷新
配置文件变更后，服务会自动检测并刷新配置（需要配置Git钩子或其他触发机制）。

## 环境配置最佳实践

### 1. 敏感信息处理
```yaml
# 使用环境变量替换敏感信息
datasource:
  username: ${DB_USERNAME:default_user}
  password: ${DB_PASSWORD:default_password}
```

### 2. 配置分层
```yaml
# 基础配置放在默认文件中
# user-service.yml
spring:
  application:
    name: user-service

# 环境特定配置放在对应文件中
# user-service-prod.yml
spring:
  datasource:
    url: jdbc:mysql://prod-mysql:3306/user_db_prod
```

### 3. 配置验证
为重要配置添加校验：
```java
@ConfigurationProperties(prefix = "app")
@Validated
public class AppProperties {
    @NotBlank
    private String env;
    
    @Min(1)
    @Max(1000)
    private int maxConnections;
}
```

## 环境切换检查清单

切换环境时需要检查：
- [ ] 数据库连接配置
- [ ] Redis连接配置
- [ ] Eureka注册中心地址
- [ ] 日志级别和输出路径
- [ ] 监控端点暴露策略
- [ ] 安全配置（CORS、认证等）
- [ ] 外部服务地址
- [ ] 文件存储路径
- [ ] 缓存配置
- [ ] 消息队列配置

## 故障排查

### 1. 配置加载失败
```bash
# 检查配置中心连接
curl http://localhost:8888/user-service/dev

# 检查服务配置
curl http://localhost:8081/actuator/env
```

### 2. 环境配置不生效
- 检查profile是否正确激活
- 验证配置文件命名是否正确
- 确认配置中心中是否存在对应配置文件

### 3. 配置冲突
- 检查多个配置源的优先级
- 使用`/actuator/configprops`查看最终生效的配置

## 部署脚本示例

### 开发环境启动脚本
```bash
#!/bin/bash
# start-dev.sh
export SPRING_PROFILES_ACTIVE=dev
export CONFIG_SERVER_URI=http://localhost:8888
java -jar target/user-service.jar
```

### 生产环境启动脚本
```bash
#!/bin/bash
# start-prod.sh
export SPRING_PROFILES_ACTIVE=prod
export CONFIG_SERVER_URI=http://config-server:8888
export DB_USERNAME=prod_user
export DB_PASSWORD=secure_password
java -jar user-service.jar
```

## Docker Compose多环境示例

```yaml
# docker-compose.dev.yml
version: '3.8'
services:
  user-service:
    image: user-service:latest
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - CONFIG_SERVER_URI=http://config-server:8888
    depends_on:
      - config-server
      - mysql-dev

# docker-compose.prod.yml
version: '3.8'
services:
  user-service:
    image: user-service:latest
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - CONFIG_SERVER_URI=http://config-server:8888
      - DB_USERNAME=${DB_USERNAME}
      - DB_PASSWORD=${DB_PASSWORD}
    depends_on:
      - config-server
      - mysql-prod
```
