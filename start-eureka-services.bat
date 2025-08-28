@echo off
chcp 65001
echo 启动饱了么微服务系统 - Eureka版本
echo ======================================
echo.

echo 1. 启动Eureka注册中心...
cd eureka-server
start "Eureka Server" cmd /c "mvn spring-boot:run"
cd ..
timeout /t 15

echo 2. 启动认证中心...
cd auth-center
start "Auth Center" cmd /c "mvn spring-boot:run"
cd ..
timeout /t 10

echo 3. 启动API网关...
cd api-gateway
start "API Gateway" cmd /c "mvn spring-boot:run"
cd ..
timeout /t 10

echo 4. 启动用户服务...
cd user-service
start "User Service" cmd /c "mvn spring-boot:run"
cd ..
timeout /t 5

echo.
echo 所有微服务启动完成！
echo ======================================
echo 访问地址：
echo - Eureka控制台: http://localhost:8761
echo - API网关: http://localhost:8080
echo - Swagger UI: http://localhost:8080/swagger-ui.html
echo ======================================
pause
