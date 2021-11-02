FROM maven:3-jdk-11 AS maven
WORKDIR /opt/build
COPY src src
COPY pom.xml .
RUN mvn clean package

FROM openjdk:11
WORKDIR /opt/app
COPY --from=maven /opt/build/target/*.jar ./food-delivery.jar
RUN groupadd -r runner -g 1000 && useradd -r -g 1000 runner -u 1000
RUN chown runner -R /opt/app && chmod 700 -R /opt/app
USER runner
ENTRYPOINT ["java", "-jar", "food-delivery.jar"]