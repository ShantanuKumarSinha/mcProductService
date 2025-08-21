package dev.shann.mcproductservice.utils;

import dev.shann.mcproductservice.dto.UserAuthenticationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static dev.shann.mcproductservice.utils.ApplicationConstants.AUTHENTICATE;

@FeignClient(name="user-service", url ="${user.service.client.user.name-alt}")
public interface UserServiceClient {

    @PostMapping(value = AUTHENTICATE)
    public Boolean authenticateUser(@RequestBody UserAuthenticationDTO userAuthenticationDTO);
}
