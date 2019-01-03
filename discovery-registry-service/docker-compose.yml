FROM openjdk:8-jre
WORKDIR /usr/app
COPY target/discovery-registry-service-0.0.1-SNAPSHOT.jar /discovery-registry-service-0.0.1-SNAPSHOT.jar
EXPOSE 1112
COPY ./ ./
ENTRYPOINT ["java","-jar","discovery-registry-service-0.0.1-SNAPSHOT.jar"]