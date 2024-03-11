package dev.shann.mcproductservice.service;

import dev.shann.mcproductservice.mail.model.MailDTO;
import dev.shann.mcproductservice.mail.producer.EmailClient;
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

    private UserServiceClient userServiceClient;

    private EmailClient emailClient;

    private static final String INVALID_USER = "Invalid User";


    public ProductService(ProductRepository productRepository, UserServiceClient userServiceClient, EmailClient emailClient) {
        this.productRepository = productRepository;
        this.userServiceClient = userServiceClient;
        this.emailClient = emailClient;
    }

    public List<Product> getAllProducts(String productName, String email, String password) {
        var verifiedUser = userServiceClient.userAuthentication(email, password);
        if(!verifiedUser)
            throw  new UnAuthorizedAccessException(INVALID_USER);
        Pageable sortedByNameFirstPage = PageRequest.of(0,1, Sort.by("productName").descending());
        return productRepository.findAllByProductName(productName, sortedByNameFirstPage);
    }

    public Product getProduct(Long productId, String email, String password) {
        var verifiedUser = userServiceClient.userAuthenticationViaHttpConnection(email, password);
        if(!verifiedUser)
            throw  new UnAuthorizedAccessException( INVALID_USER);
        return productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    }

    public Product createProduct(Product product, String email, String password){
        var verifiedUser = userServiceClient.userAuthentication(email, password);
        if(!verifiedUser)
            throw  new UnAuthorizedAccessException(INVALID_USER);
         var createdProduct = productRepository.save(product);
        emailClient.sendSimpleMail(MailDTO.builder().recipient(email).subject("Product Created")
                .msgBody(String.
                        join("Product Created with Product Id : ",createdProduct
                                .getProductId().toString()))
                .build());
        return createdProduct;
    }

}
