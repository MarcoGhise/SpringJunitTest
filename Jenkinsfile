def CONTAINER_NAME="jenkins-pipeline"
def CONTAINER_TAG="latest"
def HTTP_PORT="8082"
def TEST_URL="http://localhost:${HTTP_PORT}/SpringJunitTest/lastname?firstname=James"
def TEST_TEXT="The user last name is - Dean"

node {

    stage('Checkout') {
        git 'https://github.com/MarcoGhise/SpringJunitTest.git'
    }

    stage('Unit Test'){
        bat "mvn -U clean compile"
    }
    
    stage('Package'){
        bat "mvn package -DskipTests"
    }

    stage("Image Clear up"){
          try {
          bat "docker stop $CONTAINER_NAME"
          bat "docker rm -f $CONTAINER_NAME"
          bat "docker rmi $CONTAINER_NAME:$CONTAINER_TAG"
      } catch(error){}
    }

    stage('Image Build'){
      bat "docker build -t $CONTAINER_NAME:$CONTAINER_TAG  -t $CONTAINER_NAME --pull --no-cache ."
      echo "Image build complete"
    }

    stage('Run App'){
      bat "docker run -d -p $HTTP_PORT:8080 --link mysql:mysql --name $CONTAINER_NAME $CONTAINER_NAME:$CONTAINER_TAG"
      echo "Application started on port: ${HTTP_PORT} (http)"
    }
 
    stage('Integration Test'){
        sleep(20)
        def response = httpRequest "${TEST_URL}"
        println("Status: "+response.status)
        println("Content: "+response.content)
        if (!response.content.contains("${TEST_TEXT}"))
        {
            echo '[FAILURE] Failed to verify TEST_TEXT'
            currentBuild.result = 'FAILURE'
            return
        }
    }
    stage('Backup Release'){
        fileOperations([fileCopyOperation(excludes: '', flattenFiles: true, includes: 'target/SpringJunitTest.war', targetLocation: 'C:\\deploy')])
        fileOperations([fileRenameOperation(destination: 'C:\\deploy\\SpringJunitTest-$BUILD_TIMESTAMP.war', source: 'C:\\deploy\\SpringJunitTest.war')])
    }
}
