# Transaction Processor

### Table of Contents
- [Introduction](#introduction)
- [Run the application](#run-the-application)
- [API Documentation](#api-documentation)
- [Dependent Components](#dependent-components)
- [Redis](#Redis)

## Introduction

This service is responsible for reading the messages from the kafka and calling the core banking service with provided TPS.

## Run Application

### Pre-requisite

Please make sure your machine has the following things installed,
- IDE: IntelliJ IDEA (preferred)
    - `Java 17` (can be installed easily using IDE if you have one)
    - `Maven 3.8.*` (again comes bundled with most of the IDE)
    - Select maven's `clean install` goals from maven section of the IDE

  (or)

- If you prefer terminal, you can use `mvn` comes with this repository itself
    - `./mvn clean install` from the root of the directory to build all the modules.

### Local Run
- Run below commands on a separate terminal,
    - `./mvn clean spring-boot:run`

      **(or)**

- Using IDE as spring boot application,
    - `transaction-processor`

- Once the services are up & running, then go to service specific Swagger UI to execute the flows as
  described below.

## Dependent Components
- Kafka server
- legacy-service

## Redis
Follow below step for setting redis,
- Step 1: Install redis in local 
- Step 2: Make the redis application locally up in port 6379