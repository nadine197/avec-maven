pipeline {
  agent any

  stages {
  stage('Build') {
      steps {
          sh(["mvn", "clean", "install"])
      }
  }

  stage('Analyse SonarQube') {
      steps {
          withSonarQubeEnv('SonarQube') {
              sh([
                  "mvn",
                  "sonar:sonar",
                  "-Dsonar.projectKey=student-management",
                  "-Dsonar.host.url=http://localhost:9000",
                  "-Dsonar.login=TON_TOKEN"
              ])
          }
      }
  }

    stage('Docker') {
      steps {
        sh 'docker build -t monapp .'
      }
    }
  }
}
