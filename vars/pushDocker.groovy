def call(String imagename){
    sh "docker push  ${imagename}"
}