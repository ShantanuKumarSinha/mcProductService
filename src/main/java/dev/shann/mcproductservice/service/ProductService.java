package dev.shann.mcproductservice.service;

import dev.shann.mcproductservice.mail.model.MailDTO;
import dev.shann.mcproductservice.mail.producer.EmailClient;
import dev.shann.mcproductservice.model.Product;
import dev.shann.mcproductservice.repository.ProductRepository;
import dev.shann.mcproductservice.utility.ProductNotFoundException;
import dev.shann.mcproductservice.utility.UnAuthorizedAccessException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService {


    private ProductRepository productRepository;

    private UserService userService;

    private EmailClient emailClient;

    private ModelMapper modelMapper;

    private static final String INVALID_USER = "Invalid User";


    public ProductService(ProductRepository productRepository, UserService userService, EmailClient emailClient) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.emailClient = emailClient;
        this.modelMapper = new ModelMapper();
    }

    public List<Product> getAllProducts(String productName, String email, String password) {
        var verifiedUser = userService.userAuthenticationViaFeignClient(email, password);
        if(!verifiedUser)
            throw  new UnAuthorizedAccessException(INVALID_USER);
        if(productName == null)
            return modelMapper.map(productRepository.findAll(),new TypeToken<List<Product>>(){}.getType());

        Pageable sortedByNameFirstPage = PageRequest.of(0,1, Sort.by("productName").descending());
        var productEntityList = productRepository.findAllByProductName(productName, sortedByNameFirstPage);
        return modelMapper.map(productEntityList, new TypeToken<List<Product>>(){}.getType());
    }

    public Product getProduct(Long productId, String email, String password) {
        var verifiedUser = userService.userAuthenticationViaHttpConnection(email, password);
        if(!verifiedUser)
            throw  new UnAuthorizedAccessException( INVALID_USER);
        return modelMapper.map(productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new),Product.class);
    }

    public Product createProduct(Product product, String email, String password){
        var verifiedUser = userService.userAuthentication(email, password);
        if(!verifiedUser)
            throw  new UnAuthorizedAccessException(INVALID_USER);
         var createdProduct = productRepository.save(product);
        emailClient.sendSimpleMail(MailDTO.builder().recipient(email)
                .subject(String.format("Product Created with Product Id %s: ",createdProduct
                        .getProductId().toString()))
                .msgBody(String.format("Product Details are listed below %s: ",createdProduct
                        .toString()))
                .build());
        return modelMapper.map(createdProduct, Product.class);
    }

}
