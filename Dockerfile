# Utiliser une image Java 17 Alpine maintenue par Eclipse Temurin
FROM eclipse-temurin:17-jdk-alpine

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le JAR généré par Maven dans le conteneur
COPY target/student-management-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port sur lequel Spring Boot tourne
EXPOSE 8080

# Commande pour démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
