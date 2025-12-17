# Multi-stage build for auth-service
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copy parent pom and service pom
#COPY ../pom.xml ../pom.xml
#COPY pom.xml ./pom.xml
# Kopiramo CELOKUPNU strukturu projekta (build kontekst je root!)
# Ovo radi ako pokrećeš `docker build` iz root direktorijuma
COPY . .

# Download dependencies
RUN mvn dependency:go-offline -B || true

## Copy source code
#COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built jar from build stage
#COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/audit-service/target/*.jar app.jar

# Expose the port
EXPOSE 8086

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]