package id.s.hendisantika.springbootvirtualthread.controller;

import id.s.hendisantika.springbootvirtualthread.model.Product;
import id.s.hendisantika.springbootvirtualthread.repository.ProductRepository;
import id.s.hendisantika.springbootvirtualthread.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-virtual-thread
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 9/23/23
 * Time: 14:18
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    private final ProductRepository productRepository;

    @GetMapping("/")
    public String getResponse() {
        return homeService.getResponse();
    }

    @GetMapping("/thread")
    public List<Product> checkThread() throws InterruptedException {
        // Sleep first, then query - don't hold transaction during sleep
        Thread.sleep(1000);
        return productRepository.findAll();
    }

    @GetMapping("/thread2")
    public List<Product> checkThread2() throws InterruptedException {
        Thread.sleep(1000);
        return productRepository.findAll();
    }

    @Transactional
    @PostMapping("/save")
    public String saveProduct() throws InterruptedException {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Product product = new Product();
            product.setProductName(RandomStringUtils.randomAlphanumeric(5));
            product.setPrice(1L);
            products.add(product);
        }
        // Batch save instead of individual saves
        productRepository.saveAll(products);
        return "hendi " + LocalDateTime.now();
    }
}
