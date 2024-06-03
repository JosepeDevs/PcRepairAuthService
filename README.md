# Authentication service

## Microservice Features
### Account Management
- Creating account

### Authentication
- Login (returns JWT Token)
- Reset password (secured, requires a valid JWT)

### ADMIN-ONLY operations
- Change other user's role (secured, requires a valid JWT and the token's authority to be ADMIN)
- Get all data (secured, requires a valid JWT and the token's authority to be ADMIN)

## Microservice from project:
### https://github.com/JosepeDevs/PcTallerProject
