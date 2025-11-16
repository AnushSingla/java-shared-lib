def call(String folderPath = '') {

    def packageFile = "${folderPath}/package.json"
    def pkgJSON = readJSON file: packageFile

    // Fix: Extract version as clean STRING, not JSON object
    def versionString = pkgJSON.version.toString().trim()

    echo "Current version string: ${versionString}"

    // Validate version format (X.Y.Z)
    if (!versionString.matches("\\d+\\.\\d+\\.\\d+")) {
        error "Invalid version format in package.json: ${versionString}"
    }

    // Split into parts
    def parts = versionString.split("\\.")
    def major = parts[0].toInteger()
    def minor = parts[1].toInteger()
    def patch = parts[2].toInteger()

    patch = patch + 1
    def newVersion = "${major}.${minor}.${patch}"

    echo "New version: ${newVersion}"

    // Write back updated version
    pkgJSON.version = newVersion
    writeJSON file: packageFile, json: pkgJSON, pretty: 4

    // Commit and push
    withCredentials([usernamePassword(
        credentialsId: 'github-jenkins',
        usernameVariable: 'GIT_USER',
        passwordVariable: 'GIT_PASS'
    )]) {
        sh """
            cd ${folderPath}

            git config user.email "jenkins@asha-saathi.com"
            git config user.name "Jenkins Bot"

            git pull origin main --rebase

            git add package.json
            git commit -m "CI: Version bump to ${newVersion}" || true
            git push origin HEAD:main
        """
    }

    return newVersion
}
