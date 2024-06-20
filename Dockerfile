FROM openjdk:8-slim-buster

RUN mkdir -p /usr/local/app/

WORKDIR /usr/local/app/

COPY target/spring-session-demo.jar .

EXPOSE 8080

CMD java -jar spring-session-demo.jar --spring.profiles.active=docker