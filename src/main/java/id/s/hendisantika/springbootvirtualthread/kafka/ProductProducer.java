package id.s.hendisantika.springbootvirtualthread.kafka;

import id.s.hendisantika.springbootvirtualthread.dto.ProductRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductProducer {
    private static final String TOPIC = "product-requests";

    private final KafkaTemplate<String, ProductRequest> kafkaTemplate;

    public void sendProductRequest(ProductRequest request) {
        log.info("Sending product request to Kafka: {}", request);
        kafkaTemplate.send(TOPIC, request);
    }
}
