package dev.shann.mcproductservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class McproductserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(McproductserviceApplication.class, args);
	}

}
