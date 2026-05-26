# Farm Easy — Product & Architecture Reference

See the master prompt used to build this platform. Farm Easy combines:

1. **Farm Easy Portal** — digital hub (marketplace, trade, irrigation, finance hooks, AI, learning)
2. **Farm Easy Smart Chip** — IoT hardware (irrigation automation, soil NPK, portal sync)

## Implemented in this repo (MVP)

- Microservices per domain with database-per-schema on PostgreSQL
- API Gateway (Spring Cloud Gateway)
- React PWA with Hindi/English
- IoT integration API for Smart Chip telemetry
- Event-ready architecture (order/payment flows planned via Kafka in Phase 3+)

## Farmer journey

Portal: seeds → equipment → irrigation plan → monitor soil (via chip) → harvest → sell to mandi/factory → profit dashboard → next season.

## Roadmap services (not yet implemented)

Government schemes, land records, crop insurance, transport, payments, community forum, Kafka event bus, Eureka discovery.
