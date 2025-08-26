#!/user/bin/env groovy
package com.example
class Docker implements Serializable{
    def script
    Docker(script){
        this.script=script
    }
    def buildDocker(String imageName){
        script.sh 'docker build -t $imageName .'
    }

    def buildLogin(){
        script.withCredentials([usernamePassword(credentialsId:'docker-hub-repo',passwordVariable:'PASS',usernameVariable:'USER')]){
        script.sh 'echo '${script.PASS}' | docker login -u '${script.USER}' --password-stdin'
    }
    }
    def buildPush(String imageName){
         script.sh 'docker push $imageName'
    }
}