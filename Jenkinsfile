pipeline {
    agent any

    tools {
        jdk 'JDK_21'
        maven 'Maven 3'
    }

    stages {
        stage('Clonar Repo') {
            steps {
                git branch: 'main', url: 'https://github.com/Sebasjc19/java-edu.git'
            }
        }

        stage('Compilar y Pruebas') {
            steps {
                sh 'mvn clean verify'
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Archivar Artefactos') {
            steps {
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }
    }

    post {
        always {
            echo 'Pipeline completado.'
        }
    }
}
