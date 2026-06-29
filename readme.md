# Transport Microservices Project

## Overview
This repository contains a Java Spring Boot microservices system with an API Gateway and 12 backend microservices. Each service runs independently, connects to its own MySQL database, and exposes Swagger documentation.

The root folder includes Windows-friendly launcher scripts for building, database creation, and process management.

## Repository Contents
- `api-gateway/` - Spring Cloud Gateway entry point
- `auth/` - Authentication service
- `city/` - City service
- `client/` - Client service
- `driver/` - Driver service
- `line/` - Line service
- `maintenance/` - Maintenance service
- `manufacturer/` - Manufacturer service
- `review/` - Review service
- `station/` - Station service
- `supervisor/` - Supervisor service
- `ticket/` - Ticket service
- `train/` - Train service
- `typeengine/` - TypeEngine service
- `zAPIStarter.py` - builds services, launches them, and opens browser tabs
- `zCreateAllDatabases.py` - creates MySQL databases and starts the app chain
- `zMenu.py` - CLI menu for starting and stopping the system
- `zTerminateMicroservices.py` - stops running microservices by PID

## Requirements
- Java 17
- Maven 17-compatible runtime or Maven wrapper from each module
- Local MySQL server available at `localhost:3306`
- Python 3.x installed on Windows
- Python package: `pymysql`

> The project was tested against Java 17 and Maven 17. Using earlier Java or Maven versions may cause build or runtime failures.

## Install Python Dependency
Open a terminal and run:

```powershell
pip install pymysql
```

## Local Service Ports
| Service | Folder | Port | Database |
|---|---|---|---|
| API Gateway | `api-gateway` | `7769` | none |
| Manufacturer | `manufacturer` | `7770` | `transport_db_manufacturer` |
| Train | `train` | `7771` | `transport_db_train` |
| TypeEngine | `typeengine` | `7772` | `transport_db_typeengine` |
| Station | `station` | `7773` | `transport_db_station` |
| Line | `line` | `7774` | `transport_db_line` |
| Maintenance | `maintenance` | `7775` | `transport_db_maintenance` |
| Ticket | `ticket` | `7776` | `transport_db_ticket` |
| City | `city` | `7777` | `transport_db_city` |
| Driver | `driver` | `7778` | `transport_db_driver` |
| Client | `client` | `7779` | `transport_db_client` |
| Auth | `auth` | `7780` | `transport_db_auth` |
| Supervisor | `supervisor` | `7781` | `transport_db_supervisor` |
| Review | `review` | `7782` | `transport_db_review` |

## API Gateway Route Mapping
The gateway listens on port `7769` and forwards requests to backend services.

- `/manufacturer/**` -> `http://manufacturer-service:7770`
- `/train/**` -> `http://train-service:7771`
- `/typeengine/**` -> `http://typeengine-service:7772`
- `/station/**` -> `http://localhost:7773` with `StripPrefix=1`
- `/line/**` -> `http://localhost:7774` with `StripPrefix=1`
- `/maintenance/**` -> `http://maintenance-service:7775`
- `/ticket/**` -> `http://ticket-service:7776`
- `/city/**` -> `http://localhost:7777` with `StripPrefix=1`
- `/driver/**` -> `http://driver-service:7778`
- `/client/**` -> `http://localhost:7779` with `StripPrefix=1`
- `/auth/**` -> `http://localhost:7780` with `StripPrefix=1`
- `/supervisor/**` -> `http://localhost:7781` with `StripPrefix=1`
- `/review/**` -> `http://review-service:7782`

## Database Setup
Each service uses a MySQL database named:

- `transport_db_<service_name>`

The launcher script automatically creates these databases if they do not already exist.

### Environment variables supported by `zCreateAllDatabases.py`
- `DB_HOST` (default: `localhost`)
- `DB_PORT` (default: `3306`)
- `DB_USER` (default: `root`)
- `DB_PASSWORD` (default: empty)

## Starting the System
From the project root, run:

```powershell
python zMenu.py
```

Then select:
- `1` to start all services via `zAPIStarter.py` and `zCreateAllDatabases.py`
- `2` to stop all running services via `zTerminateMicroservices.py`
- `3` to exit the menu

### What the startup script does
- Detects service folders by checking for `pom.xml` or Gradle files
- Creates required MySQL databases using `pymysql`
- Builds each service with Maven (`mvnw.cmd` on Windows when available)
- Starts each service as a Java process
- Writes service logs to `zzLogs/`
- Opens the API gateway and each service Swagger UI in the default browser

## Stopping the System
Run the menu and choose option `2`, or run directly:

```powershell
python zTerminateMicroservices.py
```

This script reads `microservices.pid` and terminates each process recorded there.

## Swagger Documentation
Each microservice exposes Swagger UI at:

```text
http://localhost:<service-port>/doc/swagger-ui.html
```

The launcher script also opens these documentation pages automatically after startup.

## Troubleshooting
- If service startup fails, inspect logs in `zzLogs/<service>.log`.
- If database creation fails, verify MySQL is running and credentials are correct.
- If a port is already in use, stop the conflicting process or update `server.port` in the service’s `application.properties`.
- If `pymysql` is missing, install it with `pip install pymysql`.
- If `python zMenu.py` does not work, run `python zCreateAllDatabases.py` directly to see the error.

## Notes
- The gateway configuration uses a mix of `localhost` and service host names like `manufacturer-service` and `review-service`.
- Default datasource credentials across services are `root` with an empty password.
- Swagger and Liquibase are enabled across almost every microservice.

## Credits
- `naa-chi`
  - Built `typeengine`, `train`, `maintenance`, `manufacturer`, `ticket`, `review`
  - Added Swagger and Liquibase to every microservice
  - Fixed exception handling
- `bartolomeo123` / `bartolomeo123-bot`
  - Built `line`, `city`, `client`, `driver`, `station`, `supervisor`
  - Implemented authentication and modernized exception/update methods

## Important
Always use Java 17 and Maven 17-compatible tooling for this project. Different versions may prevent the services from building or running correctly.
