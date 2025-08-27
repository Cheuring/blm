# 微服务配置说明

## 环境配置

### 1. Nacos配置

#### 安装和启动
```bash
# 下载Nacos Server 2.3.x
# 解压后进入bin目录
# Windows
startup.cmd -m standalone

# Linux/Mac
sh startup.sh -m standalone
```

#### 访问控制台
- URL: http://localhost:8848/nacos
- 用户名: nacos
- 密码: nacos

#### 命名空间配置
1. 登录Nacos控制台
2. 进入"命名空间"菜单
3. 新建命名空间: `blm-microservices`
4. 确保所有服务都使用此命名空间

### 2. MySQL配置

#### 数据库准备
```sql
-- 执行初始化脚本
source db/init-microservices.sql;

-- 或者直接导入
mysql -u root -p < db/init-microservices.sql
```

#### 连接配置
- 主机: localhost
- 端口: 3306
- 用户名: root
- 密码: 123456 (请根据实际情况修改)

#### 数据库列表
- `user_db`: 用户服务数据库
- `store_db`: 商家服务数据库 (预留)
- `order_db`: 订单服务数据库 (预留)
- `rider_db`: 骑手服务数据库 (预留)

### 3. Redis配置

#### 安装和启动
```bash
# Windows (使用WSL或下载Windows版本)
redis-server

# Linux
sudo systemctl start redis

# Mac
brew services start redis
```

#### 数据库分配
- Database 0: API网关 (限流等)
- Database 1: 认证中心 (Token黑名单等)
- Database 2: 用户服务 (缓存等)

## 服务配置

### 端口分配
| 服务 | 端口 | 说明 |
|------|------|------|
| API网关 | 8080 | 统一入口 |
| 用户服务 | 8081 | 用户相关API |
| 认证中心 | 8082 | 认证相关API |
| 商家服务 | 8083 | 商家相关API (预留) |
| 订单服务 | 8084 | 订单相关API (预留) |
| 骑手服务 | 8085 | 骑手相关API (预留) |
| 管理后台 | 8086 | 管理相关API (预留) |

### JWT配置
```yaml
jwt:
  secret: blm-takeout-secret-key-for-jwt-token-generation-and-validation-2024
  access-token-expiration: PT2H    # 访问Token有效期: 2小时
  refresh-token-expiration: P7D    # 刷新Token有效期: 7天
```

**生产环境建议**:
- 修改JWT密钥为更复杂的字符串
- 根据业务需求调整Token有效期
- 使用RSA非对称加密替代HS256

### 服务发现配置
```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        namespace: blm-microservices
        group: DEFAULT_GROUP
```

### 网关路由配置
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/users/**
```

## 开发配置

### IDE配置

#### IDEA配置
1. 安装Lombok插件
2. 启用注解处理: Settings -> Build -> Compiler -> Annotation Processors -> Enable annotation processing
3. 配置Java 21: File -> Project Structure -> Project -> Project SDK

#### VS Code配置
安装以下插件:
- Extension Pack for Java
- Spring Boot Extension Pack
- Lombok Annotations Support

### Maven配置
```xml
<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

## 监控配置

### Actuator端点
每个服务都开放了健康检查端点:
- `/actuator/health`: 健康状态
- `/actuator/info`: 服务信息

### 日志配置
```yaml
logging:
  level:
    com.blm: debug
    org.springframework.cloud.gateway: debug
```

## 安全配置

### 认证白名单
API网关配置的无需认证路径:
```java
private static final List<String> WHITE_LIST = Arrays.asList(
    "/api/auth/login",
    "/api/auth/register", 
    "/api/auth/refresh",
    "/api/users/register",
    "/actuator",
    "/v3/api-docs",
    "/swagger-ui"
);
```

### CORS配置
```yaml
spring:
  cloud:
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
            allowedOriginPatterns: "*"
            allowedMethods: "*"
            allowedHeaders: "*"
            allowCredentials: true
```

## 生产环境配置建议

### 1. 配置中心化
使用Nacos Config管理所有配置，避免配置散落在各服务中。

### 2. 数据库连接池
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
```

### 3. Redis连接池
```yaml
spring:
  data:
    redis:
      lettuce:
        pool:
          max-active: 50
          max-idle: 20
          min-idle: 5
          max-wait: 5000ms
```

### 4. JVM参数
```bash
-Xms512m -Xmx1024m 
-XX:+UseG1GC 
-XX:MaxGCPauseMillis=200
-XX:+PrintGCDetails
-XX:+PrintGCTimeStamps
-Xloggc:gc.log
```

### 5. 日志配置
使用logback-spring.xml配置日志:
- 按日期滚动
- 压缩归档
- 异步输出

### 6. 健康检查
```yaml
management:
  endpoint:
    health:
      show-details: always
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
```

## 故障排查

### 常见问题

1. **服务注册失败**
   - 检查Nacos是否启动
   - 检查命名空间配置
   - 检查网络连接

2. **数据库连接失败**
   - 检查MySQL服务状态
   - 检查数据库连接参数
   - 检查数据库权限

3. **Redis连接失败**
   - 检查Redis服务状态
   - 检查Redis连接参数
   - 检查防火墙设置

4. **JWT验证失败**
   - 检查Token是否过期
   - 检查JWT密钥配置
   - 检查请求头格式

5. **网关路由失败**
   - 检查路由配置
   - 检查目标服务状态
   - 检查负载均衡配置

### 日志查看
```bash
# 查看服务日志
tail -f logs/spring.log

# 查看GC日志
tail -f gc.log

# 查看Nacos日志
tail -f nacos/logs/start.out
```
