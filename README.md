# Media Ratings Platform (MRP)

## 1. Project Overview

The Media Ratings Platform is a Java REST API for managing users and media entries.

The application uses Java's built-in HTTP server, PostgreSQL for persistent storage, JDBC for database access, and JWT for authentication.

This repository contains the intermediate submission of the project.

## 2. Technologies

| Technology | Purpose |
|---|---|
| Java | Backend application |
| Java HttpServer | HTTP requests and routing |
| PostgreSQL 16 | Relational database |
| JDBC | Database operations |
| Docker Compose | Database setup |
| Jackson | JSON serialization and deserialization |
| JWT | Authentication |
| JUnit | Automated tests |
| Postman | API testing |

## 3. Architecture

The application separates responsibilities into different layers:

- Model: Represents application data.
- RouteHandler: Routes incoming HTTP requests.
- Controller: Processes HTTP requests and responses.
- Service: Handles business logic and validation.
- Repository: Performs database operations.

Example request flow:

`Postman → MediaRouteHandler → MediaController → MediaService → JdbcMediaRepository → PostgreSQL`

## 4. Requirements

Before running the application, install:

- Java 21
- Docker Desktop with Docker Compose
- Postman
- Git
- The build tool configured in the project

## 5. Database Setup

The PostgreSQL database runs inside Docker.

The project contains:

- `docker-compose.yml`
- `src/main/resources/schema.sql`

The SQL schema defines the `users` and `media` tables, including their primary keys and the foreign key relationship between media entries and their creators.

Start PostgreSQL from the project root:

```bash
docker compose up -d
```

Verify the container:

```bash
docker compose ps
```

The database is configured to use port `5432` on localhost.

The SQL initialization script runs when PostgreSQL initializes a new, empty database volume.

## 6. Application Configuration

Configure the database connection and JWT signing key before starting the application.

Database connection settings for the local Docker environment:

```text
Host: localhost
Port: 5432
Database: postgres
Username: postgres
```

Use the password configured for your local development environment.

The JWT signing key and database credentials must not be committed as production secrets.

### Database Configuration

The application uses a local PostgreSQL database running in Docker.

| Setting | Value |
|---|---|
| Host | localhost |
| Port | 5432 |
| Database | postgres |
| Username | postgres |
| Password | mysecretpassword |

These credentials are intended exclusively for local development and academic testing. They must not be used in production.

The database connection settings in the Java application must match the Docker Compose configuration.

JWT authentication uses a separate signing key.
### 7. Build and Start the Application

The project uses Maven for dependency management and building.

### Build the project

Open a terminal in the project root directory, where `pom.xml` is located.

```bash
mvn clean package
```

### Run the application

Start PostgreSQL first:

```bash
docker compose up -d
```

Then start the Java application using the main class configured in the project.

Start the Java Application

Open the project in IntelliJ IDEA.

Wait for Maven to load the project dependencies.

Start the PostgreSQL database using:

docker compose up -d

Navigate to src/main/java/at/fhtw/mrp/presentation/Main.java.

Click the green Run button next to the main() method.

Main class: at.fhtw.mrp.presentation.Main

The application starts an HTTP server on port 8080.

API base URL: http://localhost:8080

Run Automated Tests

Use the Maven tool window in IntelliJ or execute:

mvn test

PostgreSQL must be running for database integration tests.
The API is accessible at:

`http://localhost:8080`

### Run automated tests

```bash
mvn test
```

The tests require a running PostgreSQL instance if they access the database.
## 8. Authentication

The API uses JWT Bearer authentication.

To authenticate with Postman:

1. Register a user through `POST /api/users/register`.
2. Execute `POST /api/users/login`.
3. The login response contains a JWT in the `token` field.
4. Postman stores the token automatically.
5. Protected endpoints use the saved token.

The login request contains this Postman Tests script:

```javascript
const response = pm.response.json();
pm.collectionVariables.set("token", response.token);
```

For protected requests, select Authorization → Bearer Token and enter:

```text
{{token}}
```

No manual JWT copying is required.

## 9. Implemented Media Endpoints

| Method | Endpoint | Description | Authentication |
|---|---|---|---|
| GET | `/api/media` | List, search and filter media | No |
| GET | `/api/media/{id}` | Get a media entry | No |
| POST | `/api/media` | Create a media entry | JWT |
| PUT | `/api/media/{id}` | Update a media entry | JWT |
| DELETE | `/api/media/{id}` | Delete a media entry | JWT |

Only the creator of a media entry is authorized to update or delete the entry.

### Example: Create Media

`POST /api/media`

```json
{
  "title": "Interstellar",
  "description": "Science Fiction",
  "mediaType": "MOVIE",
  "releaseYear": 2014,
  "ageRestriction": 12,
  "duration": 169,
  "genres": ["ACTION"]
}
```

A successful creation returns HTTP `201 Created`.

### Search and Filtering

Examples:

```text
GET /api/media?title=Interstellar
GET /api/media?genre=ACTION
GET /api/media?mediaType=MOVIE
GET /api/media?sortBy=releaseYear
```

## 10. HTTP Status Codes

The following responses have been verified with Postman:

| Status | Description |
|---|---|
| 200 OK | Successful GET or PUT |
| 201 Created | Media entry created |
| 204 No Content | Media entry deleted |
| 400 Bad Request | Invalid media ID |
| 401 Unauthorized | Missing or invalid JWT |
| 403 Forbidden | User does not own the media entry |
| 404 Not Found | Media entry does not exist |
| 405 Method Not Allowed | Unsupported HTTP method |

## 11. Testing

The project contains JUnit tests for repository operations.

Verified repository operations include:

- Create
- Read
- Update
- Delete
- Search

The Media API has also been tested using Postman, including authentication, authorization, filtering and sorting.

To run the Postman tests:

1. Import the provided Postman Collection.
2. Start PostgreSQL and the Java application.
3. Register or log in with a test user.
4. Execute the Media requests.
5. Check the returned HTTP status codes and JSON responses.

The Collection contains additional requests for planned project functionality. Their presence does not indicate completed implementation.

## 12. Current Project Scope

The intermediate implementation focuses on user authentication and media management.

Further project functionality includes ratings, favorites, recommendations and leaderboard features.

These features are not documented as completed in this intermediate submission.


- PostgreSQL stores application data persistently.
- Docker Compose provides the local database environment.
- The database schema is stored in `src/main/resources/schema.sql`.
- Authentication uses JWT Bearer tokens.
- SQL statements use parameterized queries to protect against SQL injection.
