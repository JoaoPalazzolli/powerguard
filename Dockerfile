FROM maven:3.9.6

WORKDIR /usr/src/powerguard

COPY . /usr/src/powerguard/

RUN mvn clean install -DskipTests

EXPOSE 8080

ENTRYPOINT ["mvn", "spring-boot:run", "-Pprod", "-DskipTests"]