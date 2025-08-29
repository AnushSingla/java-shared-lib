def call() {
    echo "Building Application"

    // First parse the version using build-helper plugin
    sh "mvn build-helper:parse-version"

    // Get the version components
    def majorVersion = sh(script: "mvn help:evaluate -Dexpression=parsedVersion.majorVersion -q -DforceStdout", returnStdout: true).trim()
    def minorVersion = sh(script: "mvn help:evaluate -Dexpression=parsedVersion.minorVersion -q -DforceStdout", returnStdout: true).trim()
    def currentPatchVersion = sh(script: "mvn help:evaluate -Dexpression=parsedVersion.incrementalVersion -q -DforceStdout", returnStdout: true).trim()

    echo "Current Version: ${majorVersion}.${minorVersion}.${currentPatchVersion}"

    // Increment the patch version manually
    def newPatchVersion = (currentPatchVersion as Integer) + 1
    def fullVersion = "${majorVersion}.${minorVersion}.${newPatchVersion}"

    echo "Bumping PATCH version to: ${fullVersion}"

    // Set new version
    sh "mvn versions:set -DnewVersion=${fullVersion}"
    sh "mvn versions:commit"

    env.IMAGE_TAG = fullVersion

    sh "mvn package"
}