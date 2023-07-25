FROM amazoncorretto:17
ARG JAR_FILE=build/libs/*SNAPSHOT.jar
VOLUME ["/var/log", "/image"]
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]
