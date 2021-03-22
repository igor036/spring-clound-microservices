FROM openjdk:12-alpine

ARG JAR_FILE=auth-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8083

ENTRYPOINT ["java","-jar","/app.jar"]