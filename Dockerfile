FROM tomcat:8.0

COPY target/SpringJunitTest.war /usr/local/tomcat/webapps/SpringJunitTest.war
