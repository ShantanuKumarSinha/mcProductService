package dev.shann.mcproductservice.repository;

import dev.shann.mcproductservice.entity.ProductEntity;
import dev.shann.mcproductservice.projections.ProductNameAndPrice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    ProductEntity save(ProductEntity productEntity);

    List<ProductEntity> findByBrandContaining(String brand, Pageable sortedByNameFirstPage);

    @Query("SELECT p.productName AS productName, p.price AS price FROM ProductEntity p WHERE p.brand = ?1")
    List<Optional<ProductNameAndPrice>> findByBrand(String brand);

}
