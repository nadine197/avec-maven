pipeline {
    agent any

    environment {
        // Nom de la configuration SonarQube dans Jenkins
        SONARQUBE_NAME = 'sonarqube'
        // Clé du projet Sonar
        SONAR_PROJECT_KEY = 'student-management'
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/nadine197/avec-maven.git', branch: 'main'
            }
        }

        stage('Build Maven') {
            steps {
                // Compile et exécute les tests pour générer le coverage
                sh 'mvn clean install'
            }
        }
        pipeline {
    agent any

    environment {
        SONARQUBE_NAME = 'sonarqube'
        SONAR_PROJECT_KEY = 'student-management'
    }

    stages {
        stage('Build & SonarQube Analysis') {
            steps {
                git url: 'https://github.com/nadine197/avec-maven.git', branch: 'main'

                // Utilisation de la credential pour SonarQube
                withCredentials([string(credentialsId: 'jenkins-sonar', variable: 'SONAR_TOKEN')]) {
                    withSonarQubeEnv('sonarqube') {
                        sh """
                        mvn clean install sonar:sonar \
                          -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                          -Dsonar.host.url=${env.SONAR_HOST_URL} \
                          -Dsonar.login=${SONAR_TOKEN}
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

        stage('Quality Gate') {
            steps {
                // Attend la fin de l'analyse Sonar et récupère le statut du Quality Gate
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
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
