FROM openjdk:11.0.5-jre-slim

ADD build/libs/students-0.0.1-SNAPSHOT.jar /opt/app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/opt/app.jar"]
