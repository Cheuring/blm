# 使用官方的OpenJDK 21运行时作为基础镜像
FROM openjdk:21-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制Maven构建的jar文件
COPY target/*.jar api-gateway.jar

# 暴露端口
EXPOSE 8080

# 设置JVM参数优化
ENV JAVA_OPTS="-Xmx512m -Xms256m -Djava.security.egd=file:/dev/./urandom"

# 运行应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar api-gateway.jar"]
