# Expense Tracker Application

A modern, full-featured personal expense management application built with **Spring Boot 3.4**, **Thymeleaf**, and **H2 Database**. This application allows users to track expenses, manage budgets, categorize spending, and gain insights into their financial habits.

## 📋 Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Project Structure](#project-structure)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Database](#database)
- [Configuration](#configuration)
- [Security](#security)
- [Features Overview](#features-overview)

## ✨ Features

### Core Functionality
- **User Management**: User registration, login, and profile management
- **Expense Tracking**: Create, view, edit, and delete expenses with category assignment
- **Income Management**: Track income sources and amounts
- **Budget Planning**: Set and monitor budgets for different categories
- **Category Management**: Create custom expense categories
- **Dashboard**: Visual summary of financial data with spending insights
- **File Upload**: Support for attaching files/receipts to expenses
- **Password Recovery**: Forgot password and reset functionality
- **Responsive UI**: Thymeleaf-based templates with modern styling

### Authentication & Security
- Secure user authentication via Spring Security
- Password-based login system
- Session management
- Protected routes and role-based access control

## 🛠 Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Java** | OpenJDK | 17 |
| **Framework** | Spring Boot | 3.4.5 |
| **Database** | H2 (In-Memory) | Latest |
| **ORM** | Spring Data JPA / Hibernate | Latest |
| **Templating** | Thymeleaf | Latest |
| **Security** | Spring Security | Latest |
| **Build Tool** | Maven | Latest |
| **Actuator** | Spring Boot Actuator | Latest |

## 📋 Prerequisites

- **Java Development Kit (JDK)**: Version 17 or higher
- **Apache Maven**: Version 3.6 or higher
- **Git**: For version control (optional)
- **Browser**: Modern web browser (Chrome, Firefox, Safari, Edge)

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd httpstud
```

### 2. Build the Project

Using Maven:

```bash
mvn clean install
```

Or on Windows, use the provided wrapper:

```bash
mvnw.cmd clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

Or execute the JAR directly:

```bash
java -jar target/httpstud-0.0.1-SNAPSHOT.jar
```

The application will start on **http://localhost:8080**

### 4. Access the Application

- **Main Application**: http://localhost:8080
- **H2 Database Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (leave blank)

## 📁 Project Structure

```
httpstud/
├── src/
│   ├── main/
│   │   ├── java/com/example/httpstud/
│   │   │   ├── HttpstudApplication.java          # Main Spring Boot application
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java           # Spring Security configuration
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java           # Authentication endpoints
│   │   │   │   ├── DashboardController.java      # Dashboard view
│   │   │   │   ├── ExpenseController.java        # Expense management
│   │   │   │   ├── ExpenseRestController.java    # REST API for expenses
│   │   │   │   ├── IncomeController.java         # Income management
│   │   │   │   ├── BudgetController.java         # Budget management
│   │   │   │   └── CategoryController.java       # Category management
│   │   │   ├── model/
│   │   │   │   ├── User.java                     # User entity
│   │   │   │   ├── Expense.java                  # Expense entity
│   │   │   │   ├── Income.java                   # Income entity
│   │   │   │   ├── Budget.java                   # Budget entity
│   │   │   │   └── Category.java                 # Category entity
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java           # User database access
│   │   │   │   ├── ExpenseRepository.java        # Expense database access
│   │   │   │   ├── IncomeRepository.java         # Income database access
│   │   │   │   ├── BudgetRepository.java         # Budget database access
│   │   │   │   └── CategoryRepository.java       # Category database access
│   │   │   ├── service/
│   │   │   │   └── [Service classes]             # Business logic layer
│   │   │   └── exception/
│   │   │       ├── ExpenseNotFoundException.java  # Custom exception
│   │   │       └── GlobalExceptionHandler.java   # Global exception handler
│   │   ├── resources/
│   │   │   ├── application.properties             # Application configuration
│   │   │   ├── static/
│   │   │   │   └── css/
│   │   │   │       └── styles.css                 # Stylesheet
│   │   │   └── templates/
│   │   │       ├── index.html                     # Landing page
│   │   │       ├── login.html                     # Login page
│   │   │       ├── register.html                  # Registration page
│   │   │       ├── dashboard.html                 # Main dashboard
│   │   │       ├── expenses.html                  # Expenses list
│   │   │       ├── expense-form.html              # Expense form
│   │   │       ├── incomes.html                   # Income list
│   │   │       ├── income-form.html               # Income form
│   │   │       ├── budgets.html                   # Budget list
│   │   │       ├── budget-form.html               # Budget form
│   │   │       ├── categories.html                # Categories list
│   │   │       ├── category-form.html             # Category form
│   │   │       ├── profile.html                   # User profile
│   │   │       ├── forgot-password.html           # Password recovery
│   │   │       ├── reset-password.html            # Password reset
│   │   │       ├── error.html                     # Error page
│   │   │       └── fragments/
│   │   │           └── layout.html                # Reusable layout template
│   │   └── test/
│   │       └── java/com/example/httpstud/
│   │           └── HttpstudApplicationTests.java  # Application tests
├── pom.xml                                         # Maven configuration
├── mvnw & mvnw.cmd                                 # Maven wrapper scripts
├── uploads/                                        # Directory for file uploads
└── README.md                                       # This file
```

## ▶️ Running the Application

### Development Mode

```bash
# Using Maven
mvn spring-boot:run

# With hot reload (requires Spring Boot DevTools)
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.devtools.restart.enabled=true"
```

### Production Build

```bash
# Build JAR
mvn clean package

# Run JAR
java -jar target/httpstud-0.0.1-SNAPSHOT.jar
```

### Running Tests

```bash
mvn test
```

## 🔌 API Endpoints

### Authentication
- `POST /login` - User login
- `POST /register` - User registration
- `GET /forgot-password` - Password recovery page
- `POST /reset-password` - Reset password

### Dashboard
- `GET /dashboard` - Main dashboard view

### Expenses
- `GET /expenses` - View all expenses
- `GET /expenses/form` - Display expense form
- `POST /expenses` - Create new expense
- `GET /expenses/{id}/edit` - Edit expense form
- `POST /expenses/{id}/update` - Update expense
- `GET /expenses/{id}/delete` - Delete expense

### Expenses (REST API)
- `GET /api/expenses` - Get all expenses (JSON)
- `GET /api/expenses/{id}` - Get specific expense
- `POST /api/expenses` - Create expense (JSON)
- `PUT /api/expenses/{id}` - Update expense
- `DELETE /api/expenses/{id}` - Delete expense

### Income
- `GET /incomes` - View all income records
- `GET /incomes/form` - Display income form
- `POST /incomes` - Create new income
- `GET /incomes/{id}/edit` - Edit income form
- `POST /incomes/{id}/update` - Update income
- `GET /incomes/{id}/delete` - Delete income

### Budgets
- `GET /budgets` - View all budgets
- `GET /budgets/form` - Display budget form
- `POST /budgets` - Create new budget
- `GET /budgets/{id}/edit` - Edit budget form
- `POST /budgets/{id}/update` - Update budget
- `GET /budgets/{id}/delete` - Delete budget

### Categories
- `GET /categories` - View all categories
- `GET /categories/form` - Display category form
- `POST /categories` - Create new category
- `GET /categories/{id}/edit` - Edit category form
- `POST /categories/{id}/update` - Update category
- `GET /categories/{id}/delete` - Delete category

## 💾 Database

### Database Configuration

The application uses **H2 Database** (in-memory) for development and testing. Configuration in `application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
```

### Database Schema

**Auto-generated by Hibernate** based on entity classes:

- **User** - Stores user account information
- **Expense** - Records expense transactions
- **Income** - Records income transactions
- **Budget** - Stores budget allocations by category
- **Category** - Expense categories

### H2 Console Access

Access the H2 web console at: `http://localhost:8080/h2-console`

## ⚙️ Configuration

### Application Properties

Edit `src/main/resources/application.properties`:

```properties
spring.application.name=Expense Tracker
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.cache=false
```

### Customization

- **Database**: Switch from H2 to MySQL/PostgreSQL by updating dependencies and properties
- **Port**: Change `server.port=8080` in application.properties
- **Logging**: Configure logging levels with `logging.level.*` properties
- **File Upload Directory**: Modify `UPLOAD_DIR` in ExpenseController.java

## 🔒 Security

### Features Implemented

- **Spring Security Integration**: Protects all sensitive endpoints
- **User Authentication**: Login-based access control
- **Session Management**: Secure session handling
- **CSRF Protection**: Built-in CSRF token protection (Thymeleaf)
- **Password Security**: Recommended to use password encoding (BCryptPasswordEncoder)

### Best Practices

- Never commit sensitive data (API keys, passwords) to version control
- Use environment variables for production configuration
- Enable HTTPS in production
- Regularly update dependencies for security patches

## 📝 Features Overview

### Dashboard
- Real-time financial summary
- Total expenses, income, and budget status
- Visual representation of spending by category
- Recent transactions

### Expense Management
- Add expenses with category, amount, date, and description
- Attach receipts/files to expenses
- Edit and delete expenses
- View expense history
- Filter and search expenses

### Income Tracking
- Record income sources and amounts
- Track income by date
- Manage multiple income entries

### Budget Planning
- Set monthly/custom budgets for categories
- Monitor budget vs. actual spending
- Budget alerts and warnings
- Track budget utilization

### Category Management
- Create custom expense categories
- Organize expenses by category
- View category-wise spending breakdown

### User Profile
- View and edit user information
- Account settings
- Password management

## 🤝 Contributing

To contribute to this project:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

## 📄 License

This project is provided as-is for educational and personal use.

## 📞 Support & Contact

For issues, questions, or feedback, please open an issue in the repository or contact the development team.

---

**Last Updated**: 2026-08-18  
**Version**: 0.0.1-SNAPSHOT  
**Status**: Development
