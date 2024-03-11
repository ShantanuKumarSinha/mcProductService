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

@Component
@Slf4j
public class UserServiceClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WebClient webClient;

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
                byte[] input = getParam(email, password).getBytes("utf-8");
                os.write(input, 0, input.length);
                os.flush();
                os.close();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"));
            var response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString().equals("true");
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch(MalformedURLException malformedURLException){
           throw  new RuntimeException(malformedURLException);
        } catch(IllegalArgumentException illegalArgumentException){
           throw new RuntimeException(illegalArgumentException);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

        private String getParam(String email, String password) throws JsonProcessingException {
        //jsonInputString = "{\"email\":\"shan.raj93@gmail.com\",\"password\":\"test@123\"}";
        return objectMapper.writeValueAsString(new UserAuthenticationDTO(email,password));
    }
}
