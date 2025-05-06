package dev.shann.mcproductservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import dev.shann.mcproductservice.dto.CreateOrUpdateProductDetailsRequestDto;
import dev.shann.mcproductservice.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
@TestPropertySource(locations = "file:src/test/resources/application-test.properties")
//@TestPropertySource(("classpath:application-test.properties"))
class McProductServiceApplicationTests {

    WireMockServer wireMockServer;
    @Value("${user.service.client.user.name-alt}")
    private String urlAlt;

//    @Autowired
//    MockMvc mockMvc;

//    @Autowired
//    private ObjectMapper objectMapper;

    @Bean
    @LoadBalanced
    public WebClient webClient() {
        return WebClient.builder().baseUrl(urlAlt).build();
    }

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(8080);
    }

    @Test
    @Disabled
    void testProductCreation() throws Exception {
//        var product = Product.builder().productName("testProduct").brand("testBrand").price(100).quantity(1000).build();
//        var createOrUpdateProductDetailsRequestDto = new CreateOrUpdateProductDetailsRequestDto("test@test.com", "testPassword", product);
//        var jsonStringify = objectMapper.writeValueAsString(createOrUpdateProductDetailsRequestDto);
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("http://product-service/products").content(jsonStringify).contentType(MediaType.APPLICATION_JSON);
//        var result = mockMvc.perform(requestBuilder).andExpect(status().isCreated()).andReturn();
//        var actualProduct = objectMapper.readValue(result.getResponse().getContentAsString(), Product.class);
//        assertSoftly(softly -> softly.assertThat(actualProduct).extracting(Product::getProductId, Product::getProductName, Product::getBrand, Product::getPrice, Product::getQuantity).contains(1L, "testProduct", "testBrand", 100, 1000));


    }

}
