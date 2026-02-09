# Working Day Calculator (Clean Architecture)

A Spring Boot application demonstrating SOLID principles to calculate working days (Monday-Friday) between two dates.

## Architecture
- **Domain Layer**: Contains `WorkingDayService`. It is independent of Spring Boot.
- **Infrastructure Layer**: Uses Spring Boot and Thymeleaf to provide a web interface.
- **SOLID Compliance**:
    - **SRP**: Calculation logic is isolated from web logic.
    - **DIP**: The UI depends on abstractions (methods), not specific persistence details.

## API Specification
- `GET /`: Returns the HTML form.
- `GET /calculate?start=YYYY-MM-DD&end=YYYY-MM-DD`: Returns the calculation result.

## Deployment Instructions

### Prerequisites
- Docker installed
- Maven (if building locally)

### Option 1: Using Docker
1. Build the image:
   ```bash
   docker build -t working-day-calc .# Working Day Calculator

A Spring Boot application that calculates working days (Monday-Friday) between two dates with comprehensive validation and testing.

## Features
- Calculate working days between any two dates (inclusive)
- Input validation for date ranges (1900-01-01 to 2100-12-31)
- Prevents invalid date ranges (start date after end date)
- Clean web interface with error handling
- Comprehensive test coverage

## Architecture
- **Domain Layer**: `WorkingDayService` handles business logic independently of Spring Boot
- **Infrastructure Layer**: Spring Boot web controllers with Thymeleaf templates
- **Validation**: Input validation with proper error messages and redirects

## API Specification
- `GET /`: Returns the HTML form for date input
- `GET /calculate?start=YYYY-MM-DD&end=YYYY-MM-DD`: Calculates and displays working days

## Validation Rules
- Start date cannot be after end date
- Date range must be between 1900-01-01 and 2100-12-31
- Both dates are required

## Running the Application

### Prerequisites
- Java 17+
- Maven 3.6+

### Option 1: Using Maven
```bash
mvn spring-boot:run
```
Access the application at: http://localhost:8080

### Option 2: Using Docker
```bash
docker build -t working-day-calc .
docker run -p 8080:8080 working-day-calc
```
Access the application at: http://localhost:8080

## Testing
Run all tests:
```bash
mvn test
```

## Repository
https://github.com/Helderpm/ai_exec.git
