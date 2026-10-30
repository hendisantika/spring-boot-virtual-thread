# spring-boot-virtual-thread

[![Java CI with Maven](https://github.com/hendisantika/spring-boot-virtual-thread/actions/workflows/maven.yml/badge.svg)](https://github.com/hendisantika/spring-boot-virtual-thread/actions/workflows/maven.yml)
![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.2-brightgreen?logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-9.3-blue?logo=mysql)

Demo of Java virtual threads (Project Loom) in a Spring Boot REST API: Tomcat request handling and `@Async` tasks both
run on `Executors.newVirtualThreadPerTaskExecutor()` (see `config/AsyncConfig.java`).

## Tech Stack

- Java 25 (Temurin)
- Spring Boot 4.1.2 (Web, Data JPA, Docker Compose support)
- MySQL 9.3
- Maven 3.10 (via Maven Wrapper)
- JMeter & k6 for load testing

## Prerequisites

- JDK 25
- Docker (MySQL is started automatically from `compose.yaml` by `spring-boot-docker-compose`)

### Things todo list

Now let’s setup our JMeter. We will have 1000 requests, which will ramp up in 3 seconds. And it will continue like this
for a duration of 200 seconds. Every 3 seconds, 1000 GET (“/thread”) requests will be fired. We have also added a
Response Time Graph Listener.
Clearly, now the response time for concurrent 1000 requests is nearly just above 1000 ms and at some point, shoots up to
1400 seconds, which is far better than when we were using normal threads.

Clearly, when we need to utilise the underlying CPU to the fullest, we should start embracing virtual threads in our
application, and suddenly we can see that the throughput of our application has increased manifold for the same
hardware.

This is much better than switching to reactive programming, which means rewriting all your code, which is very hard to
maintain and even harder to debug.

Simply put, more users can use the application and get their response simultaneously as the first user.

## How to Run

1. Clone this repository: `git clone https://github.com/hendisantika/spring-boot-virtual-thread.git`
2. Navigate to the folder: `cd spring-boot-virtual-thread`
3. (Optional) Change your DB credentials in `src/main/resources/application.yml` and `compose.yaml`
4. Run the application: `./mvnw clean spring-boot:run` (MySQL container is started automatically)
5. Build & run tests: `./mvnw verify` (tests expect MySQL on `localhost:3308` with database `todos`, same as the CI service container)

JMeter Config

Run JMeter CLI: `jmeter -n -t Thread_Group.jmx -l -e -o report/report.csv `

![JMeter Config](img/config.png)

Normal Thread

![Normal Thread](img/normal.png "Normal Thread")

Virtual Thread

![Virtual Thread](img/virtual.png "Virtual Thread")

Link: https://qaprovider.com/discussion/show/how-to-run-jmeter-from-command-line-non-gui-mode/40

## Run Performance Testing with k6

The project includes a comprehensive k6 script (`k6.js`) for load testing the API endpoints. This script tests all three
endpoints:

1. `GET /` - Home endpoint
2. `GET /thread` - Thread endpoint that returns all products with a 1-second delay
3. `POST /save` - Endpoint that creates 1000 random products

### Load Testing Configuration

The script uses the following configuration:

- **Stages**:
  - Ramp up to 20 virtual users over 30 seconds
  - Stay at 20 virtual users for 1 minute
  - Ramp down to 0 virtual users over 30 seconds

- **Thresholds**:
  - 95% of requests should complete within 2 seconds
  - 95% of requests should be successful
  - Less than 5% of requests should fail

### Custom Metrics

The script collects the following custom metrics:

- Success rate
- Error rate
- Response time for each endpoint

### How to Run

1. Install k6 [here](https://k6.io/docs/get-started/installation/)
2. Make sure your Spring Boot application is running on port 8080 (or update the `BASE_URL` in k6.js)
3. Run the test script:
   ```
   k6 run k6.js
   k6 --duration '200s' --vus 1000 run k6.js
   ```

### Interpreting Results

After running the test, k6 will output detailed statistics including:

- HTTP request metrics (total requests, rate, duration)
- Custom metrics (success rate, error rate, response times)
- Checks (validation of responses)
- Thresholds (whether performance targets were met)

You can compare these results with the JMeter results to see the performance differences between normal threads and
virtual threads.

## CI/CD & Dependency Updates

- **GitHub Actions** (`.github/workflows/maven.yml`): builds and tests on every push/PR to `main` using JDK 25 with a
  MySQL 9.3 service container, then submits the Maven dependency graph on `main` for better Dependabot alerts.
- **Dependabot** (`.github/dependabot.yml`):
  - Maven: daily, grouped (Spring Boot / minor & patch), 3-day cooldown, `build(deps)` commit prefix
  - GitHub Actions: weekly, single grouped PR, `ci(deps)` commit prefix
  - Docker Compose: weekly updates for the MySQL image in `compose.yaml`
