# Kafka Stream

### Table of Contents
- [Introduction](#introduction)
- [Run the application](#run-the-application)

## Introduction

Demo purpose, this service is used to make kafka locally up.

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
    - `kafka-stream`

- Once the services are up & running, then go to service specific Swagger UI to execute the flows as
  described below.