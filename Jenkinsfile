def CONTAINER_NAME="jenkins-pipeline"
def CONTAINER_TAG="latest"
def HTTP_PORT="8082"
def TEST_URL="http://localhost:${HTTP_PORT}/SpringJunitTest/lastname?firstname=James"
def TEST_TEXT="The user last name is - James"

node {

    stage('Checkout') {
        //git 'https://github.com/MarcoGhise/SpringJunitTest.git'
        checkout scm
    }

    stage('Unit Test'){
        bat "mvn -U clean compile"
    }
    
    stage('Package'){
        bat "mvn package -DskipTests"
    }

    stage("Image Clear up"){
          try {
          bat "docker stop mysql"
          bat "docker rm -f mysql"
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
      bat "docker run --name mysql -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -d mysql:5.5"
      bat "docker run -d -p $HTTP_PORT:8080 --link mysql:mysql --name $CONTAINER_NAME $CONTAINER_NAME:$CONTAINER_TAG"
      echo "Application started on port: ${HTTP_PORT} (http)"
    }
 
    stage('Integration Test'){
        sleep(10)
        bat "docker exec -i mysql mysql -e \"create schema junit;use junit;DROP TABLE IF EXISTS Customers;CREATE TABLE Customers (  id INT NOT NULL AUTO_INCREMENT,  first_name VARCHAR(30),  last_name  VARCHAR(30),  PRIMARY KEY (`id`));INSERT INTO Customers VALUES (1, 'James', 'Dean');INSERT INTO Customers VALUES (2, 'Larry', 'Bird');\""
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
