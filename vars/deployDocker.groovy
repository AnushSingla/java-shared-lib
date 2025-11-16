def call(String imagename, String containerName, String portmapping) {
    sh """
        docker stop ${containerName} || true
        docker rm ${containerName} || true
        docker run -d --name ${containerName} -p ${portmapping} ${imagename}
    """
}
