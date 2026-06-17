FROM openjdk:17-jdk-alpine
COPY target/NASA-real-time-Satliate-Data-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
