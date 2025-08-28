# Credit Card Application System

A comprehensive microservices-based credit card application platform built with Spring Boot and Angular.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Overview

This system provides a complete credit card application workflow including user registration, application submission, document management, automated credit decisions, and user dashboard functionality.

## Architecture

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Frontend      │    │  User-App        │    │  Document       │
│  (Angular)      │◄──►│  Service         │◄──►│  Service        │
│  Port: 4200     │    │  Port: 8081      │    │  Port: 8083     │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │  Decision       │
                       │  Engine         │
                       │  Port: 8084     │
                       └─────────────────┘
```

## Features

### Core Functionality
- **User Management**: Registration, authentication with JWT
- **Application Processing**: Multi-step credit card applications
- **Document Management**: File upload and verification system
- **Credit Decision Engine**: Automated approval/rejection based on credit criteria
- **Dashboard**: Real-time application status and decision details

### Security
- JWT-based authentication
- Password encryption with BCrypt
- Cross-origin resource sharing (CORS) support
- Input validation and sanitization

### Business Logic
- Credit score-based decision making
- Dynamic credit limit calculation
- Risk-based interest rate pricing
- Application status tracking

## Technology Stack

### Backend
- **Java 17**
- **Spring Boot**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL**
- **JWT (JSON Web Tokens)**
- **Gradle**

### Frontend
- **Angular 17**
- **Angular Material**
- **TypeScript**
- **Node.js & npm**

### Database
- **PostgreSQL**
- **Multiple database architecture**

## Prerequisites

- Java 17 or higher
- Node.js 18+ and npm
- PostgreSQL
- Gradle 8+
- Angular CLI 17+

## Installation

### 1. Clone Repository
```bash
git clone https://github.com/yourusername/credit-card-app.git
cd credit-card-app
```



### 3. Backend Services Setup

#### User-Application Service
```bash
cd backend/user-application-service
./gradlew bootRun
```

#### Document Service
```bash
cd backend/document-service
./gradlew bootRun
```

#### Decision Engine Service
```bash
cd backend/decision-engine-service
./gradlew bootRun
```

### 4. Frontend Setup
```bash
cd frontend/credit-card-angular
npm install
npm run
```

## Configuration

### Backend Configuration Files

#### User-Application Service (`application.properties`)
```yaml
server:
  port: 8081
spring:
  application:
    name: user-application-service
  datasource:
    url: jdbc:postgresql://localhost:5432/credit_card_users
    username: postgres
    password: password
  jpa:
    hibernate:
      ddl-auto: update
jwt:
  secret: mySecretKey12345678901234567890
  expiration: 86400000
services:
  document-service:
    url: http://localhost:8083
  decision-engine:
    url: http://localhost:8084
```

#### Decision Engine Service (`application.properties`)
```yaml
server:
  port: 8084
spring:
  application:
    name: decision-engine-service
  datasource:
    url: jdbc:postgresql://localhost:5432/credit_decisions
    username: postgres
    password: password
```

#### Document Service (`application.properties`)
```yaml
server:
  port: 8083
spring:
  application:
    name: document-service
  datasource:
    url: jdbc:postgresql://localhost:5432/documents
    username: postgres
    password: password
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB
file:
  upload-dir: ./uploads/documents
```

## API Documentation

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | User registration |
| POST | `/api/auth/login` | User login |

### Application Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/applications/submit` | Submit credit application |
| GET | `/api/applications/user` | Get user applications |
| GET | `/api/applications/{id}` | Get specific application |

### Credit Score Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/credit-scores` | Save credit score |
| GET | `/api/credit-scores/user/{userId}` | Get user credit score |
| PUT | `/api/credit-scores/{userId}` | Update credit score |
| DELETE | `/api/credit-scores/{userId}` | Delete credit score |
| POST | `/api/credit-scores/bulk` | Bulk save credit scores |

### Decision Engine

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/decision/process` | Process application decision |
| GET | `/api/decision/application/{id}` | Get decision by application ID |


## Usage

### 1. Start Services
```bash
# Terminal 1 - User Service
cd backend/user-application-service && ./gradlew bootRun

# Terminal 2 - Document Service  
cd backend/document-service && ./gradlew bootRun

# Terminal 3 - Decision Engine
cd backend/decision-engine-service && ./gradlew bootRun

# Terminal 4 - Frontend
cd frontend/credit-card-angular && ng serve
```

### 2. Access Application
- Frontend: http://localhost:4200
- User Service API: http://localhost:8081
- Document Service API: http://localhost:8083  
- Decision Engine API: http://localhost:8084


### API Testing with curl

#### Register User
```bash
curl -X POST http://localhost:8081/api/auth/register \
-H "Content-Type: application/json" \
-d '{
  "firstName": "John",
  "lastName": "Doe", 
  "email": "john.doe@email.com",
  "password": "password123",
  "phoneNumber": "555-0123",
  "ssn": "123-45-6789",
  "dateOfBirth": "1990-01-01",
  "streetAddress": "123 Main St",
  "city": "Anytown",
  "state": "CA",
  "zipCode": "12345"
}'
```

#### Submit Application
```bash
curl -X POST http://localhost:8081/api/applications/submit \
-H "Content-Type: application/json" \
-H "Authorization: Bearer YOUR_JWT_TOKEN" \
-d '{
  "annualIncome": 75000,
  "employmentStatus": "FULL_TIME",
  "employerName": "Tech Corp",
  "yearsAtJob": 3,
  "monthlyRent": 1500,
  "housingStatus": "RENT"
}'
```


## Project Structure

```
credit-card-app/
├── backend/
│   ├── user-application-service/
│   │   ├── src/main/java/com/creditcard/javaCreditCard/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   ├── controller/
│   │   │   ├── dataObject/
│   │   │   ├── config/
|   │   │   ├── security/
│   │   │   └── util/
|   |   |    JavaCreditCardApplication.java
│   │   ├── src/main/java/resources/application.properties #(add this file manually based on your config)
│   │   └── build.gradle
│   ├── document-service/
│   ├── decision-engine-service/
├── frontend/
│       ├── src/app/
│       │   ├── features/
│       │   ├── shared/
│       ├── package.json
│       └── angular.json
└── README.md
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines
- Follow Java coding standards and Spring Boot best practices
- Update documentation for API changes
- Use meaningful commit messages
- Ensure all tests pass before submitting PR

## Troubleshooting

### Common Issues

#### Database Connection Issues
```bash
# Check PostgreSQL status
sudo systemctl status postgresql

# Restart PostgreSQL
sudo systemctl restart postgresql
```

#### Port Conflicts
```bash
# Check what's running on a port
lsof -i :8081

# Kill process on port
kill -9 $(lsof -t -i:8081)
```

#### JWT Token Issues
- Ensure token is included in Authorization header
- Verify token hasn't expired (default: 24 hours)
- Check JWT secret configuration matches across services

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Note**: This is an MVP implementation intended for demonstration purposes. For production use, additional security measures, monitoring, and compliance considerations should be implemented.