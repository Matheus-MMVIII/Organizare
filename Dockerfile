FROM eclipse-temurin:11-jdk-alpine AS build

WORKDIR /app

COPY src ./src
COPY lib ./lib

RUN mkdir -p bin \
    && javac -cp lib/postgresql-42.7.3.jar -d bin $(find src/main/java -name '*.java')

FROM eclipse-temurin:11-jre-alpine

WORKDIR /app

RUN apk add --no-cache wget

COPY --from=build /app/bin ./bin
COPY --from=build /app/lib ./lib
COPY .env ./.env

EXPOSE 8080

CMD ["java", "-cp", "lib/postgresql-42.7.3.jar:bin", "com.organizare.App"]
