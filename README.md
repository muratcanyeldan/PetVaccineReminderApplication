![GitHub CI](https://github.com/muratcanyeldan/PetVaccineReminderApplication/actions/workflows/maven.yml/badge.svg)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

![LinkedIn](https://img.shields.io/badge/linkedin-%230077B5.svg?style=for-the-badge&logo=linkedin&logoColor=white)(https://www.linkedin.com/in/muratcanyeldan/)
&nbsp;
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)(https://github.com/muratcanyeldan) 
&nbsp;

# My Related Articles on Medium

[![Medium](https://img.shields.io/badge/Medium-12100E?style=for-the-badge&logo=medium&logoColor=white) How to Remote Debug a Containerized Java Application with IntelliJ IDEA](https://muratcanyeldan.com/how-to-remote-debug-a-containerized-java-application-with-intellij-idea-45cd9de7ee9a)


[![Medium](https://img.shields.io/badge/Medium-12100E?style=for-the-badge&logo=medium&logoColor=white) Implementing Kafka and Dead Letter Queue in Java](https://medium.com/@muratcanyeldan/implementing-kafka-and-dead-letter-queue-in-java-f0938276f217)


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

