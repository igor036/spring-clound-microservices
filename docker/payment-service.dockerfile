FROM openjdk:12-alpine

ARG JAR_FILE=payment-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","/app.jar"]