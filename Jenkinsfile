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

        stage('SonarQube Analysis') {
            steps {
                // Utilisation de la credential correcte pour Sonar
                withCredentials([string(credentialsId: 'jenkins-sonar', variable: 'SONAR_TOKEN')]) {
                    withSonarQubeEnv('sonarqube') {
                        sh """
                        mvn sonar:sonar \
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
