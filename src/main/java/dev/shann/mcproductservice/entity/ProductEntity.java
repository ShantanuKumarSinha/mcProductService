package dev.shann.mcproductservice.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "Product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long productId;
    String productName;
    String brand;
    Integer quantity;
    Integer price;
}
