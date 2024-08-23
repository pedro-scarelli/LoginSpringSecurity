FROM maven:3.8.3-openjdk-17

WORKDIR /

COPY target/user-0.0.1-SNAPSHOT.jar user-app.jar

ENTRYPOINT [ "java", "-jar", "user-app.jar" ]