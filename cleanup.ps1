# 清理Kubernetes部署的PowerShell脚本

param(
    [switch]$All,
    [switch]$Pods,
    [switch]$Services, 
    [switch]$Images,
    [switch]$Namespace,
    [switch]$Force
)

Write-Host "🧹 饱了么微服务清理脚本" -ForegroundColor Green
Write-Host ""

if (-not ($All -or $Pods -or $Services -or $Images -or $Namespace)) {
    Write-Host "用法: ./cleanup.ps1 [选项]" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "选项:" -ForegroundColor Cyan
    Write-Host "  -All        清理所有资源 (Pod, Service, 镜像, 命名空间)"
    Write-Host "  -Pods       只清理Pod" 
    Write-Host "  -Services   只清理Service"
    Write-Host "  -Images     只清理Docker镜像"
    Write-Host "  -Namespace  删除整个命名空间"
    Write-Host "  -Force      强制删除，不询问确认"
    Write-Host ""
    Write-Host "示例:" -ForegroundColor Yellow
    Write-Host "  ./cleanup.ps1 -All          # 清理所有"
    Write-Host "  ./cleanup.ps1 -Pods         # 只清理Pod"
    Write-Host "  ./cleanup.ps1 -Images       # 只清理镜像"
    exit 0
}

# 确认操作
if (-not $Force) {
    $confirmation = Read-Host "⚠️  确定要执行清理操作吗? (y/N)"
    if ($confirmation -ne 'y' -and $confirmation -ne 'Y') {
        Write-Host "❌ 操作已取消" -ForegroundColor Red
        exit 0
    }
}

# 检查kubectl是否可用
if (-not (Get-Command kubectl -ErrorAction SilentlyContinue)) {
    Write-Host "❌ kubectl未找到，请先安装kubectl" -ForegroundColor Red
    exit 1
}

# 清理Kubernetes资源
if ($All -or $Pods -or $Services -or $Namespace) {
    Write-Host "🗑️  清理Kubernetes资源..." -ForegroundColor Cyan
    
    if ($All -or $Namespace) {
        Write-Host "  删除命名空间 blm-microservices..." -ForegroundColor Yellow
        kubectl delete namespace blm-microservices --ignore-not-found=true
        Write-Host "  ✅ 命名空间已删除" -ForegroundColor Green
    } else {
        # 检查命名空间是否存在
        $nsExists = kubectl get namespace blm-microservices --ignore-not-found=true
        if (-not $nsExists) {
            Write-Host "  ℹ️  命名空间 blm-microservices 不存在" -ForegroundColor Blue
        } else {
            if ($All -or $Pods) {
                Write-Host "  删除所有Pod..." -ForegroundColor Yellow
                kubectl delete pods --all -n blm-microservices
                Write-Host "  ✅ Pod已删除" -ForegroundColor Green
            }
            
            if ($All -or $Services) {
                Write-Host "  删除所有Service..." -ForegroundColor Yellow
                kubectl delete services --all -n blm-microservices
                Write-Host "  ✅ Service已删除" -ForegroundColor Green
            }
        }
    }
}

# 清理Docker镜像
if ($All -or $Images) {
    Write-Host "🐳 清理Docker镜像..." -ForegroundColor Cyan
    
    # 停止所有运行的容器
    $runningContainers = docker ps -q --filter "ancestor=blm/*"
    if ($runningContainers) {
        Write-Host "  停止运行中的容器..." -ForegroundColor Yellow
        docker stop $runningContainers
        Write-Host "  ✅ 容器已停止" -ForegroundColor Green
    }
    
    # 删除blm相关的容器
    $blmContainers = docker ps -a -q --filter "ancestor=blm/*"
    if ($blmContainers) {
        Write-Host "  删除blm相关容器..." -ForegroundColor Yellow
        docker rm $blmContainers
        Write-Host "  ✅ 容器已删除" -ForegroundColor Green
    }
    
    # 删除blm相关的镜像
    $blmImages = docker images -q "blm/*"
    if ($blmImages) {
        Write-Host "  删除blm相关镜像..." -ForegroundColor Yellow
        docker rmi $blmImages -f
        Write-Host "  ✅ 镜像已删除" -ForegroundColor Green
    }
    
    # 删除GitHub Container Registry的镜像
    $ghcrImages = docker images -q "ghcr.io/cheuring/blm/*"
    if ($ghcrImages) {
        Write-Host "  删除GHCR镜像..." -ForegroundColor Yellow
        docker rmi $ghcrImages -f
        Write-Host "  ✅ GHCR镜像已删除" -ForegroundColor Green
    }
    
    # 清理未使用的镜像和网络
    Write-Host "  清理未使用的Docker资源..." -ForegroundColor Yellow
    docker system prune -f
    Write-Host "  ✅ Docker资源清理完成" -ForegroundColor Green
}

Write-Host ""
Write-Host "🎉 清理完成!" -ForegroundColor Green
Write-Host ""

# 显示当前状态
Write-Host "📊 当前状态:" -ForegroundColor Cyan

# 检查Kubernetes状态
$nsExists = kubectl get namespace blm-microservices --ignore-not-found=true 2>$null
if ($nsExists) {
    Write-Host "  Kubernetes:" -ForegroundColor Yellow
    kubectl get pods -n blm-microservices 2>$null
    if ($LASTEXITCODE -ne 0) {
        Write-Host "    无Pod运行" -ForegroundColor Blue
    }
} else {
    Write-Host "  Kubernetes: 命名空间不存在" -ForegroundColor Blue
}

# 检查Docker状态
Write-Host "  Docker镜像:" -ForegroundColor Yellow
$blmImages = docker images --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}" | Select-String "blm/"
if ($blmImages) {
    $blmImages
} else {
    Write-Host "    无blm相关镜像" -ForegroundColor Blue
}

Write-Host ""
Write-Host "💡 提示:" -ForegroundColor Yellow
Write-Host "  • 重新部署: ./deploy-k8s.ps1"
Write-Host "  • 重新构建: ./build-images.ps1"
Write-Host "  • 查看帮助: ./cleanup.ps1"
