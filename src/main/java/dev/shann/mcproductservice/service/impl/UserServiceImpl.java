package dev.shann.mcproductservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.shann.mcproductservice.dto.UserAuthenticationDTO;
import dev.shann.mcproductservice.service.UserService;
import dev.shann.mcproductservice.utils.UserServiceClient;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static dev.shann.mcproductservice.utils.ApplicationConstants.AUTHENTICATE;

/**
 * Implementation of UserService
 * </p>
 * This class provides methods to authenticate users using various methods such as WebClient, RestTemplate, Feign Client, and HttpURLConnection.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    RestTemplate restTemplate;

    WebClient webClient;

    UserServiceClient userServiceClient;

    private ObjectMapper objectMapper;

    /**
     * User service URL from application properties
     */
    @Value("${user.service.client.user.name-alt}")
    private String userServiceURL;

    private String userAuthenticate;

    public UserServiceImpl(RestTemplate restTemplate, WebClient webClient, UserServiceClient userServiceClient, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.webClient = webClient;
        this.userServiceClient = userServiceClient;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    private void initUserAuthenticate() {
        userAuthenticate = userServiceURL + AUTHENTICATE;
    }

    /**
     * Verify user using WebClient
     *
     * @param email
     * @param password
     * @return Boolean Type
     */
    @Override
    public boolean userAuthentication(String email, String password) {
        UserAuthenticationDTO userAuthenticationDTO = new UserAuthenticationDTO(email, password);
        try {
            return Boolean.TRUE.equals(webClient
                    .post()
                    .uri(AUTHENTICATE)
                    .body(Mono.just(userAuthenticationDTO), UserAuthenticationDTO.class)
                    .retrieve().bodyToMono(Boolean.class).block());
        } catch (WebClientException webClientException) {
            log.error("Exception : {}", webClientException.getMessage());
        }
        return false;
    }

    /**
     * Calls UserAuthentication RestTemplate
     *
     * @param email
     * @param password
     * @return Boolean type
     */
    @Override
    public boolean userAuthenticationViaRestTemplate(String email, String password) {
        UserAuthenticationDTO userAuthenticationDTO = new UserAuthenticationDTO(email, password);
        try {
            return Boolean.TRUE.equals(restTemplate.postForObject(AUTHENTICATE, userAuthenticationDTO, Boolean.class));
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage());
        }
        return false;
    }

    /**
     * Calls UserAuthentiactionFeignClient
     *
     * @param email
     * @param password
     * @return Boolean type
     */
    @Override
    public boolean userAuthenticationViaFeignClient(String email, String password) {
        return userServiceClient.authenticateUser(new UserAuthenticationDTO(email, password));
    }

    /**
     * Calls UserAuthentication via HttpConnection
     *
     * @param email
     * @param password
     * @return Boolean type
     */
    @Override
    public boolean userAuthenticationViaHttpConnection(String email, String password) {

        try {
            URL url = new URI(userAuthenticate).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HttpMethod.POST);
            connection.setRequestProperty("Content-Type",
                    MediaType.APPLICATION_JSON_VALUE);
            connection.setRequestProperty("Accept", MediaType.APPLICATION_JSON_VALUE);
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
        return objectMapper.writeValueAsString(new UserAuthenticationDTO(email, password));
    }
}
