# CareConnect Patient Portal

A full-stack healthcare patient portal for account registration, secure sign-in, appointment scheduling, and access to medical records.

## Stack

- Java 17, Spring Boot 3, Spring Security, JWT, OAuth2-ready configuration
- React 18, Vite, responsive CSS
- PostgreSQL 16
- Docker Compose

## Run with Docker

```bash
docker compose up --build
```

Open `http://localhost:5174`. The API is served at `http://localhost:8081/api`; PostgreSQL is available on `localhost:15432`.

## Demo credentials

After the API starts, sign in with `patient@careconnect.dev` / `Password123!`.

## Local development

Start PostgreSQL using Docker, then run `./mvnw spring-boot:run` from `backend` and `npm install && npm run dev` from `frontend`.
