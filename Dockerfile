FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/messageservice-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENV SPRING_PROFILES_ACTIVE=default
ENV DB_HOST=localhost:1521
ENV DDL_AUTO=update

ENTRYPOINT ["java", "-jar", "-Dconfig.server=${CONFIG_SERVER}", "-Ddb.host=${DB_HOST}", "-Ddb.password=${DB_PASSWORD}", "-Ddb.username=${DB_USERNAME}", "-Dddl.auto=${DDL_AUTO}", "-Deureka.server=${EUREKA_SERVER}", "-Dhostname=${HOSTNAME}", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "app.jar"]

EXPOSE 3006