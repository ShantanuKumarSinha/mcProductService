package dev.shann.mcproductservice.controller;

import dev.shann.mcproductservice.dto.CreateOrUpdateProductDetailsRequestDto;
import dev.shann.mcproductservice.dto.GetProductDetailsRequestDto;
import dev.shann.mcproductservice.model.Product;
import dev.shann.mcproductservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("")
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(required = false) String brand,
                                                        @RequestParam(required = false) Integer pageNumber,
                                                        @RequestParam(required = false) Integer pageSize) {
        return new ResponseEntity<>(this.productService.
                getAllProducts(brand, pageNumber, pageSize, null, null), HttpStatus.OK);
    }

    @Deprecated
    @GetMapping("/old")
    public ResponseEntity<List<Product>> getAllProductsOld(@RequestParam(required = false) String brand,
                                                           @RequestParam(required = false) Integer pageNumber,
                                                           @RequestParam(required = false) Integer pageSize,
                                                           @RequestBody GetProductDetailsRequestDto requestDto) {
        return new ResponseEntity<>(this.productService.
                getAllProducts(brand, pageNumber, pageSize, requestDto.email(), requestDto.password()), HttpStatus.OK);
    }

    @GetMapping
            ("/{productId}")
    public ResponseEntity<Product> getProductDetails(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(this.productService.getProduct(productId, null, null));
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product createProduct(@Validated @RequestBody CreateOrUpdateProductDetailsRequestDto
                                         requestDto) {
        return this.productService.createOrUpdateProduct(requestDto.product(),
                requestDto.emailId(), requestDto.password());
    }

    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product updateProduct(@Validated @RequestBody CreateOrUpdateProductDetailsRequestDto
                                         requestDto) {
        return this.productService.createOrUpdateProduct(requestDto.product(),
                requestDto.emailId(), requestDto.password());
    }


}
