pipeline {
  agent any

  parameters {
    choice(name: 'TEST_SUITE', choices: ['all', 'ui', 'api', 'hybrid'], description: 'Select test suite to run')
    choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Browser for UI tests')
    booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browser in headless mode')
    string(name: 'ENV', defaultValue: 'staging', description: 'Target environment')
  }

  tools {
    maven 'Maven-3.9'
    jdk 'JDK-17'
  }

  environment {
    REPORT_DIR = 'target/extent-reports'
    SCREENSHOT_DIR = 'target/screenshots'
    TIMESTAMP = sh(script: 'date +%Y%m%d_%H%M%S', returnStdout: true).trim()
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
        echo "Branch: ${env.BRANCH_NAME}"
        echo "Build: ${env.BUILD_NUMBER}"
      }
    }

    stage('Build') {
      steps {
        sh 'mvn clean compile -q'
      }
    }

    stage('Run Tests') {
      steps {
        script {
          def suite = params.TEST_SUITE == 'all' ? 'testng-all.xml' : "testng-${params.TEST_SUITE}.xml"
          sh """
            mvn test \\
              -Dsurefire.suiteXmlFiles=src/test/resources/${suite} \\
              -Dbrowser=${params.BROWSER} \\
              -Dheadless=${params.HEADLESS} \\
              -Denv=${params.ENV} \\
              -Dreport.dir=${REPORT_DIR} \\
              -Dscreenshot.dir=${SCREENSHOT_DIR} \\
              -Dbuild.number=${env.BUILD_NUMBER}
          """
        }
      }
    }
  }

  post {
    always {
      publishHTML(target: [
        allowMissing: true,
        alwaysLinkToLastBuild: true,
        keepAll: true,
        reportDir: "${REPORT_DIR}",
        reportFiles: 'ExtentReport.html',
        reportName: "Extent Report - Build ${env.BUILD_NUMBER}"
      ])
      archiveArtifacts artifacts: 'target/screenshots/**/*.png', allowEmptyArchive: true
      archiveArtifacts artifacts: 'target/surefire-reports/**', allowEmptyArchive: true
      junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
    }
    success {
      echo 'All tests passed!'
    }
    failure {
      echo 'Tests failed - check Extent Report and screenshots'
    }
    unstable {
      echo 'Some tests failed - partial pass'
    }
  }
}
