# booking-service

Microservice acts as a room occupancy manager. Project built under time constraints from interview process.

## Description

Our hotel clients have two different categories of rooms:

- Premium
- Economy

Requirements:

1. If a customer willing to pay 100 EUR or more - book for him a Premium room.
2. If a customer willing to pay below 100 EUR - book for him an Economy room.
3. If all Economy rooms are occupied then move Economy customer into Premium room if any is empty. The highest paying
   customers below EUR 100 will get preference for the “upgrade”.

## Build with

- Java 11
- MySQL
- Docker
- Makefile

## Prerequisites

Make sure you have installed and configured tools listed below:

1. Docker
2. Make

## How to run application

In your CLI type:

```shell
make run
```

## How to test application

In your CLI type:

```shell
make test
```

## Api docs

Api exposes Swagger docs that are accessible at [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/).

## Things to improve if there was more time

Below I'm writing down a list of things to do if there was more time for that task. This list is not complete and
describes only general things that should be improved at the beginning.

1. Introduce dto layer to do not pass database entities directly to user.
2. Add more tests unit, integration and e2e tests.
3. Add to project Liquibase to manage database schema versioning instead of Hibernate mechanism.
4. Review and adjust current domain objects (maybe they need some additional properties).
5. Add more custom exceptions and automatic mapping from exception to readable error message for API consumers.
6. Verify if parallel requests are handled in a proper way.

# Contributing

Contribution is disabled and the project will not be developed in the future.

