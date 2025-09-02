#!/bin/bash

# 启用Kubernetes
echo "启用Docker Desktop中的Kubernetes..."
echo "请确保已在Docker Desktop中启用Kubernetes支持"
echo ""

# 检查kubectl是否安装
if ! command -v kubectl &> /dev/null; then
    echo "错误: kubectl未安装"
    echo "请先安装kubectl: https://kubernetes.io/docs/tasks/tools/"
    exit 1
fi

# 检查Docker Desktop是否运行
if ! docker info &> /dev/null; then
    echo "错误: Docker Desktop未运行"
    echo "请先启动Docker Desktop"
    exit 1
fi

# 检查Kubernetes是否可用
if ! kubectl cluster-info &> /dev/null; then
    echo "错误: Kubernetes未启用或未连接"
    echo "请在Docker Desktop中启用Kubernetes"
    exit 1
fi

echo "✅ 环境检查通过"
echo ""

# 创建命名空间
echo "创建Kubernetes命名空间..."
kubectl apply -f k8s/namespace.yaml

# 部署基础设施 (MySQL, Redis)
echo "部署基础设施服务..."
kubectl apply -f k8s/mysql.yaml
kubectl apply -f k8s/mysql-deployment.yaml
kubectl apply -f k8s/redis-deployment.yaml

echo "等待基础设施服务启动..."
kubectl wait --for=condition=ready pod -l app=mysql -n blm-microservices --timeout=300s
kubectl wait --for=condition=ready pod -l app=redis -n blm-microservices --timeout=300s

# 部署Eureka注册中心
echo "部署Eureka注册中心..."
kubectl apply -f k8s/eureka-server.yaml
kubectl wait --for=condition=ready pod -l app=eureka-server -n blm-microservices --timeout=300s

# 部署配置中心
echo "部署配置中心..."
kubectl apply -f k8s/config-server.yaml
kubectl wait --for=condition=ready pod -l app=config-server -n blm-microservices --timeout=300s

# 部署微服务
echo "部署微服务..."
kubectl apply -f k8s/auth-center.yaml
kubectl apply -f k8s/user-service.yaml
kubectl apply -f k8s/order-service.yaml
kubectl apply -f k8s/store-service.yaml
kubectl apply -f k8s/rider-service.yaml
kubectl apply -f k8s/admin-service.yaml
kubectl apply -f k8s/file-service.yaml

echo "等待微服务启动..."
sleep 30

# 部署API网关
echo "部署API网关..."
kubectl apply -f k8s/api-gateway.yaml
kubectl wait --for=condition=ready pod -l app=api-gateway -n blm-microservices --timeout=300s

echo ""
echo "✅ 部署完成!"
echo ""
echo "查看部署状态:"
kubectl get pods -n blm-microservices
echo ""
echo "访问应用:"
echo "API网关: http://localhost:30080"
echo "Eureka: kubectl port-forward svc/eureka-service 8761:8761 -n blm-microservices"
echo ""
echo "查看日志:"
echo "kubectl logs -f deployment/api-gateway -n blm-microservices"
