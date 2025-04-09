# Spring Boot REST API with JWT Authentication

A secure REST API built with Spring Boot and Kotlin, featuring JWT-based authentication.

## Features

- User registration and login
- JWT authentication with access and refresh tokens
- Secure password hashing
- Token refresh mechanism
- HS512 signature algorithm for enhanced security

## Technical Stack

- Kotlin 1.9.x
- Spring Boot 3.2.x
- Spring Security
- JSON Web Tokens (JWT)
- JPA/Hibernate
- H2 Database (for testing)
- JUnit 5 for testing

## Security Features

- Secure JWT implementation using HS512 algorithm
- 512-bit signing key
- Separate access and refresh tokens
- Proper error handling for invalid tokens
- HTTP 401 responses for authentication failures

## Prerequisites

- JDK 17 or higher
- Gradle 8.x

## Configuration

The application uses the following key configurations:

```properties
jwt.secret=your-512-bit-secret
jwt.expiration=3600000      # 1 hour in milliseconds
jwt.refresh-expiration=86400000  # 24 hours in milliseconds
```

## API Endpoints

### Authentication

```
POST /api/auth/register
POST /api/auth/login
POST /api/auth/refresh
```

### Request/Response Examples

#### Register
```json
POST /api/auth/register
{
    "email": "user@example.com",
    "password": "password123"
}
```

#### Login
```json
POST /api/auth/login
{
    "email": "user@example.com",
    "password": "password123"
}
```

Response:
```json
{
    "accessToken": "eyJhbG...",
    "refreshToken": "eyJhbG..."
}
```

#### Refresh Token
```json
POST /api/auth/refresh
{
    "refreshToken": "eyJhbG..."
}
```

## Running Tests

```bash
./gradlew test
```

## Security Considerations

- Uses HS512 (HMAC with SHA-512) for token signing
- Requires a 512-bit (64-byte) signing key
- Implements proper token validation and error handling
- Returns appropriate HTTP status codes for security-related responses

## Development Notes

- JVM target is set to Java 17
- Uses Spring Security best practices
- Implements proper exception handling for JWT operations
- Database cleanup between integration tests using @DirtiesContext

## Error Handling

- Invalid credentials: Returns 401 Unauthorized
- Invalid/expired tokens: Returns 401 Unauthorized
- Invalid requests: Returns 400 Bad Request
- Server errors: Returns 500 Internal Server Error

# REST API Authentication Service

A Spring Boot REST API with JWT authentication, built with Kotlin.

## Prerequisites

- Java 21
- Maven 3.9+ or Maven Daemon (mvnd)
- Your favorite IDE (IntelliJ IDEA recommended for Kotlin development)

## Technology Stack

- Kotlin 1.9.25
- Spring Boot 3.2.3
- Spring Security
- JWT (JSON Web Tokens)
- H2 Database
- JUnit 5 for testing

## Getting Started

### 1. Clone the repository
```bash
git clone <repository-url>
cd rest-api
```

### 2. Build the project
Using Maven:
```bash
mvn clean install
```
Or using Maven Daemon:
```bash
mvnd clean install
```

### 3. Run the application
```bash
mvn spring-boot:run
```
Or using Maven Daemon:
```bash
mvnd spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication

#### Register a new user
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123",
    "name": "Test User"
  }'
```

#### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

#### Refresh Token
```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "your-refresh-token-here"
  }'
```

### Protected Endpoints
To access protected endpoints, include the JWT token in the Authorization header:
```bash
curl -H "Authorization: Bearer your-access-token" http://localhost:8080/your-protected-endpoint
```

## Testing

### Run all tests
```bash
mvn test
```
Or using Maven Daemon:
```bash
mvnd test
```

### Run specific test categories
```bash
# Run only unit tests
mvn test -Dtest=*Test

# Run only integration tests
mvn test -Dtest=*IntegrationTest
```

## Database

The application uses H2 in-memory database. You can access the H2 console at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: ` ` (empty)

## Security Features

- JWT-based authentication
- Refresh token mechanism
- Password encryption using BCrypt
- Stateless session management
- CSRF protection disabled for API endpoints
- Public endpoints under `/api/auth/**`
- All other endpoints require authentication

## Response Format

### Successful Authentication Response
```json
{
  "token": "your.jwt.token",
  "refreshToken": "your.refresh.token",
  "email": "user@example.com",
  "name": "User Name"
}
```

### Error Response
```json
{
  "message": "Error message",
  "status": 400
}
```

## Development

### Project Structure
```
src/
├── main/
│   ├── kotlin/
│   │   └── com/restapp/rest_api/
│   │       ├── config/          # Configuration classes
│   │       ├── controller/      # REST controllers
│   │       ├── dto/            # Data Transfer Objects
│   │       ├── model/          # Domain models
│   │       ├── repository/     # Data repositories
│   │       ├── security/       # Security configurations
│   │       └── service/        # Business logic
│   └── resources/
│       └── application.properties
└── test/
    ├── kotlin/
    │   └── com/restapp/rest_api/
    │       ├── controller/     # Controller tests
    │       ├── service/       # Service tests
    │       └── utils/         # Test utilities
    └── resources/
        └── application-test.properties
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

#

<br>

<div align="center">
  <a  href="https://github.com/jeffersontavaresdm">
    <img width="30%" src="https://github.com/jeffersontavaresdm/jeffersontavaresdm/blob/main/images/call-me-shaq.gif" width="25"/>
  </a>
</div>
