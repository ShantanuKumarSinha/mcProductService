package dev.shann.mcproductservice.service;

import dev.shann.mcproductservice.entity.ProductEntity;
import dev.shann.mcproductservice.exceptions.UnAuthorizedAccessException;
import dev.shann.mcproductservice.exceptions.ProductNotFoundException;
import dev.shann.mcproductservice.mail.model.MailDTO;
import dev.shann.mcproductservice.mail.producer.EmailClient;
import dev.shann.mcproductservice.model.Product;
import dev.shann.mcproductservice.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class ProductService {


    private static final String INVALID_USER = "Invalid User";
    private ProductRepository productRepository;
    private UserService userService;
    private EmailClient emailClient;
    private ModelMapper modelMapper;


    public ProductService(ProductRepository productRepository, UserService userService, EmailClient emailClient) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.emailClient = emailClient;
        this.modelMapper = new ModelMapper();
    }

    public List<Product> getAllProducts(String brand, Integer pageNumber, Integer
            pageSize, String email, String password) {
        if (email != null && password != null) {
            var verifiedUser = userService.userAuthenticationViaFeignClient(email, password);
            if (!verifiedUser)
                throw new UnAuthorizedAccessException(INVALID_USER);
        }
        if (Objects.isNull(brand))
            brand = "";
        if (pageNumber == null)
            pageNumber = 0;
        if (pageSize == null)
            pageSize = 20;
        Pageable sortedByNamePage = PageRequest.of(pageNumber, pageSize, Sort.by("price").descending()
                .and(Sort.by("brand")));
        var productEntityList = productRepository.findByBrandContaining(brand, sortedByNamePage);
        return modelMapper.map(productEntityList, new TypeToken<List<Product>>() {
        }.getType());
    }

    public Product getProduct(Long productId, String email, String password) {
        var verifiedUser = userService.userAuthenticationViaHttpConnection(email, password);
        if (!verifiedUser)
            throw new UnAuthorizedAccessException(INVALID_USER);
        return modelMapper.map(productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new), Product.class);
    }

    public Product createProduct(Product product, String email, String password) {
        var verifiedUser = userService.userAuthentication(email, password);
        if (!verifiedUser)
            throw new UnAuthorizedAccessException(INVALID_USER);
        var createdProduct = productRepository.save(modelMapper.map(product, ProductEntity.class));
        sendMail(email, createdProduct);
        return modelMapper.map(createdProduct, Product.class);
    }

    public Product updateProduct(Product product, String email, String password) {
        var verifiedUser = userService.userAuthentication(email, password);
        if (!verifiedUser)
            throw new UnAuthorizedAccessException(INVALID_USER);
        var updatedProduct = productRepository.save(modelMapper.map(product, ProductEntity.class));
        sendMail(email, updatedProduct);
        return modelMapper.map(updatedProduct, Product.class);
    }

    private void sendMail(String email, ProductEntity productEntity) {
        emailClient.sendSimpleMail(MailDTO.builder().recipient(email)
                .subject(String.format("Product Created with Product Id %s: ", productEntity
                        .getProductId().toString()))
                .msgBody(String.format("Product Details are listed below %s: ", productEntity
                        .toString()))
                .build());
    }

}
