# Spring Boot and GraphQL POC

This project is a proof of concept using Java 17, Spring Boot, and GraphQL. Below you'll find details on the project
structure, how to run the application, and how to execute tests.

## Project Structure

The project is organized according to Clean Architecture principles and is divided into the following packages:

- **config**: Contains the project configuration.
- **domain**: Contains the domain objects and core entities.
- **dto**: Contains Data Transfer Objects (DTOs) used for input and output operations.
- **entity**: Contains JPA entities that are mapped to the database.
- **exception**: Contains custom exceptions and a custom GraphQL exception handler.
- **mapper**: Contains mappers to perform the conversion between domain objects and core entities.
- **repository**: Contains the interfaces for data access.
- **resolver**: Contains GraphQL resolvers that define how queries and mutations are resolved.
- **service**: Contains business services that implement the application's logic.

## Prerequisites

Before you begin, ensure you have the following components installed:

- **Java 17**: The required version of Java to compile and run the project.
- **Maven Wrapper**: Included in the project to handle dependencies and build tasks.

## Building the project

To build the project, use the command:

```bash
   ./mvnw clean install
   ```

This command will download all necessary dependencies, compile the code, and package the microservice into a JAR file.

## Running the project

To run the microservice locally, you can use the following command:

```bash
   ./mvnw spring-boot:run
   ```

## Testing

The project includes a comprehensive test suite that covers:

- **Unit Tests**: Validating the functionality of the services.
- **Integration Tests**: Testing the interactions between resolvers and the different layers of the project.

You can run the project's unit and integration tests with the the following command:

```bash
   ./mvnw test
   ```

## License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).

