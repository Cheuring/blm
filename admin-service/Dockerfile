# 基于官方 OpenJDK 镜像
FROM openjdk:21-jdk-alpine

# 创建目录
WORKDIR /app

# 复制 jar 包（假设 jar 包名为 admin-service.jar，实际构建时需用 mvn package 生成）
COPY target/*.jar app.jar

# 暴露端口（根据实际服务端口调整）
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
