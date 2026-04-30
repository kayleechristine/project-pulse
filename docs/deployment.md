# Deployment

## Environments
- Local
- Dev
- Staging
- Production

---

## CI/CD

- GitHub Actions builds and deploys

---

## Azure

- Backend: Azure App Service
- Database: Azure MySQL

---

## Environment Variables

- DB connection
- API base URL
- Secrets stored securely

## Invitation Email

Set these Azure App Service application settings for the backend:

- `FRONTEND_BASE_URL` — the deployed frontend URL, for example `https://your-static-web-app.azurestaticapps.net`
- `MAIL_HOST` — SMTP host, for example `smtp.gmail.com`
- `MAIL_PORT` — usually `587`
- `MAIL_USERNAME` — SMTP username
- `MAIL_PASSWORD` — SMTP password or app password

The frontend also includes `staticwebapp.config.json` so direct links such as `/register?token=...` fall back to `index.html` instead of returning an Azure Static Web Apps 404.
