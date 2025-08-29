def call() {
    echo "Building Application"

    // Get current version
    def currentVersion = sh(script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
    echo "Current Version: ${currentVersion}"

    // Remove -SNAPSHOT and split by dots
    def cleanVersion = currentVersion.replace('-SNAPSHOT', '')
    def parts = cleanVersion.split('\\.')
    
    // Increment patch version (third part)
    def newPatch = (parts[2] as Integer) + 1
    def fullVersion = "${parts[0]}.${parts[1]}.${newPatch}"

    echo "Bumping to: ${fullVersion}"

    // Update version and build
    sh "mvn versions:set -DnewVersion=${fullVersion}"
    sh "mvn versions:commit"
    env.IMAGE_TAG = fullVersion
    sh "mvn package"
}