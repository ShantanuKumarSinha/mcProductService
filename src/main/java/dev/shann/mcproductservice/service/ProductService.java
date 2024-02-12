package dev.shann.mcproductservice.service;

import dev.shann.mcproductservice.model.Product;
import dev.shann.mcproductservice.repository.ProductRepository;
import dev.shann.mcproductservice.utility.ProductNotFoundException;
import dev.shann.mcproductservice.utility.UnAuthorizedAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService {


    private ProductRepository productRepository;

    private static final String INVALID_USER = "Invalid User";


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts(String productName, String email, String password){
        var verifiedUser = userServiceClient.userAuthentication(email, password);
        if(!verifiedUser)
            throw  new UnAuthorizedAccessException(INVALID_USER);
        Pageable sortedByNameFirstPage = PageRequest.of(0,1, Sort.by("productName").descending());
        return productRepository.findAllByProductName(productName, sortedByNameFirstPage);
    }

    public Product getProduct(Long productId, String email, String password){
        var verifiedUser = userAuthentication(email, password);
        if(!verifiedUser)
            throw  new UnAuthorizedAccessException( INVALID_USER);
        return productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    }

    public Product createProduct(Product product, String email, String password){
        var verifiedUser = userAuthentication(email, password);
        if(!verifiedUser)
            throw  new UnAuthorizedAccessException(INVALID_USER);
        product = productRepository.save(product);
        return product;
    }

}
