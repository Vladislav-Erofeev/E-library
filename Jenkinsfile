pipeline {
  agent any
 
  stages {
    stage("build") {
      steps {
        echo 'Building the project'
        sh '.\mvnw clean package -DskipTests'
      }
    }
  }
}
