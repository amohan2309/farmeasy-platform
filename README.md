# Farm Easy Platform

**Farm Easy** is a digital-first agricultural ecosystem for Indian farmers: **Farm Easy Portal** (app/web) + **Farm Easy Smart Chip** (IoT). This repository delivers the MVP backend (Java microservices), API gateway, PostgreSQL, Redis, and a farmer-facing **React PWA**.

## Products

| Product | Description |
|---------|-------------|
| **Farm Easy Portal** | Marketplace, mandi prices, crop selling, irrigation, dashboard, AI assistant, learning content |
| **Farm Easy Smart Chip** | IoT integration service — soil sensors, pump control, wireless sync to portal |

## Architecture

| Service | Port | Purpose |
|---------|------|---------|
| gateway | 8080 | API routing, CORS |
| authentication | 8081 | Login, JWT, OTP |
| subscription-service | 8082 | Plans, entitlements |
| catalog-service | 8083 | Learning apps & gov schemes content |
| i18n-service | 8084 | Locale detection |
| chatbot-service | 8085 | AI crop analysis |
| marketplace-service | 8086 | Seeds, fertilizer, equipment |
| weather-service | 8087 | Forecasts & alerts |
| irrigation-service | 8088 | Schedules, pump control, water usage |
| iot-integration-service | 8089 | Smart Chip devices & sensor readings |
| crop-selling-service | 8090 | Direct crop listings & orders |
| live-prices-service | 8091 | Live mandi prices |
| dashboard-service | 8092 | Aggregated farmer home dashboard |

## Quick start

### Prerequisites

- Java 21, Maven 3.9+
- Docker & Docker Compose
- Node 20+ (frontend dev)

### Full stack (Docker)

```bash
cd FarmEasy_backend
mvn clean package -DskipTests
docker compose up --build
```

| URL | Service |
|-----|---------|
| http://localhost:3000 | Web app (PWA) |
| http://localhost:8080 | API gateway |

### Frontend only (dev)

```bash
cd frontend
npm install
npm run dev   # http://localhost:5173 — proxy VITE_API_URL=http://localhost:8080
```

### Test phone login

1. `POST /api/auth/phone/send-otp` with `{"phoneNumber":"9876543210"}`
2. OTP from Redis: `docker exec -it farmeasy-redis redis-cli GET otp:phone:9876543210`
3. `POST /api/auth/phone/verify` with phone + OTP

### Smart Chip demo

- Device ID: `FE-CHIP-001`
- Status: `GET /api/iot/devices/FE-CHIP-001/status`

## Portal modules (web)

- **Home** — profit summary, journey, quick stats
- **Shop** — seeds, inputs, equipment hire
- **Sell** — mandi prices & crop listings
- **Smart Chip** — live soil moisture, NPK, temperature
- **Water** — irrigation schedules, pump on/off
- **Learn** — schemes & training content
- **AI** — crop image analysis

## Documentation

- [Product design prompt](docs/PRODUCT_DESIGN_PROMPT.md)
- [Web application](docs/WEB_APPLICATION.md)
- [Deployment](docs/DEPLOYMENT.md)
- [Datadog](docs/DATADOG.md)

## License

Proprietary — Farm Easy.
