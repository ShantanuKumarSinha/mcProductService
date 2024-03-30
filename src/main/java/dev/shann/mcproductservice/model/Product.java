package dev.shann.mcproductservice.model;

import lombok.Data;



@Data
public class Product {
    Long productId;
    String productName;
    String brand;
    Integer quantity;
    Integer price;
}
