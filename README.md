# Camel Spring Boot 2 Example

This sample application shows how to run Apache Camel to Spring Boot.

## How to run

Run with:

    mvn spring-boot:run

## How to load data

Configure a SFTP using the docker image below

```
docker run -p 2222:22 -d emberstack/sftp --name sftp
```

Connect using Filezilla to load sample data to the desired folder location

## How to monitor the application

Go to (http://localhost:8081/actuator/hawtio)

You can also customize the endpoint path of the Hawtio actuator endpoint by setting the `management.endpoints.web.path-mapping.hawtio` property in `application.properties`:

```
management.endpoints.web.path-mapping.hawtio=hawtio/console
```

