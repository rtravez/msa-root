FROM eclipse-temurin:21-jdk-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

VOLUME /tmp
EXPOSE 8082
COPY ./target/msa-service-0.0.1-SNAPSHOT.jar msa-service.jar
COPY --chown=appuser:appgroup ./target/msa-service-0.0.1-SNAPSHOT.jar /msa-service.jar
USER appuser
ENTRYPOINT ["java","-jar","/msa-service.jar"]