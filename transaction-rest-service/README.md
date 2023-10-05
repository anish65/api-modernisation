# Transaction rest microservice

### Table of Contents
- [Introduction](#introduction)
- [Run the application](#run-the-application)
- [API Documentation](#api-documentation)
- [Sample request & response](#sample-request--response)
- [Postgresql DB Client Web Console](#h2-db-client-web-console)

## Introduction

This service is responsible for doing fund transfer and provide account balance in zand system.

## Run the application
Please refer the parent [README.md](../README.md#local-run) file on how to run the services.

## API Documentation
Note: Make sure application is running.
- Refer: [api-docs](http://localhost:8082/v3/api-docs) for Open API Specification 3.0
- To access Swagger UI [click here](http://localhost:8085/webjars/swagger-ui/index.html)
    - It is a fully working client to rest the rest endpoint. So postman is actually not needed.
    - Refer the below sample request & response to test

## Sample request & response
- Fund transfer
    - Request
        - Endpoint
          ```
          POST  http://localhost:8085/v1/transactions/fundTransfer
          ```

        - Headers
          ```
          accept: application/json
          Content-Type: application/json
          ```

        - Payload
          ```json
          {
            "cifId" : "123",
            "fromAccountId" : "0123456789",
            "toAccountId" : "0123456787",
            "currency" : "AED",
            "amount" : 1.00,
            "description" : "testing"
          }
           ```
        - Response
            - HTTP code  `200`

            - Payload
              ```json
              {
                "referenceNo": "1425043046",
                "status": "SUCCESS"
              }
              ```
- Account balance Request
    - Endpoint
      ```
      POST  http://localhost:8085/v1/accounts/0123456789/balance
      ```

- Response
    - HTTP code  `200`

    - Payload
      ```json
      {
        "cifId": "123",
        "accountId": "0123456789",
        "balance": 89.00,
        "currency": "AED"
      }
      ```

## Postgresql DB 
Note: Make sure application is running.
  - Step 1: Open the console
  - Step 2: Create database with name `txn`
  - Step 2: Run the script present in the resource folder [db-schema.sql](./src/main/resources/db-schema.sql)
  - Step 2: Configure below values for login
    - User Name
    - Password 
