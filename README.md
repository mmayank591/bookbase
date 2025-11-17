This project is a modern, high-performance Library Management System built on a Microservices Architecture, utilizing Spring Boot for scalable backend services and React for a responsive user interface. This distributed approach ensures the system is resilient, horizontally scalable, and highly maintainable for core library operations.
Key Features
Microservices Architecture: Core functionalities are decoupled into independent services:

User-Service: Manages user registration, profiles, and authentication/authorization.

Book-Service: Manages the entire book catalog, inventory, and search functionality.

Borrowing-Service: Handles the transactional logic for checkouts, returns, due dates, and fine calculations.

Service Orchestration: Uses Spring Cloud components (Eureka for Discovery, Gateway for Routing).

Role-Based Access Control (RBAC): Secure access via Spring Security and JWT tokens across all services.

Responsive Frontend: Built with React for a smooth, single-page application experience for librarians and patrons.

Persistent Storage: Dedicated database instances for individual services (e.g., PostgreSQL).
Component,Technology,Purpose
Microservices,Spring Boot 3 (Java),"Build individual, high-performance services."
Service Discovery,Netflix Eureka,Dynamic service registration and lookup.
API Gateway,Spring Cloud Gateway,"Centralized routing, load balancing, and cross-cutting concerns (Auth)."
Security,Spring Security + JWT,Token-based authentication and service-to-service security.
Data Access,Spring Data JPA (Hibernate),Simplifies database operations for each service.
Database,PostgreSQL/MySQL,Production-grade persistent data storage.

Component,Technology,Purpose
Frontend Framework,React,Component-based architecture for the UI.
Styling,Material UI (MUI) / Bootstrap,"Provides a professional, responsive design."
Routing,React Router DOM,Handles client-side navigation.
API Calls,Axios,HTTP client for interacting with the API Gateway.


Getting Started
Follow these steps to set up and run the entire distributed system locally.

Prerequisites
You must have the following installed:

Java Development Kit (JDK 17+ / 21 recommended)

Maven (or Gradle)

Node.js (LTS) and npm

Docker and Docker Compose (Highly recommended for infrastructure services)

1. Database and Infrastructure Setup (Recommended via Docker)
This step assumes you have a docker-compose.yml file to start your databases and other key infrastructure (like Eureka).

Bash

# Start your databases and Eureka/Config servers
docker-compose up -d
2. Backend Services Setup (Spring Boot)
Navigate to the root directory of the project.

Review and update the database connection properties in the application.yml (or .properties) files within each service folder (user-service, book-service, borrowing-service, eureka-server, api-gateway).

Build and run all services. You must start the Eureka Server and API Gateway first.

Bash

# Start the Discovery Server
cd eureka-server
./mvnw spring-boot:run &

# Start the API Gateway
cd ../api-gateway
./mvnw spring-boot:run &

# Start the Core Microservices
cd ../user-service
./mvnw spring-boot:run &

cd ../book-service
./mvnw spring-boot:run &

cd ../borrowing-service
./mvnw spring-boot:run &
The services should be running on their respective ports (e.g., 8081, 8082, 8083, etc.) and registered with Eureka.

3. Frontend Setup (React)
Navigate to the frontend directory:

Bash

cd frontend-ui
Install the required dependencies:

Bash

npm install
Start the React application:

Bash

npm start
The frontend application should now be accessible in your browser, typically at http://localhost:3000, communicating with the API Gateway (usually on http://localhost:8080).

Feel free to fork the repository, submit pull requests, or open issues if you find any bugs or have suggestions for new features!




