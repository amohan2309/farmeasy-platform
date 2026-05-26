# Datadog observability for FarmEasy

## What is configured

- **JSON logs** via Logstash encoder in each service (`logback-spring.xml` pattern on gateway; console JSON elsewhere).
- **Docker labels** via `DD_SERVICE` and `DD_ENV` in `docker-compose.yml`.
- **Optional Datadog Agent** profile `observability` collects container logs and APM.

## Setup

1. Create a [Datadog account](https://www.datadoghq.com/) and copy your **API key**.
2. Copy `.env.example` to `.env` and set:
   ```
   DD_API_KEY=your_key_here
   DD_SITE=datadoghq.com
   DD_ENV=dev
   ```
3. Start stack with agent:
   ```bash
   docker compose --profile observability up -d
   ```
4. In Cursor, run `/ddsetup` if you want to query logs via the Datadog MCP plugin.

## Production (cloud)

On **Fly.io** / **Railway** / **AWS ECS**, either:

- Run the Datadog Agent as a sidecar, or
- Use [Datadog Serverless / Agentless](https://docs.datadoghq.com/) for your platform.

Set unified tags: `service`, `env`, `version` for filtering farmer-platform services.

## Log queries (examples)

```
service:authentication-service status:error
service:chatbot-service @message:*limit*
```

## APM (optional next step)

Add to each service `Dockerfile` entrypoint:

```dockerfile
ENV JAVA_TOOL_OPTIONS="-javaagent:/dd-java-agent.jar"
```

Download agent from Datadog docs and mount at build time for distributed traces across gateway → chatbot → subscription.
