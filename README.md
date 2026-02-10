# Working Day Calculator (Clean Architecture)

A Spring Boot application demonstrating SOLID principles and clean architecture to calculate working days (Monday-Friday) between two dates, excluding weekends and public holidays.

## Architecture Overview

### UML Class Diagram

```mermaid
classDiagram
    %% Domain Layer
    class WorkingDayService {
        +calculateWorkingDays(LocalDate, LocalDate, List~Holiday~) int
    }
    
    class WorkingDayRequestValidator {
        -CountryRepository countryRepository
        +validateDateRange(LocalDate, LocalDate) WorkingDayError
        +validateCountry(String, Country) WorkingDayError
        +validate(LocalDate, LocalDate, String) ValidationResult
    }
    
    class Country {
        -String code
        -String name
        +code() String
        +name() String
    }
    
    class WorkingDayResult {
        -int workingDays
        -LocalDate startDate
        -LocalDate endDate
        -Country country
        -List~Country~ allCountries
        -String selectedCountryCode
        +workingDays() int
        +startDate() LocalDate
        +endDate() LocalDate
        +country() Country
        +allCountries() List~Country~
        +selectedCountryCode() String
    }
    
    class WorkingDayError {
        <<enumeration>>
        INVALID_DATE_RANGE
        DATE_BEFORE_MINIMUM
        INVALID_COUNTRY
        +code() String
        +message() String
    }
    
    class ValidationResult {
        -boolean valid
        -WorkingDayError error
        -Country country
        +valid() boolean
        +error() WorkingDayError
        +country() Country
    }
    
    class WorkingDayException {
        +WorkingDayException(String)
        +WorkingDayException(String, Throwable)
    }
    
    %% Domain Ports
    class CountryRepository {
        <<interface>>
        +getAllCountries() List~Country~
        +findByCode(String) Country
    }
    
    class WorkingDayCalculator {
        <<interface>>
        +calculateWorkingDays(LocalDate, LocalDate, List~Holiday~) int
    }
    
    class RequestValidator {
        <<interface>>
        +validate(LocalDate, LocalDate, String) ValidationResult
    }
    
    %% Application Layer
    class WorkingDayApplicationService {
        -WorkingDayCalculator workingDayCalculator
        -RequestValidator requestValidator
        -CountryRepository countryRepository
        +validateRequest(LocalDate, LocalDate, String) ValidationResult
        +calculateWorkingDays(LocalDate, LocalDate, String, Country) WorkingDayResult
        +getAllCountries() List~Country~
    }
    
    %% Infrastructure Layer
    class WorkingDayController {
        -WorkingDayApplicationService applicationService
        +index(Model) String
        +calculate(LocalDate, LocalDate, String, Model, RedirectAttributes) String
    }
    
    class CountryService {
        -List~Country~ countries
        -ObjectMapper objectMapper
        +loadCountries() void
        +getAllCountries() List~Country~
        +findByCode(String) Country
    }
    
    %% Relationships
    WorkingDayService ..|> WorkingDayCalculator
    WorkingDayRequestValidator ..|> RequestValidator
    CountryService ..|> CountryRepository
    
    WorkingDayApplicationService --> WorkingDayCalculator
    WorkingDayApplicationService --> RequestValidator
    WorkingDayApplicationService --> CountryRepository
    
    WorkingDayController --> WorkingDayApplicationService
    
    WorkingDayRequestValidator --> CountryRepository
    WorkingDayRequestValidator --> ValidationResult
    WorkingDayRequestValidator --> WorkingDayError
    
    ValidationResult --> WorkingDayError
    ValidationResult --> Country
    
    WorkingDayApplicationService --> WorkingDayResult
    WorkingDayApplicationService --> ValidationResult
    
    CountryService --> Country
    
    WorkingDayException --|> RuntimeException
    
    note for WorkingDayApplicationService "Application Service\nOrchestrates domain services"
    note for WorkingDayController "Infrastructure Adapter\nWeb layer"
    note for CountryService "Infrastructure Adapter\nData access"
    note for WorkingDayService "Domain Service\nBusiness logic"
    note for WorkingDayRequestValidator "Domain Service\nValidation logic"
```

### Request Flow Sequence Diagram

```mermaid
sequenceDiagram
    participant User
    participant Controller as WorkingDayController
    participant AppService as WorkingDayApplicationService
    participant Validator as WorkingDayRequestValidator
    participant Calculator as WorkingDayService
    participant CountryAdapter as CountryService
    participant JollyDay as JollyDay Library
    
    User->>Controller: GET /calculate?start=2023-10-01&end=2023-10-07&country=DE
    Controller->>AppService: validateRequest(start, end, country)
    AppService->>Validator: validate(start, end, country)
    Validator->>CountryAdapter: findByCode("DE")
    CountryAdapter-->>Validator: Country("DE", "Germany")
    Validator->>Validator: validateDateRange(start, end)
    Validator-->>AppService: ValidationResult(valid=true, country=Germany)
    AppService-->>Controller: ValidationResult(valid=true, country=Germany)
    
    Controller->>AppService: calculateWorkingDays(start, end, country, germany)
    AppService->>Calculator: calculateWorkingDays(start, end, holidays)
    Calculator->>JollyDay: getHolidays(start, end, "DE")
    JollyDay-->>Calculator: List<Holiday>
    Calculator->>Calculator: countWeekdays(start, end, holidays)
    Calculator-->>AppService: 5 working days
    AppService->>AppService: buildResult(5, start, end, germany)
    AppService-->>Controller: WorkingDayResult(workingDays=5, ...)
    Controller-->>User: HTML page with result
    
    Note over User, JollyDay: Success flow with valid input
```

### Error Handling Flow

```mermaid
sequenceDiagram
    participant User
    participant Controller as WorkingDayController
    participant AppService as WorkingDayApplicationService
    participant Validator as WorkingDayRequestValidator
    
    User->>Controller: GET /calculate?start=2023-10-10&end=2023-10-05&country=DE
    Controller->>AppService: validateRequest(start, end, country)
    AppService->>Validator: validate(start, end, country)
    Validator->>Validator: validateDateRange(start, end)
    Note over Validator: Start date is after end date!
    Validator-->>AppService: ValidationResult(valid=false, error=DATE_001)
    AppService-->>Controller: ValidationResult(valid=false, error=DATE_001)
    Controller->>Controller: addFlashAttribute("error", "Start date cannot be after end date")
    Controller->>Controller: addFlashAttribute("errorCode", "DATE_001")
    Controller-->>User: redirect:/ with error messages
    
    Note over User, Validator: Error flow with invalid date range
```

### Component Diagram

```mermaid
graph TB
    %% External Actors
    User[User/Browser]
    
    %% Infrastructure Layer
    subgraph "Infrastructure Layer"
        Controller[WorkingDayController<br/>Spring MVC]
        CountryAdapter[CountryService<br/>JSON Adapter]
    end
    
    %% Application Layer
    subgraph "Application Layer"
        AppService[WorkingDayApplicationService<br/>Orchestration]
    end
    
    %% Domain Layer
    subgraph "Domain Layer"
        Calculator[WorkingDayService<br/>Business Logic]
        Validator[WorkingDayRequestValidator<br/>Validation Logic]
        
        %% Domain Models
        Models[Domain Models<br/>Country, WorkingDayResult<br/>ValidationResult, WorkingDayError]
        Exceptions[Domain Exceptions<br/>WorkingDayException]
    end
    
    %% External Systems
    subgraph "External Systems"
        JSON[EU Countries JSON]
        JollyDay[JollyDay Library<br/>Holiday Data]
    end
    
    %% Data Flow
    User --> Controller
    Controller --> AppService
    AppService --> Calculator
    AppService --> Validator
    Validator --> Models
    Calculator --> Models
    AppService --> CountryAdapter
    CountryAdapter --> JSON
    Calculator --> JollyDay
    AppService --> Controller
    Controller --> User
    
    %% Style
    style User fill:#e1f5fe
    style Controller fill:#f3e5f5
    style CountryAdapter fill:#f3e5f5
    style AppService fill:#e8f5e8
    style Calculator fill:#fff3e0
    style Validator fill:#fff3e0
    style Models fill:#fce4ec
    style Exceptions fill:#fce4ec
    style JSON fill:#f1f8e9
    style JollyDay fill:#f1f8e9
```

### Hexagonal Architecture (Ports and Adapters)
- **Domain Layer**: Core business logic with use cases and domain models
- **Application Layer**: Orchestrates domain use cases and provides application services
- **Infrastructure Layer**: Adapters for external systems (web, data persistence)
- **Domain Ports**: Interfaces defining contracts for infrastructure

### Package Structure
```
com.example.calculator/
├── domain/
│   ├── model/           # Domain models (Country, WorkingDayResult, WorkingDayError, ValidationResult)
│   ├── port/            # Domain ports (interfaces)
│   │   ├── CountryRepository
│   │   ├── WorkingDayCalculator
│   │   └── RequestValidator
│   ├── exception/        # Domain exceptions
│   └── usecase/          # Business use cases
│       ├── WorkingDayService
│       └── WorkingDayRequestValidator
├── application/
│   └── service/         # Application services (orchestration)
│       └── WorkingDayApplicationService
└── infrastructure/
    ├── adapter/           # Infrastructure adapters
    │   └── CountryService
    └── web/              # Web layer
        └── WorkingDayController
```

## SOLID Compliance

- **SRP**: Each class has a single responsibility
  - `WorkingDayService`: Core calculation logic
  - `WorkingDayRequestValidator`: Input validation
  - `WorkingDayApplicationService`: Orchestration of domain services
  - `WorkingDayController`: HTTP request/response handling
  - `CountryService`: Data access abstraction

- **OCP**: Open for extension without modification
  - New calculation methods can be added to `WorkingDayCalculator` interface
  - New validation rules can extend `RequestValidator` interface
  - New data sources can implement `CountryRepository` interface

- **LSP**: Subtypes can replace base types
  - `CountryServiceAdapter` implements `CountryRepository` interface
  - Custom exceptions extend `RuntimeException`

- **ISP**: Clients depend on interfaces they use
  - Controller depends on `WorkingDayApplicationService` interface
  - Application service depends on domain ports, not concrete implementations

- **DIP**: Depends on abstractions, not concretions
  - Application service depends on `WorkingDayCalculator` and `CountryRepository` interfaces
  - Controller depends on application service interface

## API Specification

### Endpoints
- `GET /`: Returns HTML form for date input with country selection
- `GET /calculate?start=YYYY-MM-DD&end=YYYY-MM-DD&country=XX`: 
  - Validates input parameters
  - Calculates working days excluding weekends and holidays
  - Returns result page with calculation details

### Response Format
- **Success**: HTML page with calculation result, input dates, and country info
- **Validation Errors**: Redirect with flash attributes:
  - `error`: Human-readable error message
  - `errorCode`: Structured error code (e.g., "DATE_001", "COUNTRY_001")

## Features

### Core Functionality
- **Working Day Calculation**: Monday-Friday calculation excluding weekends
- **Holiday Integration**: Uses JollyDay library for public holidays by country
- **Input Validation**: Comprehensive date range and country validation
- **Error Handling**: Structured error codes with user-friendly messages
- **Clean Architecture**: Hexagonal architecture with clear separation of concerns

### Validation Rules
- **Date Range**: Must be between 1900-01-01 and 2100-12-31
- **Date Logic**: Start date cannot be after end date
- **Country Validation**: Must be valid country code from loaded countries
- **Required Fields**: All input parameters are required

### Error Codes
- `DATE_001`: Start date cannot be after end date
- `DATE_002`: Date range must be between 1900-01-01 and 2100-12-31
- `COUNTRY_001`: Invalid country selected

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Java**: 17+
- **Build**: Maven 3.6+
- **Testing**: JUnit 5
- **Templating**: Thymeleaf
- **Holiday Data**: JollyDay library
- **JSON Processing**: Jackson
- **Code Quality**: Lombok for boilerplate reduction

## Deployment Instructions

### Prerequisites
- Docker installed
- Maven 3.6+
- Java 17+

### Option 1: Using Maven
```bash
mvn spring-boot:run
```
Access application at: http://localhost:8080

### Option 2: Using Docker
```bash
docker build -t working-day-calc .
docker run -p 8080:8080 working-day-calc
```
Access application at: http://localhost:8080

## Testing

### Test Coverage
- **Unit Tests**: Domain logic, validation, and application services
- **Integration Tests**: Full web layer testing with MockMvc
- **Adapter Tests**: Infrastructure component testing

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=WorkingDayServiceTest

# Run integration tests only
mvn test -Dtest=WorkingDayControllerIT
```

## Code Quality

### SonarQube Compliance
- No duplicate methods
- No redundant object instantiation
- Clean exception handling
- Proper test assertions
- Meaningful test coverage

### Best Practices Applied
- **Immutable domain objects** with Lombok
- **Builder pattern** for complex object construction
- **Structured error handling** with custom exception types
- **Dependency injection** via constructors
- **Clean separation** of concerns across layers

## Recent Improvements

### v2.0 - Clean Architecture Refactor
- **Hexagonal Architecture**: Implemented ports and adapters pattern
- **Domain Services**: Separated business logic from infrastructure
- **Application Layer**: Added orchestration services
- **Structured Errors**: Implemented error codes with enum
- **Builder Pattern**: Added for complex object construction
- **Enhanced Testing**: Comprehensive test coverage
- **Code Quality**: Removed SonarQube warnings

### v2.1 - Exception Handling Enhancement
- **WorkingDayException**: Custom domain exception
- **WorkingDayError**: Structured error codes
- **ValidationResult**: Type-safe validation result
- **Error Codes**: Consistent error categorization

### v2.2 - UI/UX Improvements
- **Dynamic Result Display**: Result div shows/hides based on calculation state using `th:if`
- **Form Reset**: Reset button clears all fields and hides results
- **Country Selection Persistence**: Selected country preserved after calculation
- **Error Display**: Flash attributes for error messages with structured error codes
- **Thymeleaf Optimization**: Improved conditional rendering for better reliability

## Repository

https://github.com/Helderpm/ai_exec.git

## Contributing

1. Follow SOLID principles
2. Maintain clean architecture boundaries
3. Add comprehensive tests for new features
4. Use structured error handling
5. Keep code SonarQube compliant
