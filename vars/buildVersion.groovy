def call(String folderPath = '') {

    // --- Start of new Git pull logic ---
    withCredentials([usernamePassword(...)]) {
        sh """
            cd ${folderPath}
            git config user.email "jenkins@asha-saathi.com"
            git config user.name "Jenkins Bot"
            git checkout main
            // 1. PULL LATEST CHANGES FIRST
            git pull --ff-only origin main || true 
        """
    }
    // --- End of new Git pull logic ---

    def packageFile = "${folderPath}/package.json"
    // 2. NOW read the file
    def pkgJSON = readJSON file: packageFile 

    // ... (rest of your version increment logic is fine) ...
    def versionString = pkgJSON.version.toString().trim()
    // ...
    def newVersion = "${major}.${minor}.${patch}"
    echo "New version: ${newVersion}"

    // 3. Write the new version back
    pkgJSON.version = newVersion
    writeJSON file: packageFile, json: pkgJSON, pretty: 4

    // 4. Commit and push (no need to pull again)
    withCredentials([usernamePassword(...)]) {
        sh """
            cd ${folderPath}
            // No need to pull again, just add, commit, push
            git add package.json
            git commit -m "CI: Version bump to ${newVersion}" || true
            git push https://${GIT_USER}:${GIT_PASS}@github.com/AnushSingla/Asha.git HEAD:main
        """
    }

    return newVersion
}