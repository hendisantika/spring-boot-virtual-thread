package id.s.hendisantika.springbootvirtualthread;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootApplication
@RequiredArgsConstructor
public class SpringBootVirtualThreadApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootVirtualThreadApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootVirtualThreadApplication.class, args);
    }

//    @Bean
//    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
//        return protocolHandler -> {
//            LOGGER.info("Configuring " + protocolHandler + " to use VirtualThreadPerTaskExecutor");
//            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
//        };
//    }

    private final ApplicationEventPublisher publisher;

    @Override
    public void run(String... args) throws Exception {
        Instant start = Instant.now();
        IntStream.rangeClosed(1, 10_000)
                .forEachOrdered(i ->
                        publisher.publishEvent("Hello #" + i + " at:" + LocalDateTime.now())
                );

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();

        LOGGER.info("=====Elapsed time : {} ========", timeElapsed);
    }
}
