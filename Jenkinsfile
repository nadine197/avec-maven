pipeline {
    agent any

    environment {
        SONAR_HOST_URL = 'http://localhost:9000'
    }

    stages {
        stage('Cloner le projet') {
            steps {
                git 'https://github.com/nadine197/avec-maven.git'
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
                    withSonarQubeEnv('SonarQube') {
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

        stage('Docker Build & Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                        docker login -u $DOCKER_USER -p $DOCKER_PASS
                        docker build -t nadine2025/alpine:latest .
                        docker push nadine2025/alpine:latest
                    """
                }
            }
        }
    }

    post {
        success { echo 'Pipeline terminé avec succès !' }
        failure { echo 'Pipeline échoué !' }
    }
}
