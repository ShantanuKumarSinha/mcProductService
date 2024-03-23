package dev.shann.mcproductservice.service;

import dev.shann.mcproductservice.dto.UserAuthenticationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="user-service", url = "http://localhost:8082/users")
public interface UserServiceClient {
    public static final String AUTHENTICATE =  "/authenticate";

    @PostMapping(value = AUTHENTICATE)
    public Boolean authenticateUser(@RequestBody UserAuthenticationDTO userAuthenticationDTO);
}
