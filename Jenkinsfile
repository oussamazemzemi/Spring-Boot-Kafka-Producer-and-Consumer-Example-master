pipeline {
    agent any

    tools {
        // Remplacez 'maven-3' par le Name exact saisi dans Manage Jenkins > Tools
        maven 'maven-3'
    }

    environment {
        // Remplacez par la clé exacte de votre projet dans SonarQube
        SONAR_PROJECT_KEY = 'Spring-Boot-Kafka-Example'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                bat 'mvn clean verify'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube-server') {
                    script {
                        def prArgs = ""
                        if (env.CHANGE_ID) {
                            prArgs = "-Dsonar.pullrequest.key=${env.CHANGE_ID} -Dsonar.pullrequest.branch=${env.CHANGE_BRANCH} -Dsonar.pullrequest.base=${env.CHANGE_TARGET}"
                        }
                        bat "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=${SONAR_PROJECT_KEY} -Dsonar.projectVersion=${BUILD_NUMBER} ${prArgs}"
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}