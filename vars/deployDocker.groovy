
def call(String imagename, String containerName, String portMapping) {
    sh """
        docker stop ${containerName} || true
        docker rm ${containerName} || true
        
        
        docker run -d --name ${containerName} -p ${portMapping} ${imagename}
    """
}