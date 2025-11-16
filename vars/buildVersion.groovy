def call(String folderPath = '') {

 
    withCredentials([usernamePassword(
        credentialsId: 'github-jenkins',
        usernameVariable: 'GIT_USER',
        passwordVariable: 'GIT_PASS'
    )]) {
        sh """
            cd ${folderPath}
            git config user.email "jenkins@asha-saathi.com"
            git config user.name "Jenkins Bot"
            git checkout main
            
           
            git pull --ff-only origin main || true 
        """
    }
   

    def packageFile = "${folderPath}/package.json"
    
    
    def pkgJSON = readJSON file: packageFile 

    
    def versionString = pkgJSON.version.toString().trim()
    echo "Current version string: ${versionString}"

   
    if (!versionString.matches("\\d+\\.\\d+\\.\\d+")) {
        error "Invalid version format in package.json: ${versionString}"
    }

    
    def parts = versionString.split("\\.")
    def major = parts[0].toInteger()
    def minor = parts[1].toInteger()
    def patch = parts[2].toInteger()

    // Increment patch
    patch = patch + 1
    def newVersion = "${major}.${minor}.${patch}"
    echo "New version: ${newVersion}"

    // 3. Update package.json
    pkgJSON.version = newVersion
    writeJSON file: packageFile, json: pkgJSON, pretty: 4 

   
    withCredentials([usernamePassword(
        credentialsId: 'github-jenkins',
        usernameVariable: 'GIT_USER',
        passwordVariable: 'GIT_PASS'
    )]) {
        sh """
            cd ${folderPath}
            
            git add package.json
            git commit -m "CI: Version bump to ${newVersion}" || true
            git push https://${GIT_USER}:${GIT_PASS}@github.com/AnushSingla/Asha.git HEAD:main
        """
    }

    return newVersion
}