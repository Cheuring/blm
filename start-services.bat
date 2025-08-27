@echo off
echo ================================================
echo 饱了么微服务架构启动脚本
echo ================================================

echo.
echo 检查环境...

:: 检查Java版本
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Java环境，请先安装JDK 21
    pause
    exit /b 1
)

:: 检查Maven
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Maven，请先安装Maven
    pause
    exit /b 1
)

echo Java和Maven环境检查通过

echo.
echo 提示: 请确保以下服务已启动:
echo - MySQL (端口3306)
echo - Redis (端口6379) 
echo - Nacos (端口8848)
echo.

set /p confirm=确认继续启动微服务？(Y/N): 
if /i not "%confirm%"=="Y" (
    echo 取消启动
    pause
    exit /b 0
)

echo.
echo ================================================
echo 开始构建项目...
echo ================================================

:: 构建父工程
echo 构建父工程...
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo 构建失败！
    pause
    exit /b 1
)

echo.
echo ================================================
echo 启动微服务 (请按照提示操作)
echo ================================================

echo.
echo 请按照以下顺序启动服务：
echo.
echo 1. 用户服务 (端口: 8081)
echo 2. 认证中心 (端口: 8082)  
echo 3. API网关 (端口: 8080)
echo.

echo 启动用户服务...
echo 在新窗口中运行: cd user-service && mvn spring-boot:run
start "用户服务" cmd /k "cd user-service && mvn spring-boot:run"

timeout /t 10 /nobreak

echo 启动认证中心...
echo 在新窗口中运行: cd auth-center && mvn spring-boot:run
start "认证中心" cmd /k "cd auth-center && mvn spring-boot:run"

timeout /t 10 /nobreak

echo 启动API网关...
echo 在新窗口中运行: cd api-gateway && mvn spring-boot:run
start "API网关" cmd /k "cd api-gateway && mvn spring-boot:run"

echo.
echo ================================================
echo 启动完成！
echo ================================================
echo.
echo 服务地址:
echo - API网关: http://localhost:8080
echo - 认证中心: http://localhost:8082/swagger-ui.html
echo - 用户服务: http://localhost:8081/swagger-ui.html
echo - Nacos控制台: http://localhost:8848/nacos
echo.
echo 测试用户:
echo - 用户名: testuser
echo - 密码: 123456
echo.

pause
