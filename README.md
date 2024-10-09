![GitHub CI](https://github.com/muratcanyeldan/PetVaccineReminderApplication/actions/workflows/maven.yml/badge.svg)

[![Linkedin](https://i.sstatic.net/gVE0j.png) LinkedIn](https://www.linkedin.com/in/muratcanyeldan/)
&nbsp;
[![GitHub](https://i.sstatic.net/tskMh.png) GitHub](https://github.com/muratcanyeldan) 

# Pet Vaccine Reminder Application

This application is designed to help users manage their pets' vaccine schedules. It integrates with Keycloak for authentication, Apache Kafka for message-driven architecture, and uses PostgreSQL for persistence.

## Features

- **User Registration/Login**: Secured via Keycloak (OAuth2)
- **Pet Management**: Users can add and manage their pets
- **Vaccine Reminders**: Scheduled reminders for pet vaccines using Kafka
- **Telegram Bot**: Notifications via a Telegram bot
- **Docker**: Fully containerized with Docker and Docker Compose

## Tech Stack

- **Java 21** with **Spring Boot 3**
- **Keycloak** for authentication
- **Kafka** for messaging
- **PostgreSQL** as the database
- **Docker** for containerization

## Prerequisites

- **Docker** and **Docker Compose**
- **JDK 21**

## Setup & Run

1. **Clone the repository**:
    ```bash
    git clone https://github.com/muratcanyeldan/PetVaccineReminderApplication
    cd pet-vaccine-reminder
    ```

2. **Build the project**:
    - This step is optional if you want to run with Docker you can skip this step.
    - Ensure the `application.yml` is correctly configured for Keycloak, Kafka and PostgreSQL.
    ```bash
    mvn clean install
    ```

3. **Run with Docker**:
    - Ensure the `application-docker.yml` is correctly configured for Keycloak, Kafka and PostgreSQL.
    - Ensure you update PostgreSQL password and username in `docker-compose.yml`.
    ```bash
    docker-compose up -d --build
    ```

4. **Access the Application**:
    - **Swagger UI**: [http://localhost:8085/swagger-ui/index.html](http://localhost:8085/swagger-ui/index.html)
    - **Keycloak**: [http://localhost:8080](http://localhost:8080)

