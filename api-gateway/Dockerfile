# 基于官方 OpenJDK 镜像
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/api-gateway.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
