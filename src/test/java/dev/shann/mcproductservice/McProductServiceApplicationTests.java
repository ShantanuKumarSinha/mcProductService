package dev.shann.mcproductservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
@TestPropertySource(locations = "file:src/test/java/resources/application-test.properties")
class McProductServiceApplicationTests {

    @Value("${user.service.client.user.name-alt}")
    private String urlAlt;
    @Bean
    @LoadBalanced
    public WebClient webClient() {
        return WebClient.builder().baseUrl(urlAlt).build();
    }

}
