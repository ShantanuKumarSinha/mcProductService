package dev.shann.mcproductservice.controller;

import dev.shann.mcproductservice.dto.CreateProductDetailsRequestDto;
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
public class ProductController {

   private ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }


    @GetMapping("")
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(required = false) String brand,
                                                        @RequestParam(required = false) Integer pageNumber,
                                                        @RequestParam(required = false) Integer pageSize,
                                                        @RequestBody GetProductDetailsRequestDto requestDto){
        return new ResponseEntity<>(this.productService.
                getAllProducts(brand, pageNumber, pageSize,requestDto.email(), requestDto.password()), HttpStatus.OK);
    }

    @GetMapping
     ("/{productId}")
    public ResponseEntity<Product> getProductDetails(@PathVariable("productId") Long productId,
                                     @RequestBody GetProductDetailsRequestDto requestDto){
        return ResponseEntity.ok(this.productService.getProduct(productId, requestDto.email(), requestDto.password()));
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product createProduct(@Validated @RequestBody CreateProductDetailsRequestDto
                                            requestDto){
            return this.productService.createProduct(requestDto.product(),
                    requestDto.emailId(),  requestDto.password());
    }
}
