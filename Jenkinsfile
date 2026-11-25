pipeline {
    agent any

    environment {
        // Clé de votre projet dans SonarQube
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
                // Utilisez ./mvnw si le Maven Wrapper est présent dans le dépôt, sinon mvn
                sh 'mvn clean verify'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube-server') {
                    script {
                        def prArgs = ""
                        if (env.CHANGE_ID) {
                            prArgs = """
                                -Dsonar.pullrequest.key=${env.CHANGE_ID} \
                                -Dsonar.pullrequest.branch=${env.CHANGE_BRANCH} \
                                -Dsonar.pullrequest.base=${env.CHANGE_TARGET}
                            """
                        }
                        sh """
                            mvn sonar:sonar \
                              -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                              -Dsonar.projectVersion=${BUILD_NUMBER} \
                              ${prArgs}
                        """
                    }
                }
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        //stage('Checkmarx Scan') {
          //  steps {
                // Remplacez ce bloc par la syntaxe générée via "Pipeline Syntax" dans Jenkins
            //    sh 'echo "Exécution du scan Checkmarx (coller le bloc généré via Pipeline Syntax)"'
            //}
        //}
    }

    post {
        always {
            cleanWs()
        }
    }
}