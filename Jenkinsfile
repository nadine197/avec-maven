pipeline {
    agent any

    environment {
        SONAR_HOST_URL = 'http://localhost:9000'
        DOCKER_IMAGE = 'nadine2025/alpine:latest'
    }

    stages {

        stage('Cloner le projet') {
            steps {
                git branch: 'main', url: 'https://github.com/nadine197/avec-maven.git'
            }
        }

        stage('Build Maven') {
            steps {
                sh "mvn clean install -DskipTests"
            }
        }

        stage('Analyse SonarQube') {
            steps {
                withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                    withSonarQubeEnv('sonarqube') {
                        sh "mvn sonar:sonar -Dsonar.projectKey=student-management -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.login=$SONAR_TOKEN"
                    }
                }
            }
        }

        stage('Attendre Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build & Push Docker') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                        docker login -u $DOCKER_USER -p $DOCKER_PASS
                        docker build -t ${DOCKER_IMAGE} .
                        docker push ${DOCKER_IMAGE}
                    """
                }
            }
        }
    }

    post {
        success { echo 'Pipeline terminée avec succès ! ✅' }
        failure { echo 'Pipeline échouée ! ❌' }
    }
}
