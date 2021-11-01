FROM maven:3-jdk-11 AS maven
WORKDIR /opt/build
COPY src src
COPY pom.xml .
RUN mvn clean package

FROM openjdk:11
WORKDIR /opt/app
RUN groupadd -r runner && useradd -r -g runner runner
RUN chown runner -R /opt/app && chmod 700 -R /opt/app
COPY --from=maven /opt/build/target/*.jar ./food-delivery.jar
ENTRYPOINT ["java", "-jar", "food-delivery.jar"]