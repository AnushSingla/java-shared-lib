#!/usr/bin/env groovy
def call() {
    echo "Building Application"

    // Parse version and get major.minor.patch in one command
    def versionInfo = sh(script: """mvn build-helper:parse-version help:evaluate -Dexpression=parsedVersion.majorVersion -Dexpression2=parsedVersion.minorVersion -Dexpression3=parsedVersion.nextIncrementalVersion -q -DforceStdout""", returnStdout: true).trim()

    // Split the output into major, minor, patch
    def (majorVersion, minorVersion, patchVersion) = versionInfo.readLines()

    echo "Parsed Version: ${majorVersion}.${minorVersion}.${patchVersion}"

    def fullVersion = "${majorVersion}.${minorVersion}.${patchVersion}"
    echo "Bumping PATCH version to: ${fullVersion}"

    sh "mvn versions:set -DnewVersion=${fullVersion}"
    sh "mvn versions:commit"

    env.IMAGE_TAG = fullVersion

    sh "mvn package"
}
