package dev.shann.mcproductservice;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableJms
@EnableDiscoveryClient
public class McproductserviceApplication {

	@Value("${user.service.client.user.name}")
	private String url;
	@Value("${user.service.client.user.name-alt}")
	private String urlAlt;
	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate(){
		return new RestTemplateBuilder().rootUri(url).build();
	}
	@Bean
	@LoadBalanced
	public WebClient getWebClient(){
		return WebClient.builder().baseUrl(urlAlt).build();
	}

	@Bean
	@LoadBalanced
	public EmailClient getEmailClient(){
		return new EmailService();
	}

	public static void main(String[] args) {
		SpringApplication.run(McproductserviceApplication.class, args);
	}

}
