FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8083
ENV JAVA_TOOL_OPTIONS="-Dserver.port=8083"
ENTRYPOINT ["java", "-jar", "app.jar"]
