# Cloud deployment guide (cost-focused)

## Recommended providers (cheapest for MVP)

| Provider | Best for | Typical MVP cost |
|----------|----------|------------------|
| [Fly.io](https://fly.io) | Docker microservices, India region | ~$5–15/mo idle |
| [Railway](https://railway.app) | Fast Docker deploy, managed Postgres | ~$5/mo + usage |
| [Render](https://render.com) | Free tier web services | $0–7/mo starter |
| Oracle Cloud Free Tier | Always-free VMs (more ops work) | $0 |

**PostgreSQL + Redis:** use provider add-ons (Fly Postgres, Railway Redis) or **Supabase** (Postgres) + **Upstash** (Redis free tier).

## Fly.io example (single region)

1. Install `flyctl` and `fly auth login`
2. Per service:
   ```bash
   cd gateway && fly launch --no-deploy
   fly secrets set JWT_SECRET=... SPRING_DATASOURCE_URL=...
   fly deploy
   ```
3. Attach Fly Postgres; set `SPRING_DATASOURCE_URL` on all Java services.
4. Set `CORS_ORIGINS=https://your-frontend.fly.dev` on gateway.

## Frontend

Deploy `FarmEasy_frontend` to **Cloudflare Pages** or **Vercel** (free):

```bash
npm run build
# VITE_API_URL=https://api.yourdomain.com
```

## India data residency

For farmer/gov data, prefer **AWS ap-south-1 (Mumbai)** or **Azure Central India** when you outgrow Fly/Railway.

## Secrets checklist

- `JWT_SECRET` (32+ characters)
- `SPRING_DATASOURCE_*`
- `DD_API_KEY` (Datadog)
- `ANTHROPIC_API_KEY` (crop vision, optional)

Never commit `.env` to git.
