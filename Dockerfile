FROM tomcat:8.0

COPY target/SpringJunitTest-1.0.0.war /usr/local/tomcat/webapps/SpringJunitTest.war
