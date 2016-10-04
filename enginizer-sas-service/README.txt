
* Run locally with embedded tomcat (JDK 8 required):

	mvn package && $JAVA_HOME/bin/java -jar target/domestic-templates-service-1.0.0.war

	- with debug on:

	mvn package && $JAVA_HOME/bin/java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -jar target/domestic-templates-service-1.0.0.war


* Access WSDL:
	
	http://localhost:9000/services/banking/domesticTemplates/v1/domesticTemplatesV1.wsdl


* Tutorials: 

	SpringBoot: http://spring.io/guides/gs/spring-boot/
	SpringWS:   http://spring.io/guides/gs/producing-web-service/
	SpringJPA:  http://spring.io/guides/gs/accessing-data-jpa/

