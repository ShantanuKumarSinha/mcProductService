package dev.shann.mcproductservice.repository;

import dev.shann.mcproductservice.entity.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    ProductEntity save(ProductEntity productEntity);

    List<ProductEntity> findByBrandContaining(String brand, Pageable sortedByNameFirstPage);

    List<Optional<ProductPair>> findByBrand(String brand);

}
