def call(String folderPath = ''){
    def packageFile = "${folderPath}/package.json"
    def pkgJson = readJSON file: packageFile
    echo("Current version ${pkgJSON.version}")
    def(major,minor,patch) = pkgJSON.version.tokenize('.').collect(it as int )
    patch = patch + 1
    def newversion = "${major}.${minor}.${patch}"
    echo "New version: ${newversion}"
    pkgJSON.version = newversion
    writeJSON file: packageFile, json: pkgJson, pretty: 4

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