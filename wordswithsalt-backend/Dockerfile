FROM openjdk:8-jre-alpine

RUN mkdir -p /opt/app
WORKDIR /opt/app

COPY ./build/libs/wordswithsalt-backend-0.0.1-SNAPSHOT-all.jar ./app.jar

CMD ["java", "-jar", "app.jar"]