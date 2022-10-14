# Front Service
#### simulating back-to-front service

### Microservice Architecture
```
 --------------------             ------------------            ------------------      
| Client(UI/Browser) | =======> | Front api service | =======> | Customer service |   
 --------------------             ------------------            ------------------
```


### Execute using IDE/command line
```
1. Start the customer service first (reference link given below)
2. Then Start the front api service using command: mvn spring-boot:run
3. Open browser: http://localhost:8080/customerDetails/1
```
###### Reference: [customer service](https://github.com/rkdutta/otel-demo-customer-service)


### Execute using Docker
```
1. Start the customer service. . Reference link is mentioned above.
2. Start the front-api-service using Docker.
    docker network create tracing(ignore this step if already done)
    docker pull rduttaxebia/otel-demo-customer-service:latest
    docker run --name customer-service --network tracing -p 8081:8081 rduttaxebia/otel-demo-customer-service:latest
```

## Enable OpenTelemetry

1. Add the following dependencies in [pom.xml](pom.xml)
```xml
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-sleuth-zipkin</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-sleuth</artifactId>
		</dependency>
```

2. Add/append the following in [application.yml](/src/main/resources/application.yml)

```yaml
spring:
    zipkin:
        baseUrl: http://localhost:9411
    sleuth:
        sampler:
            probability: 1.0 # allowed values between 0.0 - 0.1
```

3. Start a zipkin server using Docker (ignore if already started)
```
docker network create tracing
docker run -d --rm -it --name zipkin --network tracing -p 9411:9411 openzipkin/zipkin:latest
```

4. Restart the service (check the Execute section above)

## Instrumenting by injecting agent during startup
```
export OTEL_EXPORTER_ZIPKIN_ENDPOINT=http://localhost:9411/api/v2/spans
export OTEL_TRACES_EXPORTER=zipkin
export OTEL_SERVICE_NAME=front-service
java -javaagent:opentelemetry-javaagent.jar  -jar target/*.jar
```


