package dev.shann.mcproductservice.service;

import dev.shann.mcproductservice.dto.UserAuthenticationDTO;
import dev.shann.mcproductservice.model.Product;
import dev.shann.mcproductservice.repository.ProductRepository;
import dev.shann.mcproductservice.utility.ProductNotFoundException;
import dev.shann.mcproductservice.utility.UnAuthorizedAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProductService {

    private ProductRepository productRepository;


    //private JmsTemplate jmsTemplate;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        //this.jmsTemplate = jmsTemplate;
    }

    public boolean  userAuthentication(String email, String password) {
        var restTemplate = new RestTemplate();
        UserAuthenticationDTO userAuthenticationDTO  = new UserAuthenticationDTO(email, password);
        var responseEntity = restTemplate.postForEntity("http://localhost:8080/users/authenticate",userAuthenticationDTO, Boolean.class);
        return Boolean.TRUE.equals(responseEntity.getBody());
    }

    public List<Product> getAllProducts(String email, String password){
//        var verifiedUser = userAuthentication(email, password);
//        if(!verifiedUser)
//            throw  new UnAuthorizedAccessException("Invalid User");
        Pageable sortedByNameFirstPage = PageRequest.of(0,1, Sort.by("productName").descending());
        return productRepository.findAll("Samsung", sortedByNameFirstPage);
    }

    public Product getProduct(Long productId, String email, String password){
        var verifiedUser = userAuthentication(email, password);
        if(!verifiedUser)
            throw  new UnAuthorizedAccessException("Invalid User");
        return productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    }

    public Product createProduct(Product product, String email, String password){
        var verifiedUser = userAuthentication(email, password);
        if(!verifiedUser)
            throw  new UnAuthorizedAccessException("Invalid User");
        product = productRepository.save(product);
        //sendMail(product);
        return product;
    }

//    private void sendMail(Product product) {
//        // Send a message with a POJO - the template reuse the message converter
//        System.out.println("Sending an email message.");
//        this.jmsTemplate.convertAndSend("mailbox", product);
//    }


}
