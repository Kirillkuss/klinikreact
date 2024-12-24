FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} klinikreact.jar
ENTRYPOINT ["java","-jar","/klinikreact.jar"]
EXPOSE 8086:8086