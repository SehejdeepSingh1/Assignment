# Device & Shelf Management System

A Spring Boot REST API application for managing devices, shelves, and shelf positions using Neo4j graph database.

## Overview

This is a comprehensive device and shelf management system that allows you to:
- Create, read, update, and delete devices
- Manage shelf inventory
- Track shelf positions and their relationships with devices
- Handle complex relationships between devices and shelves using Neo4j

## Technology Stack

- **Framework**: Spring Boot 4.0.3
- **Language**: Java 25
- **Database**: Neo4j (Graph Database)
- **Build Tool**: Maven
- **Lombok**: For reducing boilerplate code
- **Logging**: SLF4J (via Lombok)
- **Testing**: Spring Boot Test & WebMVC Test

## Project Structure

```
src/
├── main/
│   ├── java/com/example/assignment/
│   │   ├── AssignmentApplication.java          # Main entry point
│   │   ├── config/
│   │   │   └── neo4jConfig.java               # Neo4j configuration
│   │   ├── controller/
│   │   │   ├── DeviceController.java          # Device REST endpoints
│   │   │   ├── ShelfController.java           # Shelf REST endpoints
│   │   │   └── ShelfPositionController.java   # ShelfPosition REST endpoints
│   │   ├── service/
│   │   │   ├── DeviceService.java             # Device business logic
│   │   │   ├── ShelfService.java              # Shelf business logic
│   │   │   └── ShelfPositionService.java      # ShelfPosition business logic
│   │   ├── repository/
│   │   │   ├── DeviceRepository.java          # Device data access
│   │   │   ├── ShelfRepository.java           # Shelf data access
│   │   │   └── ShelfPositionRepository.java   # ShelfPosition data access
│   │   ├── model/
│   │   │   ├── Device.java                    # Device entity
│   │   │   ├── Shelf.java                     # Shelf entity
│   │   │   ├── ShelfPosition.java             # ShelfPosition entity
│   │   │   └── ShelfPositionResponse.java     # ShelfPosition DTO
│   │   └── Exception/
│   │       ├── DeviceNotFoundException.java
│   │       ├── ShelfNotFoundException.java
│   │       └── GlobalExceptionHandler.java    # Centralized error handling
│   └── resources/
│       └── application.properties              # Configuration file
└── test/
    └── java/com/example/assignment/
        ├── controller/                         # Controller tests
        └── service/                            # Service tests
```

## Prerequisites

- Java 25 or higher
- Maven 3.8.0 or higher
- Neo4j database server running (default: bolt://localhost:7687)
- Internet connection (for Maven dependencies)

## Installation & Setup

### 1. Clone or download the project
```bash
cd /workspaces/Assignment
```

### 2. Install dependencies
```bash
mvn clean install
```

### 3. Configure Neo4j Connection
Update `src/main/resources/application.properties` if your Neo4j instance is running on a different host or port:
```bash
# Default configuration
server.port=8080
# Neo4j connection: bolt://localhost:7687
```

Edit `src/main/java/com/example/assignment/config/neo4jConfig.java` to update Neo4j connection details if needed.

### 4. Run the Application

Using Maven:
```bash
mvn spring-boot:run
```

Or build and run the JAR:
```bash
mvn clean package
java -jar target/assignment-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## Data Models

### Device
```java
{
    "id": "string",
    "deviceName": "string",
    "partNumber": "string",
    "buildingName": "string",
    "deviceType": "string",
    "numberOfShelfPositions": "integer",
    "isDeleted": "boolean"
}
```

### Shelf
```java
{
    "id": "string",
    "shelfName": "string",
    "partNumber": "string",
    "isDeleted": "boolean"
}
```

### ShelfPosition
Represents the position/slot of a shelf within a device.

## API Endpoints

### Device Endpoints
- `POST /api/devices/create` - Create a new device
- `GET /api/devices` - Get all devices
- `GET /api/devices/{id}` - Get device by ID
- `PUT /api/devices/{id}` - Update a device
- `DELETE /api/devices/{id}` - Delete a device

### Shelf Endpoints
- `POST /api/shelves/create` - Create a new shelf
- `GET /api/shelves` - Get all shelves
- `GET /api/shelves/{id}` - Get shelf by ID
- `PUT /api/shelves/{id}` - Update a shelf
- `DELETE /api/shelves/{id}` - Delete a shelf

### ShelfPosition Endpoints
- `POST /api/shelf-positions/create` - Create a new shelf position
- `GET /api/shelf-positions` - Get all shelf positions
- `GET /api/shelf-positions/{id}` - Get shelf position by ID
- `PUT /api/shelf-positions/{id}` - Update a shelf position
- `DELETE /api/shelf-positions/{id}` - Delete a shelf position

## Exception Handling

The application includes a global exception handler that manages:
- `DeviceNotFoundException` - Thrown when a device is not found
- `ShelfNotFoundException` - Thrown when a shelf is not found
- Other general exceptions with appropriate HTTP status codes

## Testing

Run the test suite:
```bash
mvn test
```

Test files are located in `src/test/java/com/example/assignment/` and include:
- `DeviceControllerTest.java` - Tests for device endpoints
- `DeviceServiceTest.java` - Tests for device business logic
- `ShelfServiceTest.java` - Tests for shelf business logic
- `ShelfPositionServiceTest.java` - Tests for shelf position business logic

## Configuration Files

### application.properties
```properties
server.port=8080
```

### neo4jConfig.java
Configures the Neo4j driver connection:
- Connection URI: `bolt://localhost:7687`
- Authentication: Configured in the `driver()` bean

## Features

- **RESTful API**: Complete REST API for all entities
- **Neo4j Integration**: Graph database for efficient relationship management
- **Exception Handling**: Centralized error handling with custom exceptions
- **Logging**: Comprehensive logging with SLF4J
- **CORS Support**: Cross-Origin Resource Sharing enabled for all controllers
- **Soft Deletes**: Entities support soft deletion with `isDeleted` flag

## Development Notes

- **Lombok**: Used to reduce boilerplate code (@Data, @RequiredArgsConstructor, @Slf4j)
- **Service Layer**: Business logic is separated into service classes
- **Repository Pattern**: Data access is abstracted through repository interfaces
- **Controller Layer**: REST endpoints are defined with proper request/response handling

## Future Enhancements

- Add database migration scripts
- Implement authentication and authorization
- Add API documentation with Swagger/OpenAPI
- Implement caching strategies
- Add more advanced Neo4j relationship queries
- Implement pagination and sorting for list endpoints

## Troubleshooting

### Neo4j Connection Issues
- Ensure Neo4j is running and accessible at `bolt://localhost:7687`
- Check Neo4j logs for connection errors
- Verify credentials in `neo4jConfig.java`

### Port Already in Use
- Change the port in `application.properties`
- Or kill the process using port 8080

### Build Failures
- Clear Maven cache: `mvn clean`
- Ensure Java 25 is installed: `java -version`
- Update Maven: `mvn -v`

## License

This is a demo project for Spring Boot assignment. Modify and use as needed.

## Support

For issues or questions, refer to:
- Spring Boot Documentation: https://spring.io/projects/spring-boot
- Neo4j Documentation: https://neo4j.com/docs/
- Maven Documentation: https://maven.apache.org/
