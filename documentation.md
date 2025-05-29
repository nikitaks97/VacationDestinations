# Vacation Destinations - Project Documentation

## Project Overview
Vacation Destinations is a modern Spring Boot web application that allows users to explore, like, and comment on various travel destinations. The application features user authentication, admin management, and integrates with a relational database. It is designed for easy deployment using Docker and supports code quality analysis with SonarQube.

## Technologies Used
- **Java 21** (Temurin distribution)
- **Spring Boot** (Web, Data JPA, Security)
- **Hibernate** (ORM)
- **H2 Database** (for development/testing)
- **Maven** (build & dependency management)
- **JUnit & Spring Test** (testing)
- **Thymeleaf** (templating)
- **Docker** (containerization)
- **GitHub Actions** (CI/CD)
- **SonarQube** (code quality & coverage)

---

## Local Development Setup

### 1. Prerequisites
- Java 21 (JDK)
- Maven 3.8+
- Git

### 2. Clone the Repository
```sh
git clone <repo-url>
cd VacationDestinations-main
```

### 3. Install Dependencies
Maven will automatically resolve dependencies:
```sh
mvn clean install
```

### 4. Run Tests
To execute all unit and integration tests:
```sh
mvn test
```

### 5. Build the Application
To build the application JAR:
```sh
mvn clean package
```
The JAR will be generated in the `target/` directory.

### 6. Run the Application
```sh
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### 7. Access the Application
- Open your browser and go to: [http://localhost:8080](http://localhost:8080)
- Default endpoints:
  - `/` : Home page
  - `/login` : Login page
  - `/register` : User registration
  - `/admin` : Admin dashboard (admin only)

---

## Running with Docker

### 1. Build Docker Image
```sh
docker build -t vacationapp:latest .
```

### 2. Run Docker Container
```sh
docker run -d -p 8080:8080 --name vacationapp vacationapp:latest
```

### 3. Access the Application
- Open your browser at: [http://localhost:8080](http://localhost:8080)

### 4. Stopping and Removing the Container
```sh
docker stop vacationapp
# To remove:
docker rm vacationapp
```

---

## Using Docker Compose (if available)
If a `docker-compose.yml` is present:
```sh
docker-compose up --build
```

---

## Code Quality & Coverage (SonarQube)
- The project is integrated with SonarQube for static analysis and code coverage.
- To run analysis locally (requires SonarQube server):
```sh
mvn verify org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184:sonar \
  -Dsonar.projectKey=<your_project_key> \
  -Dsonar.organization=<your_org> \
  -Dsonar.host.url=<your_sonar_url> \
  -Dsonar.login=<your_token>
```

---

## CI/CD Pipeline (GitHub Actions)
- On every push or pull request, the workflow will:
  - Set up JDK 21
  - Cache Maven dependencies
  - Build the project
  - Run tests and SonarQube analysis
  - Build and push Docker image to DockerHub

---

## Useful Commands
- **Clean and build:** `mvn clean package`
- **Run tests:** `mvn test`
- **Run locally:** `java -jar target/demo-0.0.1-SNAPSHOT.jar`
- **Build Docker image:** `docker build -t vacationapp:latest .`
- **Run Docker container:** `docker run -d -p 8080:8080 vacationapp:latest`
- **Docker Compose up:** `docker-compose up --build`

---

## Additional Notes
- Application properties can be configured in `src/main/resources/application.properties`.
- For development, the H2 database is used by default. For production, configure a persistent database.
- Admin features are accessible only to users with the `ADMIN` role.
- For troubleshooting, check logs in the console or `target/` directory.

---

For more details, refer to the `README.md` or project source code.
