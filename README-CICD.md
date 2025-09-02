# CI/CD & Kubernetes 部署指南

本文档介绍如何在Windows 11环境下使用GitHub Actions + Docker Desktop + Kubernetes实现饱了么微服务项目的CI/CD。

## 🛠️ 环境要求

### 1. 必需软件
- **Windows 11**
- **Docker Desktop** (4.15+)
- **Git**
- **VS Code** (推荐)

### 2. Docker Desktop配置
1. 下载并安装 [Docker Desktop](https://www.docker.com/products/docker-desktop/)
2. 启动Docker Desktop
3. 在设置中启用Kubernetes：
   - 打开 Docker Desktop
   - 进入 Settings → Kubernetes
   - 勾选 "Enable Kubernetes"
   - 点击 "Apply & Restart"

### 3. 验证环境
```powershell
# 检查Docker
docker --version
docker info

# 检查Kubernetes
kubectl version --client
kubectl cluster-info
```

## 🚀 快速开始

### 1. 本地构建和部署

#### 使用Docker Compose (推荐用于开发环境)
```powershell
# 构建所有服务
mvn clean package -DskipTests

# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f api-gateway

# 停止服务
docker-compose down
```

#### 使用Kubernetes (推荐用于生产环境)
```powershell
# 构建项目
mvn clean package -DskipTests

# 构建Docker镜像
./build-images.ps1

# 部署到Kubernetes
./deploy-k8s.ps1

# 查看部署状态
kubectl get pods -n blm-microservices
```

### 2. 访问应用

| 服务 | 本地端口 | K8s端口 | 说明 |
|------|----------|---------|------|
| API网关 | 8080 | 30080 | 主要入口 |
| Eureka | 8761 | - | 服务注册中心 |
| MySQL | 3306 | - | 数据库 |
| Redis | 6379 | - | 缓存 |

**访问地址:**
- API网关: http://localhost:8080 (Docker Compose) 或 http://localhost:30080 (K8s)
- Eureka控制台: http://localhost:8761

## 🔄 CI/CD 工作流

### GitHub Actions 流水线

我们的CI/CD流水线包含以下阶段：

1. **测试阶段** (Test)
   - 运行单元测试
   - 生成测试报告

2. **构建和推送阶段** (Build & Push)
   - Maven构建JAR包
   - 构建Docker镜像
   - 推送到GitHub Container Registry

3. **部署阶段** (Deploy)
   - 部署到Kubernetes集群
   - 滚动更新应用

### 触发条件
- 推送到 `main`, `develop`, `micro_k8s` 分支
- 创建Pull Request到 `main`, `develop` 分支

### 需要配置的GitHub Secrets
```
KUBE_CONFIG: your-kubernetes-config-base64-encoded
```

## 📁 项目结构

```
micro1/
├── .github/workflows/          # GitHub Actions工作流
│   └── ci-cd.yml
├── k8s/                       # Kubernetes配置文件
│   ├── namespace.yaml
│   ├── mysql.yaml
│   ├── mysql-deployment.yaml
│   ├── redis-deployment.yaml
│   ├── eureka-server.yaml
│   ├── config-server.yaml
│   ├── api-gateway.yaml
│   └── ...
├── eureka-server/             # Eureka注册中心
│   ├── Dockerfile
│   └── src/main/resources/
│       └── application-k8s.yml
├── api-gateway/              # API网关
│   ├── Dockerfile
│   └── src/main/resources/
│       └── application-k8s.yml
├── [other-services]/         # 其他微服务
├── docker-compose.yml        # Docker Compose配置
├── deploy-k8s.ps1           # K8s部署脚本
└── README-CICD.md           # 本文档
```

## 🔧 开发工作流

### 1. 功能开发流程
```bash
# 1. 创建功能分支
git checkout -b feature/new-feature

# 2. 开发功能
# 编写代码...

# 3. 本地测试
mvn clean test
docker-compose up -d

# 4. 提交代码
git add .
git commit -m "feat: add new feature"
git push origin feature/new-feature

# 5. 创建Pull Request
# 在GitHub上创建PR到develop分支

# 6. 合并后自动部署
# CI/CD流水线自动运行
```

### 2. 环境说明
- **develop分支**: 开发环境，自动部署到K8s
- **main分支**: 生产环境，自动部署到K8s

## 🐛 故障排除

### 常见问题

#### 1. Docker Desktop启动失败
```powershell
# 重置Docker Desktop
# 右键Docker Desktop托盘图标 → Troubleshoot → Reset to factory defaults
```

#### 2. Kubernetes Pod启动失败
```powershell
# 查看Pod状态
kubectl get pods -n blm-microservices

# 查看Pod详细信息
kubectl describe pod <pod-name> -n blm-microservices

# 查看Pod日志
kubectl logs <pod-name> -n blm-microservices
```

#### 3. 服务无法访问
```powershell
# 检查服务状态
kubectl get svc -n blm-microservices

# 端口转发调试
kubectl port-forward svc/api-gateway-service 8080:8080 -n blm-microservices
```

#### 4. 镜像拉取失败
```powershell
# 检查镜像是否存在
docker images | grep blm

# 重新构建镜像
docker build -t blm/eureka-server ./eureka-server
```

### 日志查看
```powershell
# 查看特定服务日志
kubectl logs -f deployment/api-gateway -n blm-microservices

# 查看所有Pod日志
kubectl logs -f -l app=api-gateway -n blm-microservices

# 查看前50行日志
kubectl logs --tail=50 deployment/user-service -n blm-microservices
```

## 📊 监控和维护

### 1. 健康检查
```powershell
# 检查所有服务健康状态
kubectl get pods -n blm-microservices

# 查看服务详细状态
kubectl describe deployment api-gateway -n blm-microservices
```

### 2. 扩缩容
```powershell
# 扩展副本数
kubectl scale deployment user-service --replicas=3 -n blm-microservices

# 查看扩容状态
kubectl get deployment user-service -n blm-microservices
```

### 3. 滚动更新
```powershell
# 更新镜像
kubectl set image deployment/user-service user-service=ghcr.io/cheuring/blm/user-service:v2.0 -n blm-microservices

# 查看更新状态
kubectl rollout status deployment/user-service -n blm-microservices

# 回滚到上一版本
kubectl rollout undo deployment/user-service -n blm-microservices
```

## 🔐 安全配置

### 1. GitHub Token权限
确保GitHub Token具有以下权限：
- `repo`: 完整仓库访问
- `write:packages`: 推送到Container Registry

### 2. Kubernetes Secrets
```powershell
# 创建Docker Registry Secret
kubectl create secret docker-registry ghcr-secret \
  --docker-server=ghcr.io \
  --docker-username=<github-username> \
  --docker-password=<github-token> \
  -n blm-microservices
```

## 📝 最佳实践

### 1. 代码提交
- 使用语义化提交信息: `feat:`, `fix:`, `docs:`, `refactor:`
- 小而频繁的提交
- 提交前进行本地测试

### 2. 容器化
- 使用多阶段构建优化镜像大小
- 设置合适的资源限制
- 使用健康检查确保服务可用

### 3. Kubernetes
- 合理设置资源请求和限制
- 使用命名空间隔离环境
- 定期备份重要数据

## 🆘 获取帮助

如果遇到问题，可以：
1. 查看本文档的故障排除章节
2. 检查GitHub Actions的执行日志
3. 查看Kubernetes Pod和Service的状态
4. 检查应用程序日志

---

*最后更新: 2025年9月2日*
