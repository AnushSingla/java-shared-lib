def call() {
    echo "Building Application"

    // First get the current version directly from pom.xml
    def currentVersion = sh(script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
    echo "Current Version: ${currentVersion}"

    // Parse the version manually (more reliable)
    def versionPattern = ~/(\d+)\.(\d+)\.(\d+)(-SNAPSHOT)?/
    def matcher = versionPattern.matcher(currentVersion)
    
    if (matcher.find()) {
        def majorVersion = matcher.group(1)
        def minorVersion = matcher.group(2) 
        def currentPatchVersion = matcher.group(3)
        
        // Increment patch version
        def newPatchVersion = (currentPatchVersion as Integer) + 1
        def fullVersion = "${majorVersion}.${minorVersion}.${newPatchVersion}"
        
        echo "Parsed Version: ${majorVersion}.${minorVersion}.${currentPatchVersion}"
        echo "Bumping PATCH version to: ${fullVersion}"

        // Set new version
        sh "mvn versions:set -DnewVersion=${fullVersion}"
        sh "mvn versions:commit"

        env.IMAGE_TAG = fullVersion
        sh "mvn package"
    } else {
        error "Failed to parse version from: ${currentVersion}. Expected format: x.y.z or x.y.z-SNAPSHOT"
    }
}