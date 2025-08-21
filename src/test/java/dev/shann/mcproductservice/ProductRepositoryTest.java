package dev.shann.mcproductservice;

import dev.shann.mcproductservice.entity.ProductEntity;
import dev.shann.mcproductservice.projections.ProductNameAndPrice;
import dev.shann.mcproductservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
//@TestPropertySource(locations = "file:src/test/resources/application-test.properties")
@TestPropertySource(("classpath:application-test.properties"))
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;


    ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        productEntity = productRepository.save(ProductEntity.builder()
                .productName("Test").brand("Brand").price(100).quantity(10)
                .build());
    }

    @Test
    void shouldCreateNewProduct() {
        assertThat(productEntity).isNotNull();
        assertThat(productEntity.getProductId()).isPositive();
    }

    @Test
    void shouldFindProductById() {
        var actualProduct = productRepository.findById(productEntity.getProductId());
        assertThat(actualProduct).isPresent().get()
                .extracting(ProductEntity::getProductId, ProductEntity::getProductName,
                        ProductEntity::getBrand, ProductEntity::getPrice,
                        ProductEntity::getQuantity)
                .contains(productEntity.getProductId(), productEntity.getProductName(),
                        productEntity.getBrand(), productEntity.getPrice(),
                        productEntity.getQuantity());
    }

    @Test
    void shouldFindProductByBrand() {
        var actualProductList = productRepository.findByBrandContaining(productEntity.getBrand(), PageRequest.of(0, 2, Sort.by("price").descending()
                .and(Sort.by("brand"))));

        assertThat(actualProductList).isNotNull().hasSize(1);
        assertThat(actualProductList)
                .extracting(ProductEntity::getProductName, ProductEntity::getBrand)
                .contains(tuple(productEntity.getProductName(), productEntity.getBrand()));
    }

    // Tested Projection of a pair of field In JPA without using Query annotation
    @Test
    void shouldFindProductNameAndPriceByBrand() {
        var productNameAndPrice = productRepository.findByBrand("Brand").stream().map(optionProductNameAndPrice ->
                optionProductNameAndPrice.orElseThrow(RuntimeException::new)
        ).toList();
        assertThat(productNameAndPrice).isNotNull().hasSize(1);
        assertThat(productNameAndPrice).extracting(ProductNameAndPrice::getProductName, ProductNameAndPrice::getPrice)
                .contains(tuple("Test", 100.0));
    }

}
