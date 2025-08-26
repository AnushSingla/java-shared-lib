#!/usr/bin/env groovy
package com.example

class Docker implements Serializable {
    def script

    Docker(script) {
        this.script = script
    }

    def buildDocker(String imageName) {
        script.sh "docker build -t ${imageName} ."
    }

    def buildLogin() {
        script.withCredentials([[
            $class: 'UsernamePasswordMultiBinding',
            credentialsId: 'docker-hub-repo',
            usernameVariable: 'USER',
            passwordVariable: 'PASS'
        ]]) {
            script.sh '''
                echo $PASS | docker login -u $USER --password-stdin
            '''
        }
    }

    def buildPush(String imageName) {
        // Use double quotes so Groovy interpolates imageName
        script.sh "docker push ${imageName}"
    }
}
