# FarmEasy Web Application

FarmEasy is a **browser-based web application** (not only mobile-native). Farmers and officers can use it on:

- **Desktop/laptop** browsers (Chrome, Firefox, Safari, Edge)
- **Mobile browsers** (responsive layout + bottom navigation)
- **Installable PWA** (“Add to Home Screen” on Android/iOS)

## Stack

| Layer | Technology |
|-------|------------|
| UI | React 19 + TypeScript |
| Build | Vite 6 |
| Routing | React Router 7 |
| PWA | vite-plugin-pwa (service worker, offline shell) |
| Production serve | nginx (Docker) |
| API | Same-origin `/api` → API Gateway |

## Run locally (development)

```bash
# Terminal 1 — backend
cd FarmEasy_backend
docker compose up gateway authentication subscription-service catalog-service i18n-service chatbot-service db redis

# Terminal 2 — web dev server with hot reload
cd FarmEasy_backend/frontend
npm install
npm run dev
```

Open **http://localhost:5173** — Vite proxies `/api` to the gateway.

## Run as production-like web app (Docker)

```bash
cd FarmEasy_backend
docker compose up --build
```

Open **http://localhost:3000** — nginx serves the built SPA and proxies `/api` to the gateway.

## Build static web assets

```bash
cd frontend
npm install
npm run build
# Output: frontend/dist/
```

Deploy `dist/` to **Cloudflare Pages**, **Vercel**, or **Netlify**. Set environment:

```
VITE_API_URL=https://api.yourdomain.com
```

If the API is on another host, configure CORS on the gateway (`CORS_ORIGINS`).

## Web UI structure

1. **Login** — `/login` (OTP, password; Aadhaar placeholder)
2. **Applications** — `/apps` (launcher grid)
3. **App menu** — `/apps/:id` (chapter → topic → subtopic, Pro plan)
4. **Crop AI** — `/chatbot` (image upload + report)

Desktop: left sidebar. Mobile: bottom tab bar.

## PWA install

After `npm run build` and serving over HTTPS, users can install the app from the browser menu. Service worker caches static assets; API uses network-first.

## Environment variables

See `frontend/.env.example`:

- `VITE_API_URL` — API base (empty = same origin `/api`)
- `VITE_API_PROXY` — dev-only proxy target
