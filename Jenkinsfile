pipeline {
    agent any

    tools {
        maven 'maven-3'
    }

    environment {
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

        stage('OWASP Dependency-Check') {
            steps {
                withCredentials([string(credentialsId: 'nvd-api-key', variable: 'NVD_API_KEY')]) {
                    script {
                        def cleanKey = env.NVD_API_KEY ? env.NVD_API_KEY.trim() : ''
                        if (!cleanKey) {
                            error("Le credential 'nvd-api-key' est vide.")
                        }
                        bat "mvn org.owasp:dependency-check-maven:13.0.0:check -DnvdApiKey=${cleanKey} -DfailBuildOnCVSS=8"
                    }
                }
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
            archiveArtifacts artifacts: 'target/dependency-check-report.*', allowEmptyArchive: true
            cleanWs()
        }
    }
}