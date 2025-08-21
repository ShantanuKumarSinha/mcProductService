package dev.shann.mcproductservice.service;

import dev.shann.mcproductservice.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts(String brand, Integer pageNumber, Integer
            pageSize, String email, String password);

    Product getProduct(Long productId, String email, String password);

    Product createOrUpdateProduct(Product product, String email, String password);

}
