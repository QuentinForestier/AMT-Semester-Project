FROM ubuntu

RUN apt update

RUN apt install -y maven

COPY src/ /code/

EXPOSE 8080

WORKDIR /code

ENTRYPOINT mvn spring-boot:run