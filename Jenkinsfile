pipeline {
    agent any
    environment {
        // 阿里云 ACR 相关配置
        ACR_REGISTRY = "crpi-ak6ep24s2u70vrc7.cn-beijing.personal.cr.aliyuncs.com"
        ACR_NAMESPACE = "gabriel747"
        ACR_CREDENTIALS_ID = "aliyun-acr-credentials"
        ACR_REPOSITORY = "blm-microservices"

        IMAGE_VERSION = "v${env.BUILD_NUMBER}"
        BACKEND_SERVICES = "admin-service api-gateway auth-center order-service rider-service store-service user-service"
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build All Modules') {
            steps {
                echo "Building all microservice JAR packages at once..."
                // 在项目根目录执行 Maven 命令
                // Maven 会自动处理模块间的依赖顺序
                sh "mvn clean package -DskipTests"
            }
        }

        stage('Build & Push Images to Single ACR Repo') {
            steps {
                script {
                    docker.withRegistry("https://${ACR_REGISTRY}", ACR_CREDENTIALS_ID) {
                        for (service in env.BACKEND_SERVICES.split()) {
                            
                            def imageName = "${ACR_REGISTRY}/${ACR_NAMESPACE}/${ACR_REPOSITORY}"
                            def versionTag = "${service}-${IMAGE_VERSION}"
                            def latestTag = "${service}-latest"
                            
                            dir(service) {
                                
                                stage("Build and Push Image for ${service}") {
                                    echo "Building Docker image for ${service}..."
                                    // 使用 docker.build 构建镜像，并直接打上版本标签
                                    def image = docker.build("${imageName}:${versionTag}", ".")
                                    
                                    echo "Pushing Docker image for ${service}..."
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