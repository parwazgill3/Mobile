# User Analytics Service

## Overview

The User Analytics Service is a Spring Boot-based microservice that manages user information, billing cycles, and daily usage data. The service leverages MongoDB for data storage and is containerized using Docker for easy deployment.

## Features

- User Management: Create and update user profiles.
- Cycle Management: Retrieve current and historical billing cycles.
- Daily Usage Management: Retrieve daily usage data for the current cycle and historical data.

## Prerequisites

- Java 17
- Maven
- Docker
- MongoDB

## Run the Application
```bash
docker-compose up --build
```

## API Documentation

### User Management

#### Create User

- URL: /users
- Method: POST
- Request: {
  "firstName": "firstName",
  "lastName": "lastName",
  "email": "firstName.lastName@gmail.com",
  "password": "password"}
- Response: {
  "id": "id",
  "firstName": "firstName",
  "lastName": "lastName",
  "email": "firstName.lastName@gmail.com"}

#### Update User

- URL: /users/{id}
- Method: PUT
- Request: {
  "firstName": "firstname",
  "lastName": "lastname",
  "email": "firstname.lastname@gmail.com"}
- Response: {
  "id": "id",
  "firstName": "firstname",
  "lastName": "lastname",
  "email": "firstname.lastname@gmail.com"}

### Cycle Management

#### Get Current Cycle

- URL: /cycles/current
- Method: GET
- Query Parameters: userId, mdn
- Response: {
  "id": "cycle1",
  "startDate": "2024-05-18T00:00:00.000Z",
  "endDate": "2024-06-18T23:59:59.999Z"}

#### Get Cycle History 

- URL: /cycles/history
- Method: GET
- Query Parameters: userId, mdn
- Response: [
  {
    "id": "cycle1",
    "startDate": "2024-05-18T00:00:00.000Z",
    "endDate": "2024-06-18T23:59:59.999Z"
  },
  {
    "id": "cycle2",
    "startDate": "2024-06-18T00:00:00.000Z",
    "endDate": "2024-07-18T23:59:59.999Z"
  }
]

### Daily Usage Management

#### Get Current Cycle Daily Usage

- URL: /usage/current
- Method: GET
- Query Parameters: userId, mdn
- Response:
[
  {
    "usageDate": "2024-06-18T00:00:00.000Z",
    "usedInMb": 100.0
  },
  {
    "usageDate": "2024-06-19T00:00:00.000Z",
    "usedInMb": 200.0
  }
]

#### Get Current Day Usage

- URL: /usage/currentDay
- Method: GET
- Query Parameters: userId, mdn
- Response:
{
  "usageDate": "2024-06-19T00:00:00.000Z",
  "usedInMb": 200.0
}

#### Get Total Data Usage of Current Cycle

- URL: /usage/total/currentCycle
- Method: GET
- Query Parameters: userId, mdn
- Response:
{
  "startDate": "2024-06-18T00:00:00.000Z",
  "endDate": "2024-07-18T23:59:59.999Z",
  "totalUsage": 300.0
}

#### Get Total Data Usage History for User

- URL: /usage/total/history
- Method: GET
- Query Parameters: userId, mdn
- Response:
[
  {
    "startDate": "2024-05-18T00:00:00.000Z",
    "endDate": "2024-06-18T23:59:59.999Z",
    "totalUsage": 300.0
  },
  {
    "startDate": "2024-06-18T00:00:00.000Z",
    "endDate": "2024-07-18T23:59:59.999Z",
    "totalUsage": 300.0
  }
]


## Service Design

### Model Design

#### User Model
The User model contains essential information about the user, including firstName, lastName, email, and password. The password field is hashed.

#### Cycle Model
The Cycle model captures information about the billing cycle, including mdn (the phone number of the customer), startDate, endDate, and userId. The userId field is a foreign key that links the cycle to a specific user.

#### DailyUsage Model
The DailyUsage model contains details about the daily data usage, including mdn, userId, cycleId, usageDate, and usedInMb. Including the cycleId in the DailyUsage model allows for efficient querying of usage data within specific billing cycles, ensuring that we can quickly retrieve and aggregate usage data for any given cycle.

### Validation and Exception Handling

There are comprehensive validation mechanisms to ensure that all inputs are properly validated before being processed. For example, in the User model, we validate that the firstName, lastName, email, and password fields are not empty when creating or updating a user. There is also validation an email already in use. Further, there is handling for bad requests like when query parameters are missing, cycles don't exist and daily usage entries dont exist.

### Assumptions

One key assumption in our design is billing cycles are updated every 30 days and there are new daily usage entries everyday which get updated every 15 mins. This microservice is responsible for getting and aggregating that data.

### Testing and Data Initialization

#### Unit Tests with Mock Data
There are comprehensive unit tests using JUnit and Mockito. These tests ensure that the service methods function correctly in isolation. Mock objects are used to simulate the behavior of dependencies, allowing us to test each method independently.

#### Data Tests with an Embedded MongoDB Server
There are data tests using an embedded MongoDB server. These tests verify the integration of the service with MongoDB, ensuring that database operations perform as expected.

#### Initial Data Inside MongoDB Database
The DataInitializer class is responsible for seeding the database with initial data, while the BaseIntegrationTest class sets up a base configuration for integration tests. This allows us to run integration tests in a controlled environment with consistent data.


## Future Improvements

#### Auditing DB Changes
To keep track of changes in the database, we can use MongoDB Change Streams to listen for any updates, deletions, or insertions. This helps maintain an event-sourcing architecture and provides a way to audit changes. Additionally, integrating libraries like Hibernate Envers can help audit entity changes by tracking who made changes and when.

#### Secret Credential Management
Storing sensitive information, such as database credentials, in environment variables is a good practice to avoid hardcoding them. Using secret management services like AWS Secrets Manager, Azure Key Vault, or HashiCorp Vault provides secure storage, automated rotation, and fine-grained access control for credentials and other secrets.

#### Indexing/Sharding
To enhance query performance, it's important to index frequently queried fields like userId, mdn, and email. Analyzing query patterns and adding composite indexes can further optimize performance. Implementing sharding distributes data across multiple servers, improving scalability and handling large datasets efficiently.

#### Monitoring and Logging
Integrating monitoring tools like Prometheus and Grafana helps track application performance and health. MongoDB Atlas also provides built-in monitoring and optimization features. Centralized logging solutions like ELK (Elasticsearch, Logstash, Kibana) or Graylog ensure structured logs are collected, making debugging and auditing easier.

#### Caching
Using in-memory caching solutions like Redis or Memcached can significantly reduce database load and improve response times by caching frequently accessed data. Spring's caching abstraction allows easy integration of caching into the application, with annotations to manage cache behavior on service methods.

#### Pagination
Implementing pagination for endpoints that return large datasets improves performance and usability. Using Spring Data's Pageable and Page interfaces makes it easy to add pagination to repository queries. For large datasets, cursor-based pagination can be more efficient than offset-based pagination, preventing performance issues.
