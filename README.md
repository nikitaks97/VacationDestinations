# 🏝️ Vacation Destinations Catalogue

A modern Spring Boot web application for discovering and sharing vacation destinations. Built with Thymeleaf templates, Spring Security authentication, and a beautiful responsive UI.

## ✨ Features

### 🔐 **Authentication & Authorization**
- User registration and login with Spring Security
- Role-based access control (USER/ADMIN)
- Password encryption with BCrypt
- CSRF protection enabled

### 🏖️ **Destination Management**
- Browse beautiful vacation destinations with stunning visuals
- Like and comment on destinations (requires login)
- Admin dashboard for user management
- Responsive destination cards with animations

### 👥 **Admin Features**
- Admin dashboard with user management
- Edit user roles (USER/ADMIN)
- Delete users (with safety checks)
- Performance monitoring endpoints

### 🎨 **Modern UI/UX**
- Responsive design with Bootstrap 5
- Animated gradient backgrounds
- Glass morphism effects
- Interactive hover animations
- Theme selector functionality
- Accessibility features

### 📊 **Monitoring & Analytics**
- Performance monitoring API endpoints
- Architecture analysis for microservices readiness
- Modern Java 21+ features demonstration
- Health checks and metrics

## 🚀 Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.6+ (or use included wrapper)

### Quick Start

1. **Clone and build the project:**
   ```bash
   git clone <repository-url>
   cd VacationDestinations
   ./mvnw clean install
   ```

2. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Access the application:**
   - Main site: [http://localhost:8080](http://localhost:8080)
   - Admin dashboard: [http://localhost:8080/admin/dashboard](http://localhost:8080/admin/dashboard)

### Default Admin Account
- **Username:** `admin`
- **Password:** `admin123`

## 🏗️ Architecture

### Current State: Monolithic Application
- **Framework:** Spring Boot 3.x with Java 21
- **Frontend:** Thymeleaf + Bootstrap 5 + Custom CSS/JS
- **Database:** H2 embedded database
- **Security:** Spring Security with form-based authentication
- **Build Tool:** Maven

### Key Components
- **Controllers:** Destination, Authentication, Admin management
- **Models:** User, Destination, Like, Comment entities
- **Services:** Business logic and data processing
- **Templates:** Responsive Thymeleaf templates
- **Monitoring:** Performance and architecture analysis endpoints

## 📁 Project Structure
```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── config/          # Security and app configuration
│   │   ├── controller/      # Web controllers
│   │   ├── model/           # JPA entities
│   │   ├── repository/      # Data repositories
│   │   ├── service/         # Business logic
│   │   ├── monitoring/      # Performance monitoring
│   │   └── DemoApplication.java
│   └── resources/
│       ├── templates/       # Thymeleaf HTML templates
│       ├── static/          # CSS, JS, images
│       └── application.properties
└── test/                    # Unit and integration tests
```

## 🔧 Configuration

### Database Configuration
The application uses H2 embedded database by default. Database files are stored in the `data/` directory and excluded from git.

### Security Configuration
- Form-based authentication
- Role-based authorization
- CSRF protection enabled
- Session management configured

## 📈 Monitoring & Metrics

### Available Endpoints
- `/api/monitoring/performance` - System performance metrics
- `/api/monitoring/architecture-analysis` - Microservices readiness assessment
- `/api/monitoring/modern-metrics` - Java 21+ features demonstration

## 🔮 Future Roadmap

See [ARCHITECTURE_MODERNIZATION.md](ARCHITECTURE_MODERNIZATION.md) for detailed microservices migration plan:

1. **Phase 1:** Extract User Authentication Service
2. **Phase 2:** Extract Social Interaction Service  
3. **Phase 3:** Destination Service Optimization
4. **Phase 4:** Frontend Modernization (React/Vue.js)
5. **Phase 5:** Advanced Features (ML recommendations, real-time notifications)

## 🧪 Testing

```bash
# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report
```

## 📦 Deployment

### Docker Support
```bash
# Build Docker image
docker build -t vacation-destinations .

# Run with Docker Compose
docker-compose up
```

### Production Considerations
- Configure external database (PostgreSQL/MySQL)
- Enable HTTPS
- Set up monitoring and logging
- Configure backup strategies

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Bootstrap team for responsive components
- Font Awesome for beautiful icons
- Unsplash for stunning destination images

---

**Built with ❤️ for travelers worldwide** 🌍✈️
