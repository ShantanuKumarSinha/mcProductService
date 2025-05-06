package dev.shann.mcproductservice;

import dev.shann.mcproductservice.entity.ProductEntity;
import dev.shann.mcproductservice.mail.model.MailDTO;
import dev.shann.mcproductservice.mail.producer.EmailClient;
import dev.shann.mcproductservice.model.Product;
import dev.shann.mcproductservice.repository.ProductRepository;
import dev.shann.mcproductservice.service.ProductService;
import dev.shann.mcproductservice.exceptions.ProductNotFoundException;
import dev.shann.mcproductservice.exceptions.UnAuthorizedAccessException;
import dev.shann.mcproductservice.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private static final String INVALID_USER = "Invalid User";
    @InjectMocks
    ProductService productService;
    @Mock
    UserService userService;
    @Mock
    ProductRepository productRepository;
    @Mock
    EmailClient emailClient;
    private Product product;
    private ProductEntity productEntity;
    private ProductEntity createdProduct;

    @BeforeEach
    void setUp() {
        product = getProduct();

        productEntity = getProductEntity();

        createdProduct = getCreatedProductEntity();
    }

    @Test
    void shouldThrowUnauthorizedException() {
        when(userService.userAuthenticationViaHttpConnection(anyString(), anyString())).thenReturn(false);
        var exception = assertThrows(UnAuthorizedAccessException.class, () ->
                productService.getProduct(1L, "test@test.com", "test@123"));
        assertThat(exception.getMessage()).isEqualTo(INVALID_USER);

    }

    @Test
    void shouldCreateNewProduct() {

        var email = "Test@test.com";
        var password = "Test@123";

        when(userService.userAuthentication(email, password)).thenReturn(true);

        when(productRepository.save(productEntity))
                .thenReturn(productEntity.toBuilder().productId(1L).build());

        when(emailClient.sendSimpleMail(buildMailDTO(createdProduct))).thenReturn("Success");

        var actualProduct = productService.createProduct(product, email, password);

        verify(emailClient, times(1)).sendSimpleMail(buildMailDTO(createdProduct));

        assertThat(actualProduct).extracting(Product::getProductId, Product::getProductName,
                        Product::getBrand, Product::getPrice,
                        Product::getQuantity)
                .contains(1L, product.getProductName(),
                        product.getBrand(), product.getPrice(),
                        product.getQuantity());
    }

    @Test
    void shouldFindProductById() {
        when(userService.userAuthenticationViaHttpConnection(anyString(), anyString()))
                .thenReturn(true);
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional
                        .ofNullable(productEntity
                                .toBuilder()
                                .productId(1L)
                                .build()));
        var actualProduct = productService
                .getProduct(1L, "test@test.com", "test@123");
        assertThat(actualProduct).extracting(Product::getProductId, Product::getProductName,
                        Product::getPrice, Product::getBrand, Product::getQuantity)
                .contains(1L, product.getProductName(), product.getPrice(),
                        product.getBrand(), product.getQuantity());
    }

    @Test
    void shouldThrowProductNotFoundException() {
        when(userService.userAuthenticationViaHttpConnection(anyString(), anyString()))
                .thenReturn(true);
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional
                        .ofNullable(null));
        var exception = assertThrows(ProductNotFoundException.class, () ->
                productService.getProduct(1L, "test@test.com", "test@123"));
        var expectedMessage = "Product Not Found";
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);

    }

    @Test
    void shouldFindProductByBrand() {
        when(userService.userAuthenticationViaFeignClient(anyString(), anyString())).thenReturn(true);
        when(productRepository.findByBrandContaining(anyString(), any(Pageable.class))).thenReturn(List.of(createdProduct));

        var actualProductList = productService.getAllProducts("Samsung", 1, 3, "test@test.com", "test@123");

        Assertions.assertThat(actualProductList).isNotNull().extracting(Product::getProductId, Product::getProductName, Product::getBrand, Product::getPrice, Product::getQuantity)
                .contains(Assertions.tuple(1L, "Test", "Test Brand", 1000, 100));
    }

    @Test
    void shouldFindProductByBrandNotFound() {
        when(userService.userAuthenticationViaFeignClient(anyString(), anyString())).thenReturn(true);
        when(productRepository.findByBrandContaining(anyString(), any(Pageable.class))).thenReturn(Collections.emptyList());

        var actualProductList = productService.getAllProducts("Samsung", 1, 3, "test@test.com", "test@123");

        Assertions.assertThat(actualProductList).isNullOrEmpty();
    }

    private ProductEntity getProductEntity() {
        return ProductEntity.builder().productName("Test").brand("Test Brand").quantity(100).price(1000).build();
    }

    private Product getProduct() {
        return Product.builder().productName("Test").brand("Test Brand").quantity(100).price(1000).build();
    }

    private ProductEntity getCreatedProductEntity() {
        return ProductEntity.builder().productId(1L).productName("Test").brand("Test Brand").quantity(100).price(1000).build();
    }

    private MailDTO buildMailDTO(ProductEntity createdProduct) {
        return MailDTO.builder().subject(String.format("Product Created with Product Id %s: ", createdProduct
                        .getProductId().toString()))
                .msgBody(String.format("Product Details are listed below %s: ", createdProduct
                        .toString()))
                .recipient("Test@test.com")
                .build();
    }

}
