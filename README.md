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
   docker build -t working-day-calc .# ai_exec
