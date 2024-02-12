package dev.shann.mcproductservice.repository;

import dev.shann.mcproductservice.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product save(Product product);

    Product getById(Long productId);

    List<Product> findAllByProductName(String productName, Pageable sortedByNameFirstPage);
}
