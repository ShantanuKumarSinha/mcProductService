package dev.shann.mcproductservice;

import dev.shann.mcproductservice.entity.ProductEntity;
import dev.shann.mcproductservice.mail.producer.EmailClient;
import dev.shann.mcproductservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@TestPropertySource(locations = "file:src/test/java/resources/application-test.properties")
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    ProductEntity productEntity;

    @BeforeEach
    void setUp(){
        productEntity = productRepository.save(ProductEntity.builder()
                .productName("Test").brand("Brand").price(100).quantity(10)
                .build());
    }

    @Test
    void shouldCreateNewProduct(){
        assertThat(productEntity).isNotNull();
        assertThat(productEntity.getProductId()).isPositive();
    }

    @Test
    void shouldFindProductById(){
        var actualProduct = productRepository.findById(productEntity.getProductId());
        assertThat(actualProduct).isPresent();
        assertThat(actualProduct).isPresent().get()
                .extracting(ProductEntity::getProductId,ProductEntity::getProductName,
                        ProductEntity::getBrand, ProductEntity::getPrice,
                        ProductEntity::getQuantity)
                .contains(productEntity.getProductId(),productEntity.getProductName(),
                        productEntity.getBrand(), productEntity.getPrice(),
                        productEntity.getQuantity());
    }
}
