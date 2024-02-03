# Driver Onboarding Application

Welcome to the Driver Onboarding Application! This application is designed to streamline the onboarding process for new drivers joining our ride-sharing service.

## Table of Contents

- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Build](#build)
- [Design](#design)
- [Usage](#usage)


## Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

- Java 21 or later
- Spring Boot
- Docker
- Maven or Gradle (for building and running the application)

### Build

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/driver-onboarding-app.git
    ```
2. Navigate to the project directory:

    ```bash
    cd driver-onboarding-app
    ```
3. Build the project:

```bash

./mvnw clean install
```

### Local Setup

1. Run postgres container locally

```bash
 sudo podman run -v ./src/main/resources/init.sql:/docker-entrypoint-initdb.d/data.sql 
                   -v /var/lib/postgres-container/:/var/lib/postgresql/data
                   -p 5432:5432  -e POSTGRES_PASSWORD=password -d postgres
```

2. Run the following command to start the application

```bash
mvn spring-boot:run
```


## Design

1. Process Flow
![Process flow](./docs/Process%20Diagram.png)

