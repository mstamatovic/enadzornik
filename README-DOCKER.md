# Enadzornik Microservices - Docker Deployment Guide

## Prerequisites
- Docker Engine 20.10+
- Docker Compose 2.0+

## Project Structure
```
enadzornik/
├── auth-service/
│   ├── Dockerfile
│   └── src/
├── material-service/
│   ├── Dockerfile
│   └── src/
├── file-service/
│   ├── Dockerfile
│   └── src/
├── docker-compose.yml
├── .dockerignore
└── pom.xml
```

## Services Overview

| Service | Port | Description |
|---------|------|-------------|
| auth-service | 8083 | Authentication and authorization service |
| material-service | 8084 | Material management service |
| file-service | 8085 | File upload and storage service |
| mariadb | 3306 | MariaDB database |

## Quick Start

### 1. Build and Start All Services
```bash
cd /workspace/uploads/enadzornik
docker-compose up -d --build
```

### 2. Check Service Status
```bash
docker-compose ps
```

### 3. View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f auth-service
docker-compose logs -f material-service
docker-compose logs -f file-service
docker-compose logs -f mariadb
```

### 4. Stop All Services
```bash
docker-compose down
```

### 5. Stop and Remove Volumes (Clean Start)
```bash
docker-compose down -v
```

## Database Setup

The MariaDB container will automatically create the `enadzornik` database with the following credentials:
- **Database**: enadzornik
- **Username**: enadzornik
- **Password**: enadzornik
- **Root Password**: rootpassword

**Important**: You need to initialize your database schema before the services can work properly. You can:

1. Connect to the database:
```bash
docker exec -it enadzornik-mariadb mysql -u enadzornik -penadzornik enadzornik
```

2. Run your SQL migration scripts or use Flyway/Liquibase

## Service Health Checks

Each service has health checks configured:
- MariaDB: Checks if the database is ready to accept connections
- Spring Boot services: Wait for MariaDB to be healthy before starting

## Networking

All services are connected via a custom bridge network `enadzornik-network`. Services can communicate with each other using their service names:
- `mariadb:3306`
- `auth-service:8083`
- `material-service:8084`
- `file-service:8085`

## Persistent Storage

Two volumes are created for data persistence:
- `mariadb_data`: Stores database files
- `file_uploads`: Stores uploaded files from file-service

## Environment Variables

You can customize the following environment variables in `docker-compose.yml`:

### Database
- `MYSQL_ROOT_PASSWORD`: Root password for MariaDB
- `MYSQL_DATABASE`: Database name
- `MYSQL_USER`: Database user
- `MYSQL_PASSWORD`: Database password

### Spring Boot Services
- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password

## Development vs Production

### Development
The current configuration is suitable for development. For production:

1. Use secrets management (Docker Secrets or external vault)
2. Configure proper resource limits
3. Set up monitoring and logging
4. Use production-grade database passwords
5. Configure SSL/TLS for database connections
6. Set up reverse proxy (nginx/traefik) for services

### Production Recommendations
```yaml
# Add to each service in docker-compose.yml
deploy:
  resources:
    limits:
      cpus: '1'
      memory: 1G
    reservations:
      cpus: '0.5'
      memory: 512M
```

## Troubleshooting

### Services won't start
1. Check if ports are already in use:
```bash
netstat -tulpn | grep -E '3306|8083|8084|8085'
```

2. Check service logs:
```bash
docker-compose logs [service-name]
```

### Database connection errors
1. Ensure MariaDB is healthy:
```bash
docker-compose ps mariadb
```

2. Check database logs:
```bash
docker-compose logs mariadb
```

### Rebuild specific service
```bash
docker-compose up -d --build --no-deps auth-service
```

### Access service container
```bash
docker exec -it enadzornik-auth-service sh
docker exec -it enadzornik-material-service sh
docker exec -it enadzornik-file-service sh
```

## API Testing

Once services are running, you can test the endpoints:

```bash
# Health check (if implemented)
curl http://localhost:8083/actuator/health
curl http://localhost:8084/actuator/health
curl http://localhost:8085/actuator/health
```

## Scaling Services

To run multiple instances of a service:
```bash
docker-compose up -d --scale material-service=3
```

Note: You'll need to configure a load balancer for this to work properly.

## Backup and Restore

### Backup Database
```bash
docker exec enadzornik-mariadb mysqldump -u enadzornik -penadzornik enadzornik > backup.sql
```

### Restore Database
```bash
docker exec -i enadzornik-mariadb mysql -u enadzornik -penadzornik enadzornik < backup.sql
```

## Clean Up

### Remove all containers and images
```bash
docker-compose down --rmi all -v
```

### Remove unused Docker resources
```bash
docker system prune -a --volumes
```

## Support

For issues or questions, please refer to the project documentation or contact the development team.