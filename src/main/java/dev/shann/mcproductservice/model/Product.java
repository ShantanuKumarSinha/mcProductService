package dev.shann.mcproductservice.model;

import lombok.Data;



@Data
public class Product {
    Long productId;
    String productName;
    Integer quantity;
    Integer price;
}
