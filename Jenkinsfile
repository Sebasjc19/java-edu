pipeline {
    agent any

    environment {
        SONAR_TOKEN = credentials('sonar-token') // Asegúrate de que este ID existe en Jenkins
    }

    tools {
        jdk 'JDK_21'
        maven 'Maven 3'
    }

    stages {
        stage('Clonar Repo') {
            steps {
                // Si tu rama es "main", ajusta esto según corresponda
                git branch: 'main', url: 'https://github.com/Sebasjc19/java-edu.git'
                // Si es privado: añade credentialsId: 'mi-id-de-credenciales'
            }
        }

        stage('Compilar y Pruebas') {
            steps {
                sh 'mvn clean verify'
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Análisis de Calidad') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar -Dsonar.login=$SONAR_TOKEN'
                }
            }
        }

        // Agregar una etapa de "Archivar Artefactos"
        stage('Archivar Artefactos') {
            steps {
                // Este paso ahora está dentro de una etapa y contexto adecuado
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }
    }

    post {
        always {
            // Puedes incluir otros pasos que quieras ejecutar al finalizar el pipeline.
            echo 'Pipeline completado.'
        }
    }
}