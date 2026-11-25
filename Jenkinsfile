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
                dependencyCheck(
                    odcInstallation: 'dependency-check',
                    nvdCredentialsId: 'nvd-api-key',
                    additionalArguments: '--scan . --format XML --format HTML --out target/odc-report'
                )
                dependencyCheckPublisher(
                    pattern: 'target/odc-report/dependency-check-report.xml',
                    failedTotalCritical: 1,
                    failedTotalHigh: 1,
                    stopBuild: true
                )
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