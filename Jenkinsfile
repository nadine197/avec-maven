pipeline {
    agent any

    tools {
        maven "M2_HOME"
    }

    environment {
        SONARQUBE_NAME = 'sonarqube'                        // Nom de l’instance SonarQube dans Jenkins
        SONAR_PROJECT_KEY = 'student-management'           // Clé du projet SonarQube
        SONAR_HOST_URL = 'http://172.21.102.174:9000'      // URL du serveur SonarQube
        DOCKER_IMAGE = "nadine2025/student-management:1.0" // Image Docker
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/nadine197/avec-maven.git', branch: 'main'
            }
        }

        stage('Build & Test with Coverage') {
            steps {
                sh 'mvn clean verify jacoco:report'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'jenkins-sonar', variable: 'SONAR_TOKEN')]) {
                    withSonarQubeEnv("${SONARQUBE_NAME}") {
                        sh """
                            mvn sonar:sonar \
                              -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                              -Dsonar.login=\$SONAR_TOKEN \
                              -Dsonar.host.url=${SONAR_HOST_URL} \
                              -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                        """
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE} ."
                withCredentials([usernamePassword(credentialsId: 'dockerhub',
                                                  usernameVariable: 'DOCKER_USER',
                                                  passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh "docker push ${DOCKER_IMAGE}"
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline terminé avec succès ✅'
        }
        failure {
            echo 'Pipeline échoué ❌'
        }
    }
}
