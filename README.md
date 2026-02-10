# MediTrust API

## Table of Contents
- [About](#about)
- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Built With](#built-with)
- [Authors](#authors)
- [Acknowledgements](#acknowledgements)

## About
MediTrust is a web application for a clinic that allows users to make appointments and rate doctors. 
It includes features like doctor appointment schedules generation, authentication through SMS messages and an admin panel to manage doctors and appointments.

## Getting Started
These instructions will help you set up the project on your local machine for development and testing purposes.

### Prerequisites
Ensure you have the following software installed:
- [Java JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://maven.apache.org/install.html)
- [MySQL](https://dev.mysql.com/downloads/installer/)
- [Docker](https://www.docker.com/get-started)

### Installation
Follow these steps to set up the project:

```bash
# Step 1: Clone the repository
git clone https://github.com/hlibmurphy/MediTrust-API.git

# Step 2: Navigate to the project directory
cd MediTrust-API

# Step 3: Install dependencies using Maven
mvn clean install

# Step 4: Set up environment variables
cp .env.example .env
# Edit .env file to match your configuration (e.g., database connection details)

# Step 5: Build JAR file
mvn clean package

# Step 6: Run the application
docker-compose up
```
### Usage
Once the application runs, you can access it at `http://localhost:8081`. 
For documentation go to `http://localhost:8081/api/swagger-ui/index.html`

You can login as admin using these credentials on `http://localhost:8081/api/auth/login`
```bash
{
    "phone": "0123456789",
    "password": "12345678"
}
```

## Running Tests
To run the automated tests, use the following command:

```
mvn test
```

## Built With
- Spring Boot - Framework
- Hibernate - ORM
- MySQL - Database
- Docker - Containerization
- Mockito - Testing

## Authors
- **Hlib Bykovskyi** - *Initial work* - [hlibmurphy](https://github.com/hlibmurphy)

## Acknowledgements
- Thanks to the Spring Boot and Hibernate communities for their excellent documentation and support.
