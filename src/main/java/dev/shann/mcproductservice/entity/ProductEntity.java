package dev.shann.mcproductservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Builder(toBuilder = true)
@Table(name = "Product")
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long productId;
    String productName;
    String brand;
    Integer quantity;
    Integer price;
}
