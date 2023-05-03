@Library('ggl-jenkins-sharedlibs') _

def namespace = "api"
def jobContext = null
def mvnCommand = "./mvnw"


def yaml_slave = def_slave.java('1.0', '12', '2', '4', '1G', '2G')
pipeline {
    agent {
        kubernetes {
            //name must be different from jenkins default or yaml value are not use
            label 'sharedlibs-java'
            yaml "${yaml_slave}"
        }
    }
    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '20'))
    }
    parameters {
        choice(
            name: 'ENVIRONMENT',
            choices: ['demo', 'pp'],
            description: 'Choose your environment'
        )
        booleanParam(name: "INTEGRATION_TESTS", defaultValue: true)
        booleanParam(name: "PERFTESTS", defaultValue: false)
        choice(
            name: 'SCENARIO_NAME',
            choices: ['com.ggl.bff_test.gatling.SequentielSimulation', 'com.ggl.bff_test.gatling.CartSimulation'],
            description: 'Choose your load test'
        )
       }
    stages {
        stage('Init') {
            steps {
                script {
                    jobContext = util.load("int");
                }
            }
        }
        stage('Build') {
            steps {
                container('java') {
                    sh """
                        $mvnCommand clean package -DskipTests
                      """
                }
            }
        }
        stage('Integration Tests') {
            when { expression { params.INTEGRATION_TESTS } }
            steps {
                container('java') {
                    sh """
                        echo "Do integration tests in ${params.ENVIRONMENT}"
                        $mvnCommand test -Pintegration-testing -Dkarate.env=${params.ENVIRONMENT}
                      """
                }
            }
        }
        stage('Performance Tests') {
            when { expression { params.PERFTESTS } }
            steps {
                container('java') {
                    sh """
                        echo "Do performance tests in ${params.ENVIRONMENT}"
                        export _JAVA_OPTIONS="-XX:+UnlockExperimentalVMOptions -Dsun.zip.disableMemoryMapping=true -XX:MinHeapFreeRatio=5 -XX:MaxHeapFreeRatio=10 -XX:GCTimeRatio=4 -XX:AdaptiveSizePolicyWeight=90 -Xms1000m -Xmx2000m"
                        $mvnCommand test -Pperformance-testing -Dkarate.env=${params.ENVIRONMENT}  -Dgatling.simulationClass=${params.SCENARIO_NAME}
                        cp -R ${env.WORKSPACE}/target/gatling/*simulation*/* ${env.WORKSPACE}/target/gatling
                      """
                }
            }
        }
    }
    post {
        always {
            script {
                // Publish all Html reports
                publishHTML([
                        allowMissing         : true,
                        alwaysLinkToLastBuild: true,
                        keepAll              : true,
                        reportDir            : "$WORKSPACE/target/cucumber-html-reports",
                        reportFiles          : '*.html',
                        reportName           : 'bff-test',
                        reportTitles         : 'bff test'
                ])
            }
            script {
                // Publish all Html reports
                publishHTML([
                        allowMissing         : true,
                        alwaysLinkToLastBuild: true,
                        keepAll              : true,
                        reportDir            : "$WORKSPACE/target/gatling",
                        reportFiles          : '*.html',
                        reportName           : 'bff-test-gatling',
                        reportTitles         : 'bff test-gatling'
                ])
            }
        }
    }
}
