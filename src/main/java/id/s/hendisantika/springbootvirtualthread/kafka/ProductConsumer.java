package id.s.hendisantika.springbootvirtualthread.kafka;

import id.s.hendisantika.springbootvirtualthread.dto.ProductRequest;
import id.s.hendisantika.springbootvirtualthread.model.Product;
import id.s.hendisantika.springbootvirtualthread.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductConsumer {
    private final ProductRepository productRepository;

    @KafkaListener(topics = "product-requests", groupId = "product-consumer-group")
    public void consumeProductRequest(ProductRequest request) {
        log.info("Received product request from Kafka: {}", request);

        try {
            // Batch insert products
            List<Product> products = new ArrayList<>();
            for (int i = 0; i < request.getCount(); i++) {
                Product product = new Product();
                product.setProductName(request.getProductName());
                product.setPrice(request.getPrice());
                products.add(product);
            }

            productRepository.saveAll(products);
            log.info("Successfully saved {} products to database", products.size());
        } catch (Exception e) {
            log.error("Error saving products to database", e);
            // In production, you'd want to send to DLQ or retry
        }
    }
}
