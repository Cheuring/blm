# 基础镜像
FROM openjdk:17-jdk-slim

# 维护者信息
LABEL maintainer="blm-team"

# 创建工作目录
WORKDIR /app

# 复制 jar 文件
COPY target/config-server-1.0.0.jar app.jar

# 暴露端口
EXPOSE 8888

# 启动命令
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
