pipeline {
    agent any

    tools {
        // Nom exact configuré dans Manage Jenkins > Tools > SonarQube Scanner
        scanner 'sonar-scanner'
    }

    environment {
        // Remplacez par la clé de votre projet dans SonarQube
        SONAR_PROJECT_KEY = 'mon-projet-cle'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                // Adaptez cette commande à votre projet (ex: ./mvnw test, npm test, pytest...)
                sh 'echo "Compilation et tests unitaires..."'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube-server') {
                    script {
                        def prOptions = ""
                        if (env.CHANGE_ID) {
                            prOptions = """
                                -Dsonar.pullrequest.key=${env.CHANGE_ID} \
                                -Dsonar.pullrequest.branch=${env.CHANGE_BRANCH} \
                                -Dsonar.pullrequest.base=${env.CHANGE_TARGET}
                            """
                        }
                        sh """
                            ${tool('scanner')}/bin/sonar-scanner \
                              -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                              -Dsonar.projectVersion=${BUILD_NUMBER} \
                              ${prOptions}
                        """
                    }
                }
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Checkmarx Scan') {
            steps {
                // Remplacez ce bloc par la syntaxe générée dans Jenkins :
                // Ouvrez un job Pipeline > Pipeline Syntax > choisissez l'étape Checkmarx,
                // configurez votre projet/seuils, cliquez sur "Generate Pipeline Script" et collez ici.
                sh 'echo "Exécution du scan Checkmarx (coller le bloc généré via Pipeline Syntax)"'
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}