package dev.shann.mcproductservice;

import dev.shann.mcproductservice.controller.ProductController;
import dev.shann.mcproductservice.dto.CreateOrUpdateProductDetailsRequestDto;
import dev.shann.mcproductservice.dto.GetProductDetailsRequestDto;
import dev.shann.mcproductservice.model.Product;
import dev.shann.mcproductservice.service.ProductService;
import dev.shann.mcproductservice.utility.ProductNotFoundException;
import dev.shann.mcproductservice.utility.UnAuthorizedAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Test
    void shouldGetAllProducts() {
        var productList = getProductList();
        when(productService.getAllProducts(anyString(), anyInt(), anyInt(), eq(null), eq(null))).thenReturn(productList);
        var listResponseEntity = productController.getAllProducts("testBrand", 1, 2);
        var actualResult = listResponseEntity.getBody();
        assertThat(actualResult).isNotNull().extracting(Product::getProductId, Product::getProductName, Product::getBrand, Product::getPrice, Product::getQuantity)
                .contains(tuple(1L, "product1", "brand1", 1000, 1000), tuple(2L, "product2", "brand2", 10000, 100));
    }

    @Test
    void shouldNotGetAllProducts() {
        when(productService.getAllProducts(anyString(), anyInt(), anyInt(), eq(null), eq(null))).thenReturn(new ArrayList<Product>());
        var listResponseEntity = productController.getAllProducts("testBrand", 1, 2);
        var actualResult = listResponseEntity.getBody();
        assertThat(actualResult).isNullOrEmpty();
    }

    @Test
    void shouldNotGetAllProductsAndThrowUnAuthorizedException() {
        when(productService.getAllProducts(anyString(), anyInt(), anyInt(), eq(null), eq(null))).thenThrow(UnAuthorizedAccessException.class);
        assertThrows(UnAuthorizedAccessException.class, () -> productController.getAllProducts("testBrand", 1, 2));
    }

    @Test
    void shouldGetAllProductsOld() {
        var productList = getProductList();
        when(productService.getAllProducts(anyString(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(productList);
        var requestDto = new GetProductDetailsRequestDto("test@test.com", "testPassword");
        var listResponseEntity = productController.getAllProductsOld("testBrand", 1, 2, requestDto);
        var actualResult = listResponseEntity.getBody();
        assertThat(actualResult).isNotNull().extracting(Product::getProductId, Product::getProductName, Product::getBrand, Product::getPrice, Product::getQuantity)
                .contains(tuple(1L, "product1", "brand1", 1000, 1000), tuple(2L, "product2", "brand2", 10000, 100));
    }

    @Test
    void shouldNotGetAllProductsOld() {
        when(productService.getAllProducts(anyString(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(null);
        var requestDto = new GetProductDetailsRequestDto("test@test.com", "testPassword");
        var listResponseEntity = productController.getAllProductsOld("testBrand", 1, 2, requestDto);
        var actualResult = listResponseEntity.getBody();
        assertThat(actualResult).isNullOrEmpty();
    }

    @Test
    void shouldNotGetAllProductsOldAndThrowUnAuthorizedException() {
        when(productService.getAllProducts(anyString(), anyInt(), anyInt(), anyString(), anyString())).thenThrow(UnAuthorizedAccessException.class);
        var requestDto = new GetProductDetailsRequestDto("test@test.com", "testPassword");
        assertThrows(UnAuthorizedAccessException.class, () -> productController.getAllProductsOld("testBrand", 1, 2, requestDto));
    }

    @Test
    void shouldGetProductDetails() {
        var expectedResponse = getProductDetails();
        when(productService.getProduct(anyLong(), eq(null), eq(null))).thenReturn(expectedResponse);
        var response = productController.getProductDetails(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var actualResponseBondy = response.getBody();
        assertThat(actualResponseBondy).isNotNull().extracting(Product::getProductId, Product::getProductName, Product::getPrice, Product::getBrand, Product::getQuantity)
                .contains(1L, expectedResponse.getProductName(), expectedResponse.getBrand(), expectedResponse.getPrice(), expectedResponse.getQuantity());

    }

    @Test
    void shouldNotGetProductDetailsAndThrowProductNotFoundException() {
        when(productService.getProduct(anyLong(), eq(null), eq(null))).thenThrow(new ProductNotFoundException());
        assertThrows(ProductNotFoundException.class, () -> productController.getProductDetails(1L));
    }

    @Test
    void shouldNotGetProductDetailsAndThrowUnAuthorizedAccessException() {
        when(productService.getProduct(anyLong(), eq(null), eq(null))).thenThrow(new UnAuthorizedAccessException(null));
        assertThrows(UnAuthorizedAccessException.class, () -> productController.getProductDetails(1L));
    }

    @Test
    void shouldCreateProduct() {
        var expectedResponse = getProductDetails();
        when(productService.createProduct(any(Product.class), anyString(), anyString())).thenReturn(expectedResponse);
        CreateOrUpdateProductDetailsRequestDto requestDto = new CreateOrUpdateProductDetailsRequestDto("test@test.com", "testPassword", expectedResponse);
        var actualProduct = productController.createProduct(requestDto);
        assertThat(actualProduct).isNotNull().extracting(Product::getProductId, Product::getProductName, Product::getPrice, Product::getQuantity, Product::getBrand).contains(1L, expectedResponse.getProductName(), expectedResponse.getBrand(), expectedResponse.getPrice(), expectedResponse.getQuantity());
    }

    @Test
    void shouldThrowUnAuthorizedExceptionWhileCreatingProduct() {
        var expectedResponse = getProductDetails();
        when(productService.createProduct(any(Product.class), anyString(), anyString())).thenThrow(new UnAuthorizedAccessException(null));
        CreateOrUpdateProductDetailsRequestDto requestDto = new CreateOrUpdateProductDetailsRequestDto("test@test.com", "testPassword", expectedResponse);
        assertThrows(UnAuthorizedAccessException.class, () -> productController.createProduct(requestDto));
    }


    private List<Product> getProductList() {
        var product1 = Product.builder().productId(1L).productName("product1").brand("brand1").price(1000).quantity(1000).build();
        var product2 = Product.builder().productId(2L).productName("product2").brand("brand2").price(10000).quantity(100).build();
        return List.of(product1, product2);
    }

    private Product getProductDetails() {
        return Product.builder().productId(1L).productName("product1").brand("brand1").price(1000).quantity(1000).build();
    }
}
