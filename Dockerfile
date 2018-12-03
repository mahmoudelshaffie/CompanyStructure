FROM openjdk:8-jdk-alpine
VOLUME /tmp
WORKDIR /usr/src/app
COPY ./target/org.tradeshit.companystructure-0.0.1-SNAPSHOT.jar /usr/src/app/
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/lib /app/lib
COPY ${DEPENDENCY}/lib/META-INF /app/META-INF
COPY target/classes /app
EXPOSE 8080
CMD ["java", "-jar", "org.tradeshit.companystructure-0.0.1-SNAPSHOT.jar"]
