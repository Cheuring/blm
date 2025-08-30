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
        stage('Build & Push Images to Single ACR Repo') {
            steps {
                script {
                    docker.withRegistry("https://${ACR_REGISTRY}", ACR_CREDENTIALS_ID) {
                        for (service in env.BACKEND_SERVICES.split()) {
                            
                            def imageName = "${ACR_REGISTRY}/${ACR_NAMESPACE}/${ACR_REPOSITORY}"
                            def versionTag = "${service}-${IMAGE_VERSION}"
                            def latestTag = "${service}-latest"
                            
                            dir(service) {
                                
                                // ===================== 【新增的关键步骤】 ===================== //
                                stage("Build JAR for ${service}") {
                                    echo "Building JAR package for ${service}..."
                                    // 运行 Maven 命令来编译和打包项目，跳过测试以加快速度
                                    // 这会在当前目录 (e.g., ./admin-service/)下生成 target/*.jar 文件
                                    sh "mvn clean package -DskipTests"
                                }
                                // ========================================================== //
                                
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