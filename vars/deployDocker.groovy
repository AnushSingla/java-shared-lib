def call(String imagename, String containerName, int port=3000) {
    sh """
        docker stop ${containerName} || true
        docker rm ${containerName} || true
        docker run -d --name ${containerName} -p ${port}:${port} ${imagename}
    """
}
