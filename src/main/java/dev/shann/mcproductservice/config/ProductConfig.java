package dev.shann.mcproductservice.config;


import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfig {

//    @Value("${spring.activemq.broker-url}")
//    private String brokerUrl;
//
//    @Bean
//    public ConnectionFactory connectionFactory(){
//        ActiveMQConnectionFactory activeMQConnectionFactory  = new ActiveMQConnectionFactory();
//        activeMQConnectionFactory.setBrokerURL(brokerUrl);
//        return  activeMQConnectionFactory;
//    }
//
//    @Bean
//    public JmsTemplate jmsTemplate(){
//        JmsTemplate jmsTemplate = new JmsTemplate();
//        jmsTemplate.setConnectionFactory(connectionFactory());
//        jmsTemplate.setPubSubDomain(true);  // enable for Pub Sub to topic. Not Required for Queue.
//        return jmsTemplate;
//    }
//
//
//    @Bean
//    public ConnectionFactory connectionFactory(){
//        ActiveMQConnectionFactory activeMQConnectionFactory  = new ActiveMQConnectionFactory();
//        activeMQConnectionFactory.setBrokerURL(brokerUrl);
//        activeMQConnectionFactory.setTrustedPackages(Arrays.asList("com.mailshine.springbootstandaloneactivemq"));
//        return  activeMQConnectionFactory;
//    }
//    @Bean
//    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(){
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory());
//        factory.setPubSubDomain(true);
//        return factory;
//    }
}
