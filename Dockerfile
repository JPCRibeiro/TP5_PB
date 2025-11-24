FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew clean assemble --no-daemon

EXPOSE 7000

CMD ["sh", "-c", "java -jar build/libs/*.jar"]