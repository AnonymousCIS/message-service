FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/messageservice-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
RUN mkdir uploads

ENV SPRING_PROFILES_ACTIVE=default
ENV DB_HOST=localhost:1521
ENV DDL_AUTO=update
ENV FILE_PATH=/uploads/
ENV FILE_URL=/uploads/

ENTRYPOINT ["java", "-jar", "-Ddb.host=${DB_HOST}", "-Ddb.password=${DB_PASSWORD}", "-Ddb.username=${DB_USERNAME}", "-Dddl.auto=${DDL_AUTO}", "-Dconfig.server=${CONFIG_SERVER}", "-Deureka.server=${EUREKA_SERVER}", "-Dhostname=${HOSTNAME}", "-Dfile.path=${FILE_PATH}", "-Dfile.url=${FILE_URL}", "app.jar"]

EXPOSE 3006