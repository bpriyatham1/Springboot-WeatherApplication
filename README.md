# Spring-boot-WeatherApplication

Required installations:
Java11
Apache maven
Docker

Commands to run the spring boot project which runs the weather api

```mvn spring-boot:run```

Swagger url:

```http://localhost:8080/swagger-ui/index.html```

![swagger.png](src%2Ftest%2Fresources%2Fswagger.png)

Docker Commands:

Before test run

```docker-compose up```

After test run

```docker compose down```

Commands to use in case of debugging:

```docker run -d -p 6379:6379 --name myredis redis```

```docker build -t spring-boot-docker.jar .```

```docker run -p 8080:8080 spring-boot-docker.jar```

Commands to check Redis logs:

```
$ docker exec -it myredis redis-cli
$ 127.0.0.1:6379> KEYS *
```
