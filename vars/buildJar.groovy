#!/usr/bin/env groovy
def call() {
    echo "Building Application"

    sh "mvn build-helper:parse-version"

    def majorVersion = sh(script: "mvn help:evaluate -Dexpression=parsedVersion.majorVersion -q -DforceStdout", returnStdout: true).trim()
    def minorVersion = sh(script: "mvn help:evaluate -Dexpression=parsedVersion.minorVersion -q -DforceStdout", returnStdout: true).trim()
    def patchVersion = sh(script: "mvn help:evaluate -Dexpression=parsedVersion.nextIncrementalVersion -q -DforceStdout", returnStdout: true).trim()

    echo "Parsed Version: ${majorVersion}.${minorVersion}.${patchVersion}"

    def fullVersion = "${majorVersion}.${minorVersion}.${patchVersion}"
    echo "Bumping PATCH version to: ${fullVersion}"

    sh "mvn versions:set -DnewVersion=${fullVersion}"
    sh "mvn versions:commit"

    env.IMAGE_TAG = fullVersion

    sh "mvn package"
}
