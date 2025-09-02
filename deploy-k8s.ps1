# Windows PowerShell部署脚本

# 启用Kubernetes
Write-Host "启用Docker Desktop中的Kubernetes..." -ForegroundColor Green
Write-Host "请确保已在Docker Desktop中启用Kubernetes支持" -ForegroundColor Yellow
Write-Host ""

# 检查kubectl是否安装
if (-not (Get-Command kubectl -ErrorAction SilentlyContinue)) {
    Write-Host "错误: kubectl未安装" -ForegroundColor Red
    Write-Host "请先安装kubectl: https://kubernetes.io/docs/tasks/tools/" -ForegroundColor Yellow
    exit 1
}

# 检查Docker Desktop是否运行
try {
    docker info | Out-Null
} catch {
    Write-Host "错误: Docker Desktop未运行" -ForegroundColor Red
    Write-Host "请先启动Docker Desktop" -ForegroundColor Yellow
    exit 1
}

# 检查Kubernetes是否可用
try {
    kubectl cluster-info | Out-Null
} catch {
    Write-Host "错误: Kubernetes未启用或未连接" -ForegroundColor Red
    Write-Host "请在Docker Desktop中启用Kubernetes" -ForegroundColor Yellow
    exit 1
}

Write-Host "✅ 环境检查通过" -ForegroundColor Green
Write-Host ""

# 创建命名空间
Write-Host "创建Kubernetes命名空间..." -ForegroundColor Green
kubectl apply -f k8s/namespace.yaml

# 部署基础设施 (MySQL, Redis)
Write-Host "部署基础设施服务..." -ForegroundColor Green
kubectl apply -f k8s/mysql.yaml
kubectl apply -f k8s/mysql-deployment.yaml
kubectl apply -f k8s/redis-deployment.yaml

Write-Host "等待基础设施服务启动..." -ForegroundColor Yellow
kubectl wait --for=condition=ready pod -l app=mysql -n blm-microservices --timeout=300s
kubectl wait --for=condition=ready pod -l app=redis -n blm-microservices --timeout=300s

# 部署Eureka注册中心
Write-Host "部署Eureka注册中心..." -ForegroundColor Green
kubectl apply -f k8s/eureka-server.yaml
kubectl wait --for=condition=ready pod -l app=eureka-server -n blm-microservices --timeout=300s

# 部署配置中心
Write-Host "部署配置中心..." -ForegroundColor Green
kubectl apply -f k8s/config-server.yaml
kubectl wait --for=condition=ready pod -l app=config-server -n blm-microservices --timeout=300s

# 部署微服务
Write-Host "部署微服务..." -ForegroundColor Green
kubectl apply -f k8s/auth-center.yaml
kubectl apply -f k8s/user-service.yaml
kubectl apply -f k8s/order-service.yaml
kubectl apply -f k8s/store-service.yaml
kubectl apply -f k8s/rider-service.yaml
kubectl apply -f k8s/admin-service.yaml
kubectl apply -f k8s/file-service.yaml

Write-Host "等待微服务启动..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

# 部署API网关
Write-Host "部署API网关..." -ForegroundColor Green
kubectl apply -f k8s/api-gateway.yaml
kubectl wait --for=condition=ready pod -l app=api-gateway -n blm-microservices --timeout=300s

Write-Host ""
Write-Host "✅ 部署完成!" -ForegroundColor Green
Write-Host ""
Write-Host "查看部署状态:" -ForegroundColor Cyan
kubectl get pods -n blm-microservices
Write-Host ""
Write-Host "访问应用:" -ForegroundColor Cyan
Write-Host "API网关: http://localhost:30080" -ForegroundColor Yellow
Write-Host "Eureka: kubectl port-forward svc/eureka-service 8761:8761 -n blm-microservices" -ForegroundColor Yellow
Write-Host ""
Write-Host "查看日志:" -ForegroundColor Cyan
Write-Host "kubectl logs -f deployment/api-gateway -n blm-microservices" -ForegroundColor Yellow
