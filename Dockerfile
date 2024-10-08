FROM maven:3.9.9 AS maven
LABEL authors="Muratcan Yeldan"

WORKDIR /opt/reminder
COPY . /opt/reminder
RUN mvn clean install

FROM eclipse-temurin:21-jre-alpine

ARG JAR_FILE=PetVaccineReminder.jar

WORKDIR /opt/reminder

COPY --from=maven /opt/reminder/target/${JAR_FILE} /opt/reminder/

ENTRYPOINT ["java","-Xdebug","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000","-jar","PetVaccineReminder.jar"]