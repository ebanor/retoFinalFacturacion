# Etapa 1: Construcción
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiar el JAR generado
COPY --from=build /app/target/*.jar app.jar

# Copiar el script Python a una ubicación fija
COPY --from=build /app/src/main/resources/scripts/procesar_facturas.py /app/scripts/procesar_facturas.py

# Instalar Python y dependencias
RUN apt-get update && \
    apt-get install -y python3 python3-pip && \
    pip3 install mysql-connector-python --break-system-packages && \
    rm -rf /var/lib/apt/lists/*

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
