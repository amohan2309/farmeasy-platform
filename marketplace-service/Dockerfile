FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8086
ENV JAVA_TOOL_OPTIONS="-Dserver.port=8086"
ENTRYPOINT ["java", "-jar", "app.jar"]
