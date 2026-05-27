# Library Management System

Small Spring Boot-based library management system implemented in Java (Jakarta/Spring). This README was generated from the repository source.

**Tech stack:** Spring Boot, Spring Security (JWT), Spring Data JPA, H2 in-memory DB, Maven, Lombok, Java 17

**Main module:** [librarymangement/pom.xml](librarymangement/pom.xml#L1-L400)

## Overview

This application provides basic library features: user authentication, role-based access (ADMIN / USER), book and genre management, subscription plans and payments (domain objects present), email support and password reset tokens.

The Spring Boot entry point is `com.lms.librarymangement.LibrarymangementApplication` ([librarymangement/src/main/java/com/lms/librarymangement/LibrarymangementApplication.java](librarymangement/src/main/java/com/lms/librarymangement/LibrarymangementApplication.java#L1-L200)).

## Project structure (important packages)

- `com.lms.controller` — REST controllers (AuthController, BookController, AdminBookController, GenreController, SubscriptionController, SubscriptionPlanController, UserController)
- `com.lms.Service.impl` — service implementations (AuthServiceImpl, BookServiceImpl, UserServiceImpl, SubscriptionServiceImpl, SubscriptionPlanServiceImpl, etc.)
- `com.lms.repository` — Spring Data JPA repositories
- `com.lms.Model` — JPA entities (Book, Genre, User, Subscription, SubscriptionPlan, Payment, PasswordResetToken)
- `com.lms.configurations` — security and JWT classes (`SecurityConfig`, `JwtProvider`, `JwtValidator`, `JwtConstant`)

## Configuration

Application configuration is in [librarymangement/src/main/resources/application.properties](librarymangement/src/main/resources/application.properties#L1-L200).

Key points:
- Uses H2 in-memory DB by default (`spring.datasource.url=jdbc:h2:mem:testdb`). H2 console enabled at `/h2-console`.
- Mail properties are present for SMTP usage (Gmail settings partially configured).
- JWT secret: defined in `JwtConstant` ([librarymangement/src/main/java/com/lms/configurations/JwtConstant.java](librarymangement/src/main/java/com/lms/configurations/JwtConstant.java#L1-L200)).

## Security

- JWT-based stateless security implemented in `JwtProvider` and validated by `JwtValidator` filter. Security rules are in `SecurityConfig` which enforces:
  - requests to `/api/subscription-plans/admin/**` and `/api/admin/**` require `ROLE_ADMIN`.
  - other `/api/**` endpoints require authentication.

## Build & Run

Build with Maven wrapper:

```bash
./mvnw -f librarymangement/pom.xml clean package
```

Run with the wrapper:

```bash
./mvnw -f librarymangement/pom.xml spring-boot:run
```

Or run the generated jar:

```bash
java -jar librarymangement/target/librarymangement-0.0.1-SNAPSHOT.jar
```

## H2 Console

Once running, open the H2 console at `http://localhost:8080/h2-console` and use JDBC URL `jdbc:h2:mem:testdb`.

## Important Endpoints (high level)

Controllers present in the codebase and their purpose:

- `AuthController` — authentication, registration, login, password reset flows
- `BookController` — user-facing book operations
- `AdminBookController` — admin book management (protected)
- `GenreController` — CRUD for book genres
- `SubscriptionController` — user subscriptions and payments
- `SubscriptionPlanController` — subscription plans (admin endpoints under `/api/subscription-plans/admin/**`)
- `UserController` — user profile and management

For exact HTTP paths and request/response DTOs, inspect controller sources under [librarymangement/src/main/java/com/lms/controller](librarymangement/src/main/java/com/lms/controller#L1-L200).

## Notes & TODOs

- JWT secret is stored in code: rotate to environment variable or external secret store for production.
- Update `application.properties` for a persistent database (Postgres/MySQL) and set proper mail credentials.
- Add OpenAPI/Swagger for interactive API documentation.

## Tests

Run tests with:

```bash
./mvnw -f librarymangement/pom.xml test
```

## Contributing

Feel free to open issues or PRs. For local development, use the H2 DB and provide environment overrides in `application-local.properties` or via environment variables.

---
Generated automatically from source on request.
