FROM kaca97/maven-repo:0.1.0 AS appServerTest
ARG STAGE=test
WORKDIR /usr/src/user-service
COPY . .

FROM kaca97/maven-repo:0.1.0 AS appServerBuild
ARG STAGE=dev
WORKDIR /usr/src/user-service
COPY . .
RUN mvn package -P${STAGE} -DskipTests

FROM openjdk:11.0-jdk AS appServerRuntime
WORKDIR /app
COPY --from=appServerBuild /usr/src/user-service/target/user-service.jar ./
EXPOSE 8080
CMD java -jar user-service.jar