def call(String imagename , String path ="."){
    sh "docker build -t ${imagename} ${path}"
}