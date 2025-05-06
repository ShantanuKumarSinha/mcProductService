package dev.shann.mcproductservice.service;

import dev.shann.mcproductservice.entity.ProductEntity;
import dev.shann.mcproductservice.exceptions.ProductNotFoundException;
import dev.shann.mcproductservice.exceptions.UnAuthorizedAccessException;
import dev.shann.mcproductservice.model.Mail;
import dev.shann.mcproductservice.adapters.mail.MailAdapter;
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

/**
 * * @author shann
 * * @version 1.0
 */
@Service
public class ProductService {


    private static final String INVALID_USER = "Invalid User";
    private ProductRepository productRepository;
    private UserService userService;
    private MailAdapter mailAdapter;
    private ModelMapper modelMapper;

    /**
     * Constructor
     *
     * @param productRepository
     * @param userService
     * @param mailAdapter
     */
    public ProductService(ProductRepository productRepository, UserService userService, MailAdapter mailAdapter) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.mailAdapter = mailAdapter;
        this.modelMapper = new ModelMapper();
    }

    /**
     * Get all products
     *
     * @param brand
     * @param pageNumber
     * @param pageSize
     * @return List of Products
     */
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

    /**
     * Get product by id
     *
     * @param productId
     * @return Product
     */
    public Product getProduct(Long productId, String email, String password) {
        var verifiedUser = userService.userAuthenticationViaHttpConnection(email, password);
        if (!verifiedUser)
            throw new UnAuthorizedAccessException(INVALID_USER);
        return modelMapper.map(productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new), Product.class);
    }

    /**
     * Create product
     *
     * @param product
     * @return Product
     */
    public Product createProduct(Product product, String email, String password) {
        var verifiedUser = userService.userAuthentication(email, password);
        if (!verifiedUser)
            throw new UnAuthorizedAccessException(INVALID_USER);
        var createdProduct = productRepository.save(modelMapper.map(product, ProductEntity.class));
        sendMail(email, createdProduct);
        return modelMapper.map(createdProduct, Product.class);
    }

    /**
     * Update product
     *
     * @param product
     * @return Product
     */
    public Product updateProduct(Product product, String email, String password) {
        var verifiedUser = userService.userAuthentication(email, password);
        if (!verifiedUser)
            throw new UnAuthorizedAccessException(INVALID_USER);
        var updatedProduct = productRepository.save(modelMapper.map(product, ProductEntity.class));
        sendMail(email, updatedProduct);
        return modelMapper.map(updatedProduct, Product.class);
    }

    /**
     * Send Mail Feature
     *
     * @param email
     * @param productEntity
     */
    private void sendMail(String email, ProductEntity productEntity) {
        mailAdapter.sendSimpleMail(Mail.builder().recipient(email)
                .subject(String.format("Product Created with Product Id %s: ", productEntity
                        .getProductId().toString()))
                .msgBody(String.format("Product Details are listed below %s: ", productEntity
                        .toString()))
                .build());
    }

}
