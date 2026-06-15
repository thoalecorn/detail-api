# Dental API — Nelumbo Technical Challenge

REST API for managing dental appointments across multiple dental clinics belonging to the same oral health services network.

---

## Technologies

- **Java 21**
- **Spring Boot 4.1.0**
  - Spring Web
  - Spring Security
  - Spring Data JPA
  - Spring Validation
- **PostgreSQL** — Relational database engine
- **JJWT 0.12.6** — JWT-based authentication
- **SpringDoc OpenAPI 2.8.9** — Swagger UI documentation
- **Lombok** — Boilerplate reduction
- **Maven** — Dependency management and build

---

## 📁 Project structure

```
src/main/java/com/nelumbo/dental_api/
├── controller/       # REST controllers
├── service/          # Business logic
├── repository/       # JPA interfaces
├── entity/           # Persistence entities
├── enums/            # Domain enumerations
├── dto/              # Data transfer objects
│   ├── auth/
│   ├── clinic/
│   ├── office/
│   ├── dentist/
│   ├── procedure/
│   ├── appointment/
│   ├── notification/
│   └── indicator/
├── security/         # JWT and Spring Security configuration
├── exception/        # Custom exceptions and global handler
└── config/           # General configurations
```

---

## Environment variables

Create a `.env` file at the root of the project with the following variables:

```env
DB_URL=jdbc:postgresql://localhost:5432/dental_db
DB_USERNAME=your_username
DB_PASSWORD=your_password
JWT_SECRET=your_secret_key
JWT_EXPIRATION=21600000
NOTIFICATION_SERVICE_URL=http://localhost:8081
```

---

## Database

Engine: **PostgreSQL**

Create the database before running the project:

```sql
CREATE DATABASE dental_db;
```

> The table schema is automatically generated via Hibernate JPA on startup.

---

## How to run locally

### Prerequisites

- Java 21 installed
- PostgreSQL running locally
- Maven (or use the included `./mvnw` wrapper)
- Notification microservice running on port 8081

### Steps

```bash
# 1. Clone the repository
git clone https://github.com/thoalecorn/detail-api.git
cd detail-api

# 2. Configure environment variables (see section above)

# 3. Build and run
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

---

## Notification Microservice

The notification microservice is an independent project that must run in parallel.

```bash
# Repository
git clone https://github.com/thoalecorn/notification-service.git
cd notification-service

# Run
./mvnw spring-boot:run
```

The microservice will be available at `http://localhost:8081`.

---

## Preloaded admin user

When the application starts, an admin user is automatically created:

```
Email:    admin@mail.com
Password: admin
```

---

## API Documentation

The interactive API documentation is available in Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

---

## Postman Collection

The Postman collection with all documented endpoints is located at:

```
/postman/dental-api.postman_collection.json
```

Import the file in Postman to test all endpoints directly.

---

## Authentication

All endpoints are protected by JWT. To authenticate:

1. Make a `POST /auth/login` request with your credentials
2. Copy the token from the response
3. Add it to the header of each request:

```
Authorization: Bearer <token>
```

The token expires after **6 hours**.

---

## Roles

| Role | Permissions |
|---|---|
| ADMIN | Full CRUD, global indicators, notifications, approve/reject appointments |
| RECEPCIONISTA | Schedule appointments, check-in, attendance, cancel, clinic indicators |

---

## Entity-Relationship Model

<img width="2822" height="2100" alt="MER DENTAL AIP NELUMBO" src="https://github.com/user-attachments/assets/826719b4-3735-42b2-9f42-e4b1c5fbfcd9" />

---

## Implemented features

- JWT with 6-hour expiration and token blacklist
- Full CRUD for clinics, offices, dentists and procedures
- Appointment management with business rule validations
- Dentist schedule overlap and office capacity control
- Check-in, attendance, cancellation and no-show handling
- Late cancellation fee (30% if cancelled within 24 hours)
- Patient blocking after 3 no-shows within 90 days
- Event history per appointment
- Simulated notification microservice
- Clinical and financial indicators
- Swagger UI documentation
- Global exception handling
