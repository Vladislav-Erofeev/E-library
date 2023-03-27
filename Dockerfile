FROM openjdk:19
ADD target/E-Library-0.0.1-SNAPSHOT.jar E-Library-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "E-Library-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080