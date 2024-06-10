# Authentication service

## About this microservice 
- Custom JWT generation and validation to allow access to endpoints (secured by path and/or role) 
- API REST with resilence library for Retry, RateLimiter and Bulkhead if service is down.
- use of optionals.
- custom exceptions.
- Logging
- hexagonal architechture.
- SOLID Principles.

## Microservice Features
### Account Management
- Creating account

### Authentication
- Login (returns JWT Token)
- Reset password (secured, requires a valid JWT)
- Invalidate token to avoid usage of tokens from specific users

### ADMIN-ONLY operations
- Change other user's role (secured, requires a valid JWT and the token's authority to be ADMIN)
- Get all data (secured, requires a valid JWT and the token's authority to be ADMIN)


## Microservice from project:
### https://github.com/JosepeDevs/PcTallerProject
