FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} APIGatewayService-0.0.1-SNAPSHOT.jar
expose 1111
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/APIGatewayService-0.0.1-SNAPSHOT.jar"]