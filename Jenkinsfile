pipeline {
    agent any

    tools {
        maven "M2_HOME" // Vérifie que ce nom correspond à ton installation Maven dans Jenkins
    }

    environment {
        SONARQUBE_NAME = 'sonarqube'
        SONAR_PROJECT_KEY = 'student-management'
        SONAR_HOST_URL = 'http://172.21.102.174:9000'
        DOCKER_IMAGE = "nadine2025/student-management:1.0"
    }

    stages {

        stage('Checkout SCM') {
            steps {
                git url: 'https://github.com/nadine197/avec-maven.git', branch: 'main'
            }
        }

        stage('Build & Test') {
            parallel {
                stage('Build') {
                    steps {
                        sh "mvn clean package -DskipTests"
                    }
                }
                stage('Test & Coverage') {
                    steps {
                        sh "mvn test jacoco:report"
                    }
                }
            }
        }

        stage('SonarQube Analysis') {
            environment {
                SONAR_TOKEN = credentials('jenkins-sonar')
            }
            steps {
                withSonarQubeEnv('sonarqube') {
                    // Utilisation sécurisée du token sans interpolation Groovy
                    sh "mvn sonar:sonar -Dsonar.projectKey=${SONAR_PROJECT_KEY} -Dsonar.login=$SONAR_TOKEN -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml"
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Docker Push') {
            when {
                branch 'main' // Pousse l'image seulement depuis la branche principale
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub',
                                                  usernameVariable: 'DOCKER_USER',
                                                  passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh "docker push ${DOCKER_IMAGE}"
                }
            }
        }
    }
    stage('Deploy to Kubernetes') {
    when {
        branch 'main'
    }
    steps {
        sh '''
        kubectl apply -f k8s/ -n devops
        kubectl rollout status deployment/student-management -n devops
        '''
    }
}


    post {
        always {
            cleanWs()
        }
        success {
            echo "Pipeline terminé avec succès ✅"
        }
        failure {
            echo "Pipeline échoué ❌"
        }
    }
}
