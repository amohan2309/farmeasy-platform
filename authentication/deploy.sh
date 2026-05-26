#!/bin/zsh

# Step 1: Build the JAR file
./mvnw clean package

# Step 2: Build the Docker image
docker build -t authentication-app .

# Step 3: Remove any existing container
if docker ps -a --format '{{.Names}}' | grep -Eq '^authentication-app$'; then
  docker rm -f authentication-app
fi

# Step 4: Run the new container
docker run -d --name authentication-app -p 8080:8080 authentication-app

echo "Deployment complete. Container 'authentication-app' is running on Docker Desktop (port 8080)."
