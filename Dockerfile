# Utiliser l'image Java 17 Alpine maintenue par Eclipse Temurin
FROM eclipse-temurin:17-jdk-alpine

# Définir le répertoire de travail
WORKDIR /app

# Copier le JAR exécutable généré par Maven
COPY target/student-management-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port Spring Boot
EXPOSE 8080

# Lancer le JAR Spring Boot
ENTRYPOINT ["java","-jar","/app/app.jar"]
