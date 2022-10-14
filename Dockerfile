FROM openjdk:19

ARG JAR_FILE=target/*.jar
ARG APP_USER=spring
ARG WORK_DIR="/app"

WORKDIR ${WORK_DIR}
COPY ${JAR_FILE} ${WORK_DIR}/app.jar

RUN groupadd -f ${APP_USER} && adduser ${APP_USER} -g ${APP_USER}

EXPOSE 8080
USER ${APP_USER}:${APP_USER}
ENTRYPOINT ["java","-DCUSTOMER_SVC=customer-service","-jar", "/app/app.jar"]