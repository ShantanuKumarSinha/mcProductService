package dev.shann.mcproductservice.service;

import dev.shann.mcproductservice.dto.UserAuthenticationDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserServiceClient {

    public boolean  userAuthentication(String email, String password) {
        var url = "http://localhost:8080/users/authenticate";
        UserAuthenticationDTO userAuthenticationDTO  = new UserAuthenticationDTO(email, password);
        // var restTemplate = new RestTemplate();
        //var responseEntity = restTemplate.postForEntity(url,userAuthenticationDTO, Boolean.class);
        //return Boolean.TRUE.equals(responseEntity.getBody());
        var result = WebClient.builder()
                .baseUrl(url)
                .build().post()
                .body(Mono.just(userAuthenticationDTO), UserAuthenticationDTO.class)
                .retrieve().bodyToMono(Boolean.class).block();
        return  result;
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
        //dataOutputStream.writeBytes(getParam(parameters));
        return false;
    }

    private String getParam(String email, String password){
        StringBuilder result = new StringBuilder();
        return null;
    }
}
