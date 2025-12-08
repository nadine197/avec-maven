pipeline {
  agent any

  stages {
    stage('Build') {
      steps {
        sh 'mvn clean install'
      }
    }

    stage('SonarQube') {
      steps {
        sh 'mvn sonar:sonar'
      }
    }

    stage('Docker') {
      steps {
        sh 'docker build -t monapp .'
      }
    }
  }
}
