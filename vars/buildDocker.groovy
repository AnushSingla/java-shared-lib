#!/user/bin/env groovy
def call(){
     sh 'docker build -t anushsingla/java-react:jma-2.0 .'
    withCredentials([usernamePassword(credentialsId:'docker-hub-repo',passwordVariable:'PASS',usernameVariable:'USER')]){
        sh 'echo $PASS | docker login -u $USER --password-stdin'
    }
    sh 'docker push anushsingla/java-react:jma-2.0'
    

}