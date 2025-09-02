# 构建所有Docker镜像的PowerShell脚本

Write-Host "🚀 开始构建饱了么微服务Docker镜像..." -ForegroundColor Green
Write-Host ""

# 检查Docker是否运行
try {
    docker info | Out-Null
} catch {
    Write-Host "❌ 错误: Docker未运行，请先启动Docker Desktop" -ForegroundColor Red
    exit 1
}

# 清理并构建Maven项目
Write-Host "📦 构建Maven项目..." -ForegroundColor Cyan
mvn clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Maven构建失败" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Maven构建成功" -ForegroundColor Green
Write-Host ""

# 定义服务列表
$services = @(
    "eureka-server",
    "config-server", 
    "api-gateway",
    "auth-center",
    "user-service",
    "order-service",
    "store-service",
    "rider-service",
    "admin-service",
    "file-service"
)

# 构建Docker镜像
Write-Host "🐳 构建Docker镜像..." -ForegroundColor Cyan

foreach ($service in $services) {
    Write-Host "  构建 $service..." -ForegroundColor Yellow
    
    docker build -t "blm/$service" "./$service"
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "  ✅ $service 构建成功" -ForegroundColor Green
    } else {
        Write-Host "  ❌ $service 构建失败" -ForegroundColor Red
        exit 1
    }
}

Write-Host ""
Write-Host "🎉 所有镜像构建完成!" -ForegroundColor Green
Write-Host ""

# 显示镜像列表
Write-Host "📋 构建的镜像列表:" -ForegroundColor Cyan
docker images | Select-String "blm/"

Write-Host ""
Write-Host "💡 提示:" -ForegroundColor Yellow
Write-Host "  • 使用 'docker-compose up -d' 启动所有服务"
Write-Host "  • 使用 './deploy-k8s.ps1' 部署到Kubernetes"
Write-Host "  • 使用 'docker images' 查看所有镜像"
