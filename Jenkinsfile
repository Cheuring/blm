pipeline {
    agent any

    environment {
        // --- 阿里云 ACR 相关配置 (无需修改) ---
        ACR_REGISTRY = "crpi-ak6ep24s2u70vrc7.cn-beijing.personal.cr.aliyuncs.com"
        ACR_NAMESPACE = "gabriel747"
        ACR_CREDENTIALS_ID = "aliyun-acr-credentials" // 确保 Jenkins 中存在此 ID 的凭证
        ACR_REPOSITORY = "blm-microservices"

        // --- 构建和部署相关配置 (请根据您的环境修改) ---
        IMAGE_VERSION = "v${env.BUILD_NUMBER}"
        BACKEND_SERVICES = "admin-service api-gateway auth-center order-service rider-service store-service user-service"

        // --- SSH 和 Kubernetes 部署配置 (请务必修改) ---
        K8S_SSH_CREDENTIAL_ID = "k8s-server-ssh" // Jenkins 中配置的 SSH 凭证 ID
        K8S_USER_HOST = "root@124.243.180.56" // K8s 服务器的 [用户]@[IP或域名]
        K8S_REMOTE_DIR = "root/blm-k8s"   // K8s 服务器上存放 YAML 的目录
        K8S_NAMESPACE = "default" // 您的应用部署在哪个命名空间
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
                sh "mvn clean package -DskipTests"
            }
        }

        stage('Build & Push Images to Single ACR Repo') {
            steps {
                script {
                    // 使用 withRegistry 保证安全登录 ACR
                    docker.withRegistry("https://${ACR_REGISTRY}", ACR_CREDENTIALS_ID) {
                        // 并行构建和推送，可以大大提高效率
                        def buildStages = [:]
                        for (service in env.BACKEND_SERVICES.split()) {
                            // 必须将 service 变量赋值给一个局部变量，才能在闭包中正确使用
                            def currentService = service
                            buildStages[currentService] = {
                                stage("Build & Push: ${currentService}") {
                                    // 镜像全名
                                    def imageName = "${ACR_REGISTRY}/${ACR_NAMESPACE}/${ACR_REPOSITORY}"
                                    // 版本标签，例如：admin-service-v123
                                    def versionTag = "${currentService}-${IMAGE_VERSION}"
                                    // latest 标签，例如：admin-service-latest
                                    def latestTag = "${currentService}-latest"
                                    
                                    // 进入对应服务的目录
                                    dir(currentService) {
                                        echo "Building image for ${currentService}..."
                                        def image = docker.build("${imageName}:${versionTag}", ".")
                                        
                                        echo "Pushing image ${imageName}:${versionTag}"
                                        image.push()

                                        echo "Tagging and pushing ${imageName}:${latestTag}"
                                        image.push(latestTag)
                                    }
                                }
                            }
                        }
                        // 启动并行执行
                        parallel buildStages
                    }
                }
            }
        }

        // ======================================================================
        // =====                  修改后的部署阶段 (重点)                    =====
        // ======================================================================
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // 使用 sshagent 插件，安全地加载 Jenkins 中存储的 SSH 凭证
                    sshagent(credentials: [K8S_SSH_CREDENTIAL_ID]) {
                        
                        // 1. 动态更新本地工作区中的 YAML 文件
                        echo "--> Step 1: Updating Kubernetes YAML files with new image tags..."
                        for (service in env.BACKEND_SERVICES.split()) {
                            def currentService = service
                            // 构造本次构建的镜像版本标签
                            def versionTag = "${currentService}-${IMAGE_VERSION}"
                            def fullImageName = "${ACR_REGISTRY}/${ACR_NAMESPACE}/${ACR_REPOSITORY}:${versionTag}"
                            
                            // 假设您的 YAML 文件命名规范为 k8s/<service-name>-deployment.yaml
                            def yamlFile = "k8s/${currentService}-deployment.yaml"

                            echo "Updating image in ${yamlFile} to ${fullImageName}"
                            
                            // 使用 sed 命令替换镜像地址。
                            // 使用 `|` 作为分隔符，避免与镜像 URL 中的 `/` 冲突。
                            // `g` 表示全局替换，以防万一。
                            sh "sed -i 's|image:.*${ACR_REPOSITORY}:${currentService}-.*|image: ${fullImageName}|g' ${yamlFile}"
                        }

                        // 2. 将更新后的 YAML 文件同步到 K8s 服务器
                        echo "--> Step 2: Syncing updated YAML files to Kubernetes server..."
                        // 确保远程目录存在
                        sh "ssh -o StrictHostKeyChecking=no ${K8S_USER_HOST} 'mkdir -p ${K8S_REMOTE_DIR}'"
                        // 使用 scp 递归复制 k8s 目录下的所有文件
                        sh "scp -r k8s/* ${K8S_USER_HOST}:${K8S_REMOTE_DIR}/"

                        // 3. 远程执行 kubectl apply
                        echo "--> Step 3: Applying manifests on Kubernetes server..."
                        sh "ssh ${K8S_USER_HOST} 'kubectl apply -f ${K8S_REMOTE_DIR}'"

                        // 4. 验证每个服务的部署状态
                        echo "--> Step 4: Verifying deployment status for all services..."
                        for (service in env.BACKEND_SERVICES.split()) {
                            def currentService = service
                            // 假设您的 deployment 名称与服务名一致，后面加上 '-deployment'
                            def deploymentName = "${currentService}-deployment"
                            echo "Checking rollout status for ${deploymentName}..."
                            sh "ssh ${K8S_USER_HOST} 'kubectl rollout status deployment/${deploymentName} --namespace ${K8S_NAMESPACE}'"
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            // 清理工作区，避免旧的构建产物影响下一次构建
            cleanWs()
        }
        success {
            echo 'Pipeline finished successfully. All services deployed.'
        }
        failure {
            echo 'Pipeline failed. Please check the logs.'
        }
    }
}