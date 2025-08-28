@echo off
echo Starting microservices...

echo Starting Nacos (if not already running)...
start "Nacos" cmd /k "cd /d C:\nacos\bin && startup.cmd -m standalone"

timeout /t 10

echo Starting Auth Center...
start "Auth Center" cmd /k "cd /d %~dp0auth-center && mvn spring-boot:run"

timeout /t 5

echo Starting User Service...
start "User Service" cmd /k "cd /d %~dp0user-service && mvn spring-boot:run"

timeout /t 5

echo Starting Store Service...
start "Store Service" cmd /k "cd /d %~dp0store-service && mvn spring-boot:run"

timeout /t 5

echo Starting Order Service...
start "Order Service" cmd /k "cd /d %~dp0order-service && mvn spring-boot:run"

timeout /t 5

echo Starting API Gateway...
start "API Gateway" cmd /k "cd /d %~dp0api-gateway && mvn spring-boot:run"

echo All services are starting...
echo Please wait a few moments for all services to be ready.
echo.
echo Service URLs:
echo - API Gateway: http://localhost:8080
echo - Auth Center: http://localhost:8081
echo - User Service: http://localhost:8082
echo - Store Service: http://localhost:8085
echo - Order Service: http://localhost:8084
echo.
pause
