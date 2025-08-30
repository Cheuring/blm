# 微服务 Docker 与 Jenkins 自动化流水线说明

## 1. Dockerfile 说明

每个微服务（admin-service、api-gateway、auth-center、order-service、rider-service、store-service、user-service）目录下均已配置标准 Spring Boot Dockerfile，产物为 target/xxx.jar，端口默认 8080。

## 2. .dockerignore 说明

每个服务目录下均有 .dockerignore，避免源码、测试、git、IDE 等无关内容进入镜像上下文。

## 3. Jenkinsfile 说明

根目录 Jenkinsfile 已实现如下自动化流程：

- 拉取代码
- 依次进入每个微服务目录，mvn package 构建 jar 包
- 构建 Docker 镜像
- 登录 Docker Hub
- 推送镜像

流水线变量说明：

- `DOCKERHUB_CREDENTIALS`：Jenkins 凭证 ID，类型为“用户名+密码”，需提前在 Jenkins 全局凭证中配置（如 dockerhub-credentials）。
- `DOCKERHUB_NAMESPACE`：Docker Hub 命名空间（如 your_dockerhub_namespace），请根据实际情况修改。
- `SERVICES`：需构建的微服务目录名，默认包含全部服务。

## 4. 使用方法

1. 在 Jenkins 全局凭证中添加 Docker Hub 账号密码，ID 设为 `dockerhub-credentials`。
2. 修改 Jenkinsfile 中 `DOCKERHUB_NAMESPACE` 为你的 Docker Hub 命名空间。
3. 配置流水线任务，选择本仓库 Jenkinsfile。
4. 运行流水线，自动完成所有微服务的构建与推送。

## 5. 注意事项

- 需保证 Jenkins 节点已安装 Docker、Maven，并有权限执行 docker build/push。
- 每次构建镜像时，自动生成唯一版本号 tag（如 `${BUILD_NUMBER}`），并同时推送 `latest` tag。
- 推荐在 k8s 或其他部署场景中优先使用带版本号的 tag，便于回滚和溯源。
- 如需扩展服务，仅需在 SERVICES 变量中添加目录名并补充对应 Dockerfile。

---

如有问题请联系 DevOps 负责人。

---

## 后端依赖服务连接说明

所有后端微服务（如 admin-service、order-service 等）依赖 mysql、redis、minio 等服务时，建议通过环境变量配置连接信息。例如：

- MySQL: `MYSQL_HOST`、`MYSQL_PORT`、`MYSQL_USER`、`MYSQL_PASSWORD`
- Redis: `REDIS_HOST`、`REDIS_PORT`
- Minio: `MINIO_ENDPOINT`、`MINIO_ACCESS_KEY`、`MINIO_SECRET_KEY`

实际部署时（如 docker-compose、k8s），通过环境变量注入上述参数，Spring Boot 项目 application.yml/application.properties 可用 `${ENV_VAR}` 方式引用。例如：

```yaml
spring:
  datasource:
    url: jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/yourdb
    username: ${MYSQL_USER:root}
    password: ${MYSQL_PASSWORD:password}
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
minio:
  endpoint: ${MINIO_ENDPOINT:http://localhost:9000}
  access-key: ${MINIO_ACCESS_KEY:minioadmin}
  secret-key: ${MINIO_SECRET_KEY:minioadmin}
```

这样可实现容器化部署时灵活配置依赖服务地址，无需修改镜像内容。
