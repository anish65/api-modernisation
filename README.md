# Zand system

### Table of Contents
- [Introduction](#introduction)
- [Pre-requisite](#pre-requisite)  
- [Module anatomy](#module-anatomy)
- [Local Run](#local-run)
- [Sequence Diagram](#sequence-diagram)
- [Steps to verify the flow](#steps-to-verify-the-flow)
- [Design Decisions](#design-decisions)
- [Assumptions](#assumptions)
- [In-progress items](#in-progress-items)

## Introduction

Microservices Architecture approach that enhances scalability, integrates with legacy systems, and ensures real-time services.

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
    - Using maven wrapper (maven wrapper doesn't need maven installation locally)
      - Step 1: Open `terminal` and move to the `skiply-system` directory
        - `cd {your-location}/skiply-system` 

      - Step 2: Run each of the below commands on a separate terminal,
        - `./mvnw clean spring-boot:run --file ./kafka-server/pom.xml`  - recommended to start this first
        - `./mvnw clean spring-boot:run --file ./transaction-rest-service/pom.xml`
        - `./mvnw clean spring-boot:run --file ./transaction-proceessor/pom.xml`
        - `./mvnw clean spring-boot:run --file ./legacy-service/pom.xml`

       **(or)**

    - Using IDE as spring boot application,
      - `kafka-server` - recommended to start this first.
      - `transaction-rest-service`
      - `transaction-processor`
      - `legacy-service`

  - Once all the services are up & running, then go to service specific Swagger UI to execute the flows as 
described below.

## Sequence Diagram

```mermaid
sequenceDiagram
```

## Steps to verify the flow

## Design Decisions

## Assumptions

## In-progress items

