

def call(String image, String container, String ports, Map config = [:]) {
    script {
        echo "Deploying container '${container}' from image '${image}'..."

        
        sh "docker stop ${container} || true"
        sh "docker rm ${container} || true"

        
        String envFlags = ""
        if (config.env) {
           
            config.env.each { envVar ->
                
                envFlags += " -e \"${envVar}\""
            }
            echo "Deploying with ${config.env.size()} environment variables."
        } else {
            echo "Deploying with no environment variables."
        }
      
        sh """
            docker run -d \\
                -p ${ports} \\
                --name ${container} \\
                ${envFlags} \\
                -l asha-saathi \\
                --pull always \\
                ${image}
        """

        echo "Successfully deployed '${container}'."
    }
}