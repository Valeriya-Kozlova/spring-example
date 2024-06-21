FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN ./gradlew build

ENV SPRING_PROFILES_ACTIVE prod

CMD java -jar build/libs/app-0.0.1-SNAPSHOT.jar