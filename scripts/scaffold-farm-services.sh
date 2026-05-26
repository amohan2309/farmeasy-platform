#!/usr/bin/env bash
# Generates pom, Dockerfile, Application, application.yml for farm domain services
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"

gen_pom() {
  local svc=$1
  cat > "$ROOT/$svc/pom.xml" <<'POM'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.farmeasy</groupId>
        <artifactId>farmeasy-platform</artifactId>
        <version>0.1.0-SNAPSHOT</version>
    </parent>
    <artifactId>SVC_ARTIFACT</artifactId>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>net.logstash.logback</groupId>
            <artifactId>logstash-logback-encoder</artifactId>
            <version>8.0</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
POM
  sed -i '' "s/SVC_ARTIFACT/$svc/g" "$ROOT/$svc/pom.xml"
}

gen_dockerfile() {
  local svc=$1 port=$2
  cat > "$ROOT/$svc/Dockerfile" <<EOF
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE $port
ENV JAVA_TOOL_OPTIONS="-Dserver.port=$port"
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF
}

gen_app_yml() {
  local svc=$1 port=$2 schema=$3
  cat > "$ROOT/$svc/src/main/resources/application.yml" <<EOF
server:
  port: $port

spring:
  application:
    name: $svc
  datasource:
    url: \${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/FarmEasy}
    username: \${SPRING_DATASOURCE_USERNAME:anandmohan}
    password: \${SPRING_DATASOURCE_PASSWORD:FarmEasy}
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        default_schema: $schema

management:
  endpoints:
    web:
      exposure:
        include: health,info
EOF
}

gen_application() {
  local svc=$1 pkg=$2
  local cls=$(echo "$pkg" | sed -r 's/(^|-)([a-z])/\U\2/g' | tr -d '-')
  cat > "$ROOT/$svc/src/main/java/com/farmeasy/$pkg/${cls}Application.java" <<EOF
package com.farmeasy.$pkg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ${cls}Application {
    public static void main(String[] args) {
        SpringApplication.run(${cls}Application.class, args);
    }
}
EOF
}

while IFS=',' read -r svc port schema pkg; do
  gen_pom "$svc"
  gen_dockerfile "$svc" "$port"
  gen_app_yml "$svc" "$port" "$schema"
  gen_application "$svc" "$pkg"
done <<'CSV'
marketplace-service,8086,marketplace,marketplace
weather-service,8087,weather,weather
irrigation-service,8088,irrigation,irrigation
iot-integration-service,8089,iot,iot
crop-selling-service,8090,crop_selling,cropselling
live-prices-service,8091,live_prices,liveprices
dashboard-service,8092,dashboard,dashboard
CSV

echo "Scaffold complete"
