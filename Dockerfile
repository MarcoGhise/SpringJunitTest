FROM tomcat:latest

COPY target/SpringJunitTest-1.0.0.war /usr/local/tomcat/webapps/SpringJunitTest-1.0.0.war
