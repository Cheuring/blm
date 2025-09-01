FROM openjdk:21-jdk-slim

LABEL maintainer="baoleme-team"
LABEL service="file-service"

# 设置工作目录
WORKDIR /app

# 复制jar文件
COPY target/file-service-1.0.0.jar app.jar

# 创建用户（安全考虑）
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# 暴露端口
EXPOSE 8107

# 设置JVM参数
ENV JAVA_OPTS="-Xms512m -Xmx1024m -server -XX:+UseG1GC"

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
