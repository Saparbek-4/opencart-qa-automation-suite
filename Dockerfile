FROM maven:3.9.4-eclipse-temurin-21

WORKDIR /project

COPY . .

RUN mvn clean install -DskipTests

CMD ["mvn", "test"]
