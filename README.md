# Car Rental Web Application - Back-end Project
### New Security Configuarion was applied

This application was created to demonstrate a fully fledged full-stack application built with Spring boot + Spring Security including CRUD operations, authentication, pagination, and more.

## Technology Used
* Java Version: 11
* Spring Boot Version: 2.7.8
* jjwt Version: 0.9.1
* mapstruct Version: 1.5.2

# Layers

This is a spring boot monolithic application, it includes below layers

1. `config` contains Security Configuration and Swagger Configuration
2. `domain` contains Entity object classes
3. `repository` communicates between database and services
4. `service` contains business logic and communicating with controller, respository and other services
5. `controller` contains Rest APIs and communicating with service layers
6. `dto` used to transfer data between layers and between client
7. `security` central security system for authentication and authorization
8. `exception` contains central and local exception handling classes

# Security

Integration with Spring Security and add other filter for jwt token process.
The secret key is stored in `application.yml`.<br />
__Super Admin__ : System automatically creates super admin account on startup

# Database

It uses a PostgreSQL database (cloud database is used as default so you do not need to set local database ), can be changed easily in the `application.yml` for any other database.


# Run test

The application contains unit test cases to cover both api test and service test.

# API Documentation
_OpenAPI was used to generate REST APIs Document_<br/>
[-- Swagger Document Link - -]()


# Contact

Should you need any further information, please do not hesitate to contact the developer of this app.


