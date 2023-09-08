FROM openjdk:17
ADD target/processdb.jar processdb.jar
ENTRYPOINT ["java", "-jar", "processdb.jar"]
EXPOSE 8080