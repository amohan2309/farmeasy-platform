FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8088
ENV JAVA_TOOL_OPTIONS="-Dserver.port=8088"
ENTRYPOINT ["java", "-jar", "app.jar"]
