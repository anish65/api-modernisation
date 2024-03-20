# Transaction REST service

### Table of Contents
- [Introduction](#introduction)
- [Run the application](#run-the-application)
- [API Documentation](#api-documentation)
- [Postgresql DB](#postgresql-db)

## Introduction

This service is responsible for doing fund transaction and provide more real time account balance in zand system.

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
    - `transaction-rest-service`

- Once the services are up & running, then go to service specific Swagger UI to execute the flows as
  described below.

## API Documentation
Note: Make sure application is running.
- Refer: [api-docs](http://localhost:8085/v3/api-docs) for Open API Specification 3.0
- To access Swagger UI [click here](http://localhost:8085/webjars/swagger-ui/index.html)
    - It is a fully working client to test the rest endpoint. So postman is actually not needed.
    - Refer the below sample request & response to test

## Postgresql DB
Follow below step for setting postgresql,
- Step 1: Open the console
- Step 2: Create database with name `txn`
- Step 2: Run the script present in the resource folder [db-schema.sql](./src/main/resources/db-schema.sql)
- Step 2: Configure below values for login
    - UserName
    - Password 
