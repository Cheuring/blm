# GitHub Actions + Docker Desktop + Kubernetes 配置步骤

本文档详细说明了如何配置整个CI/CD环境。

## 📋 配置清单

### ✅ 1. Docker Desktop 配置

#### 安装和启用Kubernetes
1. **下载安装 Docker Desktop**
   - 访问: https://www.docker.com/products/docker-desktop/
   - 下载Windows版本并安装

2. **启用Kubernetes**
   ```
   Docker Desktop → Settings → Kubernetes → Enable Kubernetes → Apply & Restart
   ```

3. **验证安装**
   ```powershell
   docker --version
   kubectl version --client
   kubectl cluster-info
   ```

#### 资源配置建议
- **内存**: 至少 8GB
- **CPU**: 至少 4核
- **磁盘**: 至少 50GB 可用空间

### ✅ 2. GitHub Repository 配置

#### 创建GitHub Token
1. 访问 GitHub → Settings → Developer settings → Personal access tokens
2. 创建新token，权限选择:
   - `repo` (完整仓库访问权限)
   - `write:packages` (推送到Container Registry)
   - `read:packages` (从Container Registry拉取)

#### 配置Repository Secrets
在GitHub仓库中添加以下Secrets:

1. **KUBE_CONFIG** (Kubernetes配置)
   ```powershell
   # 获取kubectl配置并编码
   $config = Get-Content $env:USERPROFILE\.kube\config -Raw
   $bytes = [System.Text.Encoding]::UTF8.GetBytes($config)
   $encoded = [Convert]::ToBase64String($bytes)
   Write-Host $encoded
   ```
   将输出的base64字符串添加为 `KUBE_CONFIG` secret

### ✅ 3. Kubernetes 本地配置

#### 获取kubectl配置
```powershell
# 查看当前配置
kubectl config view

# 查看配置文件位置
kubectl config view --raw > kube-config.yaml
```

#### 测试集群连接
```powershell
kubectl cluster-info
kubectl get nodes
kubectl get namespaces
```

### ✅ 4. 项目配置

#### Maven配置
确保每个微服务的 `pom.xml` 包含必要的依赖和插件:

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <excludes>
            <exclude>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
            </exclude>
        </excludes>
    </configuration>
</plugin>
```

#### 应用配置文件
每个服务需要三套配置:
- `application.yml` - 默认配置
- `application-docker.yml` - Docker环境配置  
- `application-k8s.yml` - Kubernetes环境配置

### ✅ 5. 环境变量配置

#### Docker Compose环境变量
```yaml
environment:
  - SPRING_PROFILES_ACTIVE=docker
  - EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://eureka-server:8761/eureka/
  - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/microservices
```

#### Kubernetes环境变量
```yaml
env:
- name: SPRING_PROFILES_ACTIVE
  value: "k8s"
- name: EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE
  value: "http://eureka-service:8761/eureka/"
```

## 🚀 首次部署步骤

### 1. 准备工作
```powershell
# 克隆项目
git clone https://github.com/Cheuring/blm.git
cd blm

# 切换到正确分支
git checkout micro_k8s
```

### 2. 本地测试
```powershell
# 构建项目
mvn clean package -DskipTests

# 构建镜像
./build-images.ps1

# 测试Docker Compose
docker-compose up -d
docker-compose ps
docker-compose logs api-gateway

# 清理
docker-compose down
```

### 3. Kubernetes部署
```powershell
# 部署到K8s
./deploy-k8s.ps1

# 检查状态
kubectl get pods -n blm-microservices
kubectl get svc -n blm-microservices

# 访问应用
kubectl port-forward svc/api-gateway-service 8080:8080 -n blm-microservices
```

### 4. 配置GitHub Actions
```powershell
# 推送代码触发CI/CD
git add .
git commit -m "feat: add ci/cd configuration"
git push origin micro_k8s
```

## 🔧 高级配置

### 1. 自定义域名 (可选)
```powershell
# 编辑hosts文件 (需要管理员权限)
Add-Content -Path C:\Windows\System32\drivers\etc\hosts -Value "127.0.0.1 blm.local"
```

### 2. 启用Ingress (可选)
```powershell
# 安装NGINX Ingress Controller
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.2/deploy/static/provider/cloud/deploy.yaml

# 等待启动
kubectl wait --namespace ingress-nginx --for=condition=ready pod --selector=app.kubernetes.io/component=controller --timeout=90s
```

### 3. 监控配置 (可选)
```powershell
# 安装Prometheus和Grafana
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update
helm install monitoring prometheus-community/kube-prometheus-stack -n monitoring --create-namespace
```

## 🐛 常见问题解决

### 问题1: Docker Desktop Kubernetes无法启动
**解决方案:**
```powershell
# 重置Kubernetes
# Docker Desktop → Settings → Kubernetes → Reset Kubernetes Cluster

# 或者重置整个Docker Desktop
# Docker Desktop → Troubleshoot → Reset to factory defaults
```

### 问题2: kubectl命令不可用
**解决方案:**
```powershell
# 检查PATH环境变量
$env:PATH -split ';' | Select-String kubectl

# 手动添加kubectl到PATH (如果需要)
# 通常Docker Desktop会自动配置
```

### 问题3: GitHub Actions权限错误
**解决方案:**
1. 检查GitHub Token权限
2. 确认Repository设置中的Actions权限
3. 验证Container Registry权限

### 问题4: Pod启动失败
**解决方案:**
```powershell
# 查看详细错误信息
kubectl describe pod <pod-name> -n blm-microservices

# 查看应用日志
kubectl logs <pod-name> -n blm-microservices

# 检查镜像是否存在
docker images | grep blm
```

## 📊 验证部署成功

### 1. 检查所有组件状态
```powershell
# 检查Pod状态
kubectl get pods -n blm-microservices

# 检查Service状态  
kubectl get svc -n blm-microservices

# 检查Deployment状态
kubectl get deployments -n blm-microservices
```

### 2. 功能测试
```powershell
# 测试API网关
curl http://localhost:30080/actuator/health

# 测试Eureka
kubectl port-forward svc/eureka-service 8761:8761 -n blm-microservices
# 访问 http://localhost:8761
```

### 3. CI/CD流水线测试
1. 修改代码并提交到 `micro_k8s` 分支
2. 检查GitHub Actions执行状态
3. 验证新版本是否部署成功

## 📝 维护检查清单

### 每日检查
- [ ] 检查所有Pod运行状态
- [ ] 查看应用日志是否有错误
- [ ] 验证API网关可访问性

### 每周检查  
- [ ] 清理未使用的Docker镜像
- [ ] 检查磁盘空间使用情况
- [ ] 更新依赖版本

### 每月检查
- [ ] 备份Kubernetes配置
- [ ] 检查安全更新
- [ ] 性能监控分析

---

*配置完成后，您就拥有了一个完整的CI/CD流水线！* 🎉
