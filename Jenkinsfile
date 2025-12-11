pipeline {
    agent any

    environment {
        SONARQUBE_NAME = 'SonarQube'
        SONAR_HOST_URL = 'http://localhost:9000'
        DOCKER_IMAGE = 'nadine2025/alpine'
        DOCKERHUB_CREDENTIALS = 'dockerhub-creds' // ID des credentials Jenkins pour Docker Hub
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
                    withSonarQubeEnv("${SONARQUBE_NAME}") {
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
                withCredentials([usernamePassword(credentialsId: "${DOCKERHUB_CREDENTIALS}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                        docker login -u $DOCKER_USER -p $DOCKER_PASS
                        docker build -t ${DOCKER_IMAGE}:latest .
                        docker push ${DOCKER_IMAGE}:latest
                    """
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline terminé avec succès !'
        }
        failure {
            echo 'Pipeline échoué !'
        }
    }
}
