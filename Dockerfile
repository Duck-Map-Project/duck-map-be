FROM amazoncorretto:11
ARG JAR_FILE=build/libs/*SNAPSHOT.jar
VOLUME ["/var/log"]
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]
