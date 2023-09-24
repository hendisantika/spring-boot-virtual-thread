package id.s.hendisantika.springbootvirtualthread.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-virtual-thread
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 9/23/23
 * Time: 20:32
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private Long price;
}
