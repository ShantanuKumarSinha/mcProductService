package dev.shann.mcproductservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;


@Entity
@Data
@Builder
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
