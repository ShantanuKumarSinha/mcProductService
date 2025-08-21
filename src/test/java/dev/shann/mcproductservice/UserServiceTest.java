package dev.shann.mcproductservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import dev.shann.mcproductservice.dto.UserAuthenticationDTO;
import dev.shann.mcproductservice.service.UserService;
import dev.shann.mcproductservice.service.impl.UserServiceImpl;
import dev.shann.mcproductservice.utils.UserServiceClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import static dev.shann.mcproductservice.utils.ApplicationConstants.AUTHENTICATE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

//TODO
// May be try using WebTestClient also
@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-test.properties")
class UserServiceTest {

    private static WireMockServer wireMockServer;

    private String USER_AUTHENTICATE = "http://localhost:8082/users";

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private WebClient webClient;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private UserService userService = new UserServiceImpl(restTemplate, webClient, userServiceClient, objectMapper);

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUri;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private URL url;

    @Mock
    private HttpURLConnection httpURLConnection;

    @Mock
    private OutputStream outputStream;

    @Mock
    private InputStream inputStream;

    @Mock
    private BufferedReader bufferedReader;


    @Test
    void shouldAuthenticateUser() {
        var email = "test@test.com";
        var password = "Test@123";
        when(webClient.post()).thenReturn(requestBodyUri);
        when(requestBodyUri.uri(AUTHENTICATE)).thenReturn(requestBodySpec);
        when(requestBodySpec
                .body(any(Mono.class), eq(UserAuthenticationDTO.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(TRUE));
        var response = userService.userAuthentication(email, password);
        assertThat(response).isTrue();
    }

    @Test
    void shouldNotAuthenticateUser() {
        var email = "test@test.com";
        var password = "Test@123";
        when(webClient.post()).thenReturn(requestBodyUri);
        when(requestBodyUri.uri(AUTHENTICATE)).thenReturn(requestBodySpec);
        when(requestBodySpec
                .body(any(Mono.class), eq(UserAuthenticationDTO.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(FALSE));
        var response = userService.userAuthentication(email, password);
        assertThat(response).isFalse();
    }

    @Test
    @Disabled
        // TODO
    void testUserAuthenticationViaHttpConnection_success() throws Exception {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        String responseBody = "true";

        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        URL mockUrl = mock(URL.class);

        // Mock URL and HttpURLConnection behavior
        URI uri = new URI(USER_AUTHENTICATE); // Create a real URI
        when(mockUrl.openConnection()).thenReturn(mockConnection);
        when(mockConnection.getOutputStream()).thenReturn(mock(OutputStream.class));
        when(mockConnection.getInputStream()).thenReturn(
                new ByteArrayInputStream(responseBody.getBytes())
        );

        try (MockedStatic<URI> mockedUri = mockStatic(URI.class)) {
            mockedUri.when(() -> new URI(USER_AUTHENTICATE)).thenReturn(TRUE); // Return the real URI

            // Act
            boolean result = userService.userAuthenticationViaHttpConnection(email, password);

            // Assert
            assertTrue(result);

            // Verify interactions
            verify(mockConnection).setRequestMethod("POST");
            verify(mockConnection).setRequestProperty("Content-Type", "application/json");
            verify(mockConnection).setRequestProperty("Accept", "application/json");
            verify(mockConnection).setDoOutput(true);
            verify(mockConnection).setDoInput(true);
            verify(mockConnection).getOutputStream();
            verify(mockConnection).getInputStream();
        }
    }

    @Test
    @Disabled
        //TODO
    void testUserAuthenticationStub() {
        wireMockServer = new WireMockServer(
                WireMockConfiguration.wireMockConfig().port(8080));
        wireMockServer.start();
        wireMockServer.stubFor(WireMock.post(AUTHENTICATE));
        assertThat(wireMockServer.port()).isEqualTo(8080);

    }

}
