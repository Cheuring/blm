pipeline {
    agent any
    environment {
        DOCKERHUB_USERNAME = "deusex747"
        DOCKERHUB_CREDENTIALS_ID = "dockerhub-credentials"
        IMAGE_TAG = "v${env.BUILD_NUMBER}"
        BACKEND_SERVICES = "admin-service api-gateway auth-center order-service rider-service store-service user-service"
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build & Push Images') {
            steps {
                script {
                    docker.withRegistry("https://index.docker.io/v1/", DOCKERHUB_CREDENTIALS_ID) {
                        for (service in env.BACKEND_SERVICES.split()) {
                            def imageVer = "${DOCKERHUB_USERNAME}/blm-${service}:${IMAGE_TAG}"
                            def imageLatest = "${DOCKERHUB_USERNAME}/blm-${service}:latest"
                            dir(service) {
                                def image = docker.build(imageVer, ".")
                                image.push()
                                // 推送 latest tag
                                sh "docker tag ${imageVer} ${imageLatest}"
                                sh "docker push ${imageLatest}"
                            }
                        }
                    }
                }
            }
        }
    }
    post {
        success {
            echo '所有微服务镜像已构建并推送到Docker Hub（含唯一版本号tag和latest tag）。'
        }
        failure {
            echo '流水线失败，请检查日志。'
        }
    }
}
