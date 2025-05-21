# User Management

A web-based **User Management** application built using **Spring Boot**, **Vaadin**, **JasperReports**, and **PostgreSQL**, with PDF export capability.


## Features
- User CRUD with Vaadin
- REST API endpoint to export users as PDF
- Spring Data JPA + PostgreSQL for persistence
- JasperReports for PDF generation
- Docker support for production deployment

## Project Structure
src/
└── main/
├── java/com/eka/user/
│ ├── controller/ # Controller for PDF report
│ ├── model/ # JPA Entity
│ ├── repository/ # Spring Data JPA Repository
│ ├── service/ # Service class for logic PDF report
│ ├── view/ # Vaadin Views (UI)
│ └── Application.java # Spring Boot Main App
└── resources/
├── application.properties
└── report
  └──  user.jrxml #jasper report

## Maven Dependencies Summary

| Dependency                        | Description                                   |
|----------------------------------|------------------------------------------------|
| `vaadin-spring-boot-starter`     | Vaadin UI framework for modern Java web apps   |
| `spring-boot-starter-data-jpa`   | ORM using Hibernate and JPA                    |
| `spring-boot-starter-web`        | Build RESTful APIs with Spring MVC             |
| `jasperreports`                  | PDF report generation with JasperReports       |
| `postgresql`                     | PostgreSQL JDBC Driver                         |
| `spring-boot-starter-test`       | Unit and integration testing support           |

## Setup Intructions
Follow the steps below to build and run the project locally or in a Docker container.

### Prerequisites
- Java 17
- Maven 3.9+
- (Optional) Docker if you prefer containerized setup
- PostgreSQL database instance

### Clone the Repository
- git clone https://gitlab.com/ekadwic10/jasper-report.git

### Configure Database
spring.datasource.url=jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:6543/postgres
spring.datasource.username=postgres.xqwtdaxhybxjmmuvrxdp
spring.datasource.password=7Dr7K7Win2e80gVz
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

### Run
- Using Docker
    # Build the Docker image
    docker build -t user-app .

    # Run the Docker container
    docker run -p 8080:8080 user-app
- Locally
    mvn spring-boot:run


