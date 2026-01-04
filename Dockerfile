# syntax=docker/dockerfile:1.4
########################################
# Build stage (avec cache BuildKit pour ~/.m2)
########################################
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace/app

# Copier pom.xml seul pour bénéficier du cache dépendances
COPY pom.xml .

# Pré-télécharger dépendances en utilisant le cache Maven de BuildKit
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -ntp -DskipTests dependency:go-offline

# Copier sources et construire (réutilise le cache ~/.m2)
COPY src ./src
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -ntp -DskipTests package

########################################
# Runtime stage
########################################
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

ENV JAVA_OPTS=""

# Copier le jar construit (plus robuste que *-SNAPSHOT.jar)
COPY --from=build /workspace/app/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]