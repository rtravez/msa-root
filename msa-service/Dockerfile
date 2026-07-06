FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
EXPOSE 1987
COPY ./target/msa-service-0.0.1-SNAPSHOT.jar msa-service.jar
ENTRYPOINT ["java","-jar","/msa-service.jar"]