package dev.shann.mcproductservice.repository;

import dev.shann.mcproductservice.entity.ProductEntity;
import dev.shann.mcproductservice.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    ProductEntity save(ProductEntity productEntity);

    ProductEntity getById(Long productId);

    List<ProductEntity> findByBrandContaining(String brand, Pageable sortedByNameFirstPage);
}
