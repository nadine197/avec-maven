pipeline {
    agent any

    tools {
        maven "M2_HOME"
    }

    environment {
        SONARQUBE_NAME = 'sonarqube'
        SONAR_PROJECT_KEY = 'student-management'
        SONAR_HOST_URL = 'http://172.21.102.174:9000'
        DOCKER_IMAGE = "nadine2025/student-management:1.0"
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/nadine197/avec-maven.git', branch: 'main'
            }
        }

        stage('Code Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Code Build') {
    steps {
        sh 'mvn package jacoco:report'
    }
}
        stage('SonarQube Analysis') {
    steps {
        withCredentials([string(credentialsId: 'jenkins-sonar', variable: 'SONAR_TOKEN')]) {
            withSonarQubeEnv("${SONARQUBE_NAME}") {
                sh '''
                mvn sonar:sonar \
                  -Dsonar.projectKey=student-management \
                  -Dsonar.login=${SONAR_TOKEN}
                  -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                '''
            }
        }
    }
}

        stage('Docker Build') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', 
                                                  usernameVariable: 'DOCKER_USER', 
                                                  passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh "docker push ${DOCKER_IMAGE}"
                }
            }
        }
    } // <-- Fin du bloc stages

    post {
        success {
            echo 'Pipeline terminé avec succès ✅'
        }
        failure {
            echo 'Pipeline échoué ❌'
        }
    }
}
