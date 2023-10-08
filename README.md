# Zand system

### Table of Contents
- [Introduction](#introduction)
- [Block-diagram](#block-diagram)
- [Sequence Diagram](#sequence-diagram)
- [Pre-requisite](#pre-requisite)  
- [Module anatomy](#module-anatomy)
- [Local Run](#local-run)
- [Steps to verify the flow](#steps-to-verify-the-flow)
- [Design Decisions](#design-decisions)
- [Assumptions](#assumptions)
- [In-progress items](#in-progress-items)

## Introduction

Microservices Architecture approach of a scalable REST API for a banking
system that handles debit and credit transactions for individual accounts. The goal is to
create a system that efficiently manages a large volume of requests in parallel without
overwhelming an existing legacy system with limited throughput capabilities. Key concepts
employed in this solution include:
- Microservices: Breaking down the system into smaller, independent services.
- CQRS (Command Query Responsibility Segregation): Separation of write
(transaction) and read (balance) operations.
- Kafka: Facilitating communication between microservices.
- Event-Driven Architecture: Microservices communicate via events for scalability
and maintainability.

## Block-diagram

![Link Name](./doc/Block-diagram.png)

## Sequence Diagram

![Link Name](./doc/ScalableBankingApi-highlevel.png)

## Pre-requisite

Please make sure your machine has the following things installed,
- IDE: IntelliJ IDEA (preferred)
  - `Java 17` (can be installed easily using IDE if you have one)
  - `Maven 3.8.*` (again comes bundled with most of the IDE)
  - Checkout this project as whole
    - It is a multi-module mono-repo consists of all the required modules together.
  - Select maven's `clean install` goals from maven section of the IDE
  
  (or)

- If you prefer terminal, you can use `mvnw` (maven wrapper) comes with this repository itself
  - `./mvnw clean install` from the root of the directory to build all the modules.

## Module anatomy
  - All the services have a `common` maven module dependency.
  - Service modules are suffixed with `**-service` & `**-server`
    - can be run individually
  - `kafka-server` module is a `Embedded Kafka` setup using spring-boot. And it is good enough for this demo app.
    - All the required kafka `topics` are created automatically when it is up.
    - It is visible only on `localhost` and not configured for remote execution.
    - Message serialization: `Json` serializer is used for messages. Due to demo app, we haven't gone with `Avro` format
    & `schema-registry`.
 
## Local Run 
Steps to locally run the required applications,
  - Please run the below services,
    - Using maven
      - Step 1: Open `terminal` and move to the `zand-fund-transfer` directory
        - `cd {your-location}/zand-fund-transfer` 
      - Step 2: Build the `common-messaging` 
        - `./mvn clean install --file ./common-mesaging/pom.xml`

      - Step 2: Run each of the below commands on a separate terminal,
        - `./mvn clean spring-boot:run --file ./kafka-server/pom.xml`  - recommended to start this first
        - `./mvn clean spring-boot:run --file ./transaction-rest-service/pom.xml`
        - `./mvn clean spring-boot:run --file ./transaction-proceessor/pom.xml`
        - `./mvn clean spring-boot:run --file ./legacy-service/pom.xml`

       **(or)**

    - Using IDE as spring boot application,
      - `common-messaging` - Build this repo first
      - `kafka-server` - recommended to start this first.
      - `transaction-rest-service`
      - `transaction-processor`
      - `legacy-service`

  - Once all the services are up & running, then go to service specific Swagger UI to execute the flows as 
described below.

## Steps to verify the flow

## Design Decisions

## Assumptions

## In-progress items
- unit

