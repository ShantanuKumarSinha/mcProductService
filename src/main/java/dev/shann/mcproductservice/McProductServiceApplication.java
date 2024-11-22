package dev.shann.mcproductservice;

import dev.shann.mcproductservice.mail.producer.EmailClient;
import dev.shann.mcproductservice.mail.producer.impl.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class McProductServiceApplication {

    @Value("${user.service.client.user.name}")
    private String url;
    @Value("${user.service.client.user.name-alt}")
    private String urlAlt;

    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private String port;

    public static void main(String[] args) {
        SpringApplication.run(McProductServiceApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().rootUri(url).build();
    }

    @Bean
    @LoadBalanced
    public WebClient webClient() {
        return WebClient.builder().baseUrl(urlAlt).build();
    }

    @Autowired
    private Environment environment;

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(port));
        // or setup like below
        mailSender.setHost(environment.getProperty("spring.mail.host", "localhost"));
        mailSender.setPort(environment.getProperty("spring.mail.port", Integer.class, 1025));
        return mailSender;
    }

    @Bean
    @LoadBalanced
    public EmailClient emailClient() {
        return new EmailService(mailSender());
    }

}
