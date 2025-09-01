@echo off
chcp 65001
echo 启动饱了么微服务系统（包含订单相关服务）- Eureka版本
echo =================================================
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

echo 5. 启动订单服务...
cd order-service
start "Order Service" cmd /c "mvn spring-boot:run"
cd ..
timeout /t 5

echo 6. 启动商家服务...
cd store-service
start "Store Service" cmd /c "mvn spring-boot:run"
cd ..
timeout /t 5

echo 7. 启动骑手服务...
cd rider-service
start "Rider Service" cmd /c "mvn spring-boot:run"
cd ..
timeout /t 5

echo 8. 启动文件服务...
cd file-service
start "File Service" cmd /c "mvn spring-boot:run"
cd ..
timeout /t 5

echo.
echo 所有微服务启动完成！
echo =================================================
echo 访问地址：
echo - Eureka控制台: http://localhost:8761
echo - API网关: http://localhost:8080
echo - 文件服务: http://localhost:8107
echo - Swagger UI: http://localhost:8080/swagger-ui.html
echo - 文件服务API: http://localhost:8107/swagger-ui.html
echo =================================================
echo.
echo 注意：文件服务需要MinIO存储服务，请确保MinIO已启动
echo MinIO控制台: http://localhost:9001
echo =================================================
pause
