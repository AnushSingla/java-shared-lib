def call(String folderPath = '') {
    def packageFile = "${folderPath}/package.json"
    def pkgJSON = readJSON file: packageFile
    
    echo "Current version: ${pkgJSON.version}"

    def parts = pkgJSON.version.split('\\.')
    def major = parts[0] as int
    def minor = parts[1] as int
    def patch = parts[2] as int

    patch = patch + 1
    def newversion = "${major}.${minor}.${patch}"

    echo "New version: ${newversion}"

    pkgJSON.version = newversion
    writeJSON file: packageFile, json: pkgJSON, pretty: 4

    sh """
        cd ${folderPath}
        git config user.email "jenkins@asha-saathi.com"
        git config user.name "Jenkins Bot Asha-Saathi"
        git add package.json
        git commit -m "CI: Version bump to ${newversion}"
        git push origin HEAD:main
    """

    return newversion
}
