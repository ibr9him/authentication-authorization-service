FROM gradle:6.0-jdk11 AS build
WORKDIR /src
COPY . /src
RUN gradle build -x test

FROM build AS test
WORKDIR /src
COPY --from=build /src /src
RUN gradl test

FROM openjdk:11-jre-slim
EXPOSE 9090
WORKDIR /app
COPY --from=build /src/out/*.jar /app/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
