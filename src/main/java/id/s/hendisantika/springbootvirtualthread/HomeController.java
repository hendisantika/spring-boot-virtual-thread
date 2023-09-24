package id.s.hendisantika.springbootvirtualthread;

import id.s.hendisantika.springbootvirtualthread.model.Product;
import id.s.hendisantika.springbootvirtualthread.repository.ProductRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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
public class HomeController {
    @Autowired
    private HomeService homeService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String getResponse() {
        return homeService.getResponse();
    }

    @GetMapping("/thread")
    public List<Product> checkThread() throws InterruptedException {
        Thread.sleep(1000);
        return productRepository.findAll();
    }

    @PostMapping("/save")
    public String saveProduct() throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            Product product = new Product();
            product.setProductName(RandomStringUtils.randomAlphanumeric(5));
            product.setPrice(RandomUtils.nextLong(10, 1000));
            product.setPrice(1L);
            productRepository.save(product);
        }
        return "hendi " + LocalDateTime.now();
    }
}
