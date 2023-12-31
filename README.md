# spring-boot-virtual-thread

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

1. Clone this repository: `cd https://github.com/hendisantika/spring-boot-virtual-thread.git`
2. Navigate to the folder: `cd spring-boot-virtual-thread`
3. Change your DB credentials in `application.properties` file
4. Run the application: `mvn clean spring-boot:run`

JMeter Config

Run JMeter CLI: `jmeter -n -t Thread\ Group.jmx -l -e -o report/report.csv `

![JMeter Config](img/config.png)

Normal Thread

![Normal Thread](img/normal.png "Normal Thread")

Virtual Thread

![Virtual Thread](img/virtual.png "Virtual Thread")

Link: https://qaprovider.com/discussion/show/how-to-run-jmeter-from-command-line-non-gui-mode/40

Run Performance Testing with k6:

1. Install k6 [here](https://k6.io/docs/get-started/installation/)
2. Run test script `k6 --duration '200s' --vus 1000 run k6.js`
