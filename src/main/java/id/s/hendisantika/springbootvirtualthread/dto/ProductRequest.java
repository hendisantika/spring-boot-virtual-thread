package id.s.hendisantika.springbootvirtualthread.dto;

import lombok.Data;

@Data
public class ProductRequest {
    private String productName;
    private Long price;
    private int count = 1;
}
