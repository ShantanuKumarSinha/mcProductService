package dev.shann.mcproductservice.service;

import dev.shann.mcproductservice.dto.UserAuthenticationDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Slf4j
public class UserServiceClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WebClient webClient;

    Logger log =  LoggerFactory.getLogger("UserServiceClient.class");

    private static final String AUTHENTICATE =  "/authenticate";

    String jsonInputString = "\"{\"users\": { \"email\": \"testable123@gmail.com\", \"password\": \"test@123\"} }\";";


    public boolean  userAuthentication(String email, String password) {
        UserAuthenticationDTO userAuthenticationDTO  = new UserAuthenticationDTO(email, password);
//        var responseEntity = restTemplate.postForEntity(AUTHENTICATE,userAuthenticationDTO, Boolean.class);
//        return Boolean.TRUE.equals(responseEntity.getBody());
        try{
            return Boolean.TRUE.equals(webClient
                    .post()
                    .uri(AUTHENTICATE)
                    .body(Mono.just(userAuthenticationDTO), UserAuthenticationDTO.class)
                    .retrieve().bodyToMono(Boolean.class).block());
        } catch(WebClientException webClientException){
            log.error("Exception : {}",webClientException.getMessage());
        }

        return  false;
    }
    public boolean userAuthenticationViaHttpConnection(String email, String password) throws IOException {
        var url = new URL("http://localhost:8080/users/authenticate");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type",
                "application/json");
        connection.setRequestProperty("Accept",
                "application/json");
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        dataOutputStream.writeBytes(jsonInputString);
        return false;
    }

    private String getParam(String email, String password){
        StringBuilder result = new StringBuilder();
        return null;
    }
}
