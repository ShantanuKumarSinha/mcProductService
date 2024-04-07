package dev.shann.mcproductservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.shann.mcproductservice.dto.UserAuthenticationDTO;
import jakarta.ws.rs.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class UserService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WebClient webClient;

    @Autowired
    UserServiceClient userServiceClient;

    private static final String AUTHENTICATE =  "/authenticate";

    private static final String USER_AUTHENTICATE = "http://localhost:8082/users/authenticate";

    @Autowired
    public ObjectMapper objectMapper;


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
    public boolean userAuthenticationViaFeignClient(String email, String password){
    return userServiceClient.authenticateUser(new UserAuthenticationDTO(email, password));
    }

    public boolean userAuthenticationViaHttpConnection(String email, String password) {

        try {
            URL url = new URI(USER_AUTHENTICATE).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HttpMethod.POST);
            connection.setRequestProperty("Content-Type",
                    MediaType.APPLICATION_JSON_VALUE);
            connection.setRequestProperty("Accept",MediaType.APPLICATION_JSON_VALUE);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream os = connection.getOutputStream();
                byte[] input = getParam(email, password).getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
                os.flush();
                os.close();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            var response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString().equals("true");
        } catch (IllegalArgumentException | IOException | URISyntaxException e) {
            log.error(String.valueOf(e));
            return false;
        }
    }

        private String getParam(String email, String password) throws JsonProcessingException {
        //jsonInputString = "{\"email\":\"shan.raj93@gmail.com\",\"password\":\"test@123\"}";
        return objectMapper.writeValueAsString(new UserAuthenticationDTO(email,password));
    }
}
