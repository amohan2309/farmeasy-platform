FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8084
ENV JAVA_TOOL_OPTIONS="-Dserver.port=8084"
ENTRYPOINT ["java", "-jar", "app.jar"]
