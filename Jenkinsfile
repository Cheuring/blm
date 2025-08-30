pipeline {
    agent any
    environment {
        // 阿里云 ACR 相关配置
        ACR_REGISTRY = "registry.cn-hangzhou.aliyuncs.com" // 请在ACR控制台确认您的实例地址
        ACR_NAMESPACE = "gabriel747" // 您创建的命名空间
        ACR_CREDENTIALS_ID = "aliyun-acr-credentials" // 您在 Jenkins 中为ACR创建的凭据 ID

        // 【修改点 1】定义统一的仓库名称
        ACR_REPOSITORY = "blm-microservices" // <--- 请确保这个名字和您在ACR上创建的仓库名一致

        IMAGE_VERSION = "v${env.BUILD_NUMBER}"
        BACKEND_SERVICES = "admin-service api-gateway auth-center order-service rider-service store-service user-service"
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build & Push Images to Single ACR Repo') {
            steps {
                script {
                    // 登录到阿里云 ACR
                    docker.withRegistry("https://${ACR_REGISTRY}", ACR_CREDENTIALS_ID) {
                        for (service in env.BACKEND_SERVICES.split()) {
                            
                            // 【修改点 2】构建新的镜像全名和标签
                            // 完整的镜像名，不含标签，例如: registry.cn-hangzhou.aliyuncs.com/gabriel747/blm-microservices
                            def imageName = "${ACR_REGISTRY}/${ACR_NAMESPACE}/${ACR_REPOSITORY}"
                            // 版本标签，包含微服务名，例如: admin-service-v123
                            def versionTag = "${service}-${IMAGE_VERSION}"
                            // latest标签，包含微服务名，例如: admin-service-latest
                            def latestTag = "${service}-latest"
                            
                            dir(service) {
                                // 使用 docker.build 构建镜像，并直接打上版本标签
                                def image = docker.build("${imageName}:${versionTag}", ".")
                                
                                // 推送这个带版本号的镜像
                                image.push()

                                // 为这个镜像打上 latest 标签，并推送
                                image.push(latestTag)
                            }
                        }
                    }
                }
            }
        }
    }
    post {
        success {
            echo '所有微服务镜像已构建并推送到阿里云 ACR 的统一仓库中。'
        }
        failure {
            echo '流水线失败，请检查日志。'
        }
    }
}