package dev.shann.mcproductservice.repository;

import dev.shann.mcproductservice.entity.ProductEntity;
import dev.shann.mcproductservice.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    ProductEntity save(Product product);

    ProductEntity getById(Long productId);

    List<ProductEntity> findAllByProductName(String productName, Pageable sortedByNameFirstPage);
}
