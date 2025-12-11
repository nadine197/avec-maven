pipeline {
    agent any

    tools {
        maven "MAVEN_HOME"
    }

    stages {

        stage('Clone du projet') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/nadine197/avec-maven.git'
            }
        }

        stage('Build Maven') {
            steps {
                sh "mvn clean install -DskipTests"
            }
        }

        stage('Analyse SonarQube') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=student-management \
                        -Dsonar.host.url=http://localhost:9000 \
                        -Dsonar.token=sqa_eb6f3d79d84bee1c3848909d581c5ec6e431b3e7
                    """
                }
            }
        }

        stage('Attendre Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t monapp ."
            }
        }
    }
}
