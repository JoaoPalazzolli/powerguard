FROM maven:3.9.1

WORKDIR /usr/src/powerguard

COPY . /usr/src/powerguard/

RUN mvn clean install

EXPOSE 8080

ENTRYPOINT ["mvn", "spring-boot:run", "-DskipTests"]