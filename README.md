# FarmEasy Platform

Indian farmer super-app: Java 21 microservices, PostgreSQL, Redis, API Gateway, React PWA, Datadog-ready logging.

## Architecture

| Service | Port | Purpose |
|---------|------|---------|
| gateway | 8080 | API routing, CORS |
| authentication | 8081 | Login, JWT, OTP, users |
| subscription-service | 8082 | Plans, entitlements, usage quotas |
| catalog-service | 8083 | Apps, chapters, topics, subtopics |
| i18n-service | 8084 | Locale detection, UI messages |
| chatbot-service | 8085 | Crop image analysis (LLM-ready) |

## Quick start (local)

### Prerequisites
- Java 21, Maven 3.9+
- Docker & Docker Compose
- Node 20+ (for frontend)

### Backend

```bash
cd FarmEasy_backend
mvn clean package -DskipTests
docker compose up --build
```

API gateway: http://localhost:8080  
Swagger (auth): http://localhost:8081/swagger-ui.html

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Open http://localhost:5173

### Test phone login
1. Send OTP: `POST /api/auth/phone/send-otp` with `{"phoneNumber":"9876543210"}`
2. Read OTP from Redis: `docker exec -it farmeasy-redis redis-cli GET otp:phone:9876543210`
3. Verify: `POST /api/auth/phone/verify` with phone + OTP

### Upgrade user to Pro (unlock Layer 3 UI)

```bash
curl -X PUT http://localhost:8080/api/subscriptions/users/{userId}/plan \
  -H "Content-Type: application/json" \
  -d '{"planCode":"PRO"}'
```

## Datadog

See [docs/DATADOG.md](docs/DATADOG.md). Enable agent:

```bash
cp .env.example .env   # set DD_API_KEY
docker compose --profile observability up -d
```

## Cloud deployment (cost-effective)

See [docs/DEPLOYMENT.md](docs/DEPLOYMENT.md). Recommended for MVP: **Fly.io** or **Railway** (low idle cost, Docker-native).

## Design specification

See [docs/PRODUCT_DESIGN_PROMPT.md](docs/PRODUCT_DESIGN_PROMPT.md).
