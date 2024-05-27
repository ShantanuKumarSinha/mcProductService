package dev.shann.mcproductservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    Long productId;
    String productName;
    String brand;
    Integer quantity;
    Integer price;
}
