def call(String folderPath = '') {
    def packageFile = "${folderPath}/package.json"
    def pkgJSON = readJSON file: packageFile

    // Convert JSON value into real string
    def versionString = pkgJSON.version.toString()
    echo "Current version: ${versionString}"

    // Split version correctly
    def parts = versionString.split("\\.")
    def major = parts[0] as int
    def minor = parts[1] as int
    def patch = parts[2] as int

    patch = patch + 1
    def newversion = "${major}.${minor}.${patch}"
    echo "New version: ${newversion}"

    // Write updated version back
    pkgJSON.version = newversion
    writeJSON file: packageFile, json: pkgJSON, pretty: 4

    // Commit & push changes
    withCredentials([usernamePassword(
        credentialsId: 'github-jenkins',
        usernameVariable: 'GIT_USER',
        passwordVariable: 'GIT_PASS'
    )]) {
        sh """
            cd ${folderPath}
            git config user.email "jenkins@asha-saathi.com"
            git config user.name "Jenkins Bot Asha-Saathi"

            git remote set-url origin https://${GIT_USER}:${GIT_PASS}@github.com/AnushSingla/Asha.git
             git pull origin main --rebase 
            git add package.json
            git commit -m "CI: Version bump to ${newversion}" || true
            git push origin HEAD:main
        """
    }

    return newversion
}
