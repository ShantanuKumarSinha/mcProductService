package dev.shann.mcproductservice;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import dev.shann.mcproductservice.service.UserService;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//TODO
@ExtendWith(MockitoExtension.class)
@WireMockTest()
class UserServiceTest {

    CloseableHttpClient httpClient;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        httpClient = HttpClientBuilder.create().useSystemProperties().build();
    }


    @Test
    void testUserAuthentication() {
        stubFor(get("/authenticate")
                .willReturn(ok(Boolean.TRUE.toString())));
        var response = userService.userAuthentication("test@test.com", "Test@123");
        verify(1,getRequestedFor(urlEqualTo("/authenticate")));
        assertThat(response).isTrue();
    }

}
