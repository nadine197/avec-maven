pipeline {
    agent any

    tools {
        maven "M2_HOME"
    }

    environment {
        SONARQUBE_NAME = 'sonarqube'                       // Nom de l’instance SonarQube dans Jenkins
        SONAR_PROJECT_KEY = 'student-management'          // Clé du projet SonarQube
        SONAR_HOST_URL = 'http://172.21.102.174:9000'     // URL de ton serveur SonarQube
        DOCKER_IMAGE = "nadine2025/student-management:1.0" // Image Docker
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

        stage('Code Build & Coverage') {
            steps {
                // Compile, package et génère le rapport JaCoCo
                sh 'mvn clean package jacoco:report'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                // Injection du token SonarQube
                withCredentials([string(credentialsId: 'jenkins-sonar', variable: 'SONAR_TOKEN')]) {
                    // Utilisation de l’environnement SonarQube configuré dans Jenkins
                    withSonarQubeEnv("${SONARQUBE_NAME}") {
                        sh """
                        mvn sonar:sonar \
                          -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                          -Dsonar.login=${SONAR_TOKEN} \
                          -Dsonar.host.url=${SONAR_HOST_URL} \
                          -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                        """
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
                // Connexion DockerHub et push de l'image
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
