pipeline {
  agent any
 
  stages {
    stage("build") {
      steps {
        echo 'Building the project'
        sh 'mvn clean package -DskipTests'
      }
    }
  }
}
