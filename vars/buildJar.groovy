#!/usr/bin/env groovy
def call(){
    echo "Building Application"
    def version = sh(script :  "mvn help:evaluate -Dexpression=project.version -q -DforceStdout",returnStdout:true).trim()
    def buildnumber = env.BUILD_NUMBER
    def fullVersion = "${version}-${buildnumber}"
    sh "mvn build-helper:parse-version"
    sh "mvn versions:set -DnewVersion=${fullVersion}"
    sh "mvn versions:commit"
    env.IMAGE_TAG = fullVersion
    sh 'mvn package'
}