package dev.shann.mcproductservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class TestConfig {


    @Autowired
    private Environment environment;

//    @Bean
//    public JavaMailSender mailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost(environment.getProperty("spring.mail.host", "localhost"));
//        mailSender.setPort(environment.getProperty("spring.mail.port", Integer.class, 1025));
//        return mailSender;
//    }

}
