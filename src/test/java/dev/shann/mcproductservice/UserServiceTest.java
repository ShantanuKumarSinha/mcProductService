package dev.shann.mcproductservice;

import dev.shann.mcproductservice.dto.UserAuthenticationDTO;
import dev.shann.mcproductservice.service.UserService;
import dev.shann.mcproductservice.utils.HttpConnectionWrapper;
import jakarta.ws.rs.HttpMethod;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//TODO
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String AUTHENTICATE = "/authenticate";

    private static final String USER_AUTHENTICATE = "http://localhost:8082/users/authenticate";

    @InjectMocks
    private UserService userService;

    @Mock
    private WebClient webClient;

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
    void shouldAuthenticateUserViaHttpConnection() throws IOException, URISyntaxException {
        var email = "test@test.com";
        var password = "Test@123";
//        when(url.openConnection()).thenReturn(httpURLConnection);
//        when(httpURLConnection.getOutputStream()).thenReturn(outputStream);
        URL url = new URI(USER_AUTHENTICATE).toURL();
        when(httpURLConnection.getResponseCode()).thenReturn(200);
        when(httpURLConnection.getInputStream()).thenReturn(inputStream);
        when(bufferedReader.readLine()).thenReturn(String.valueOf(TRUE));

        HttpConnectionWrapper wrapper = new HttpConnectionWrapper(url) {
            @Override
            protected HttpURLConnection openConnection(URL url) throws IOException {
                return httpURLConnection;
            }
        };
       when(httpURLConnection.getOutputStream()).thenReturn(outputStream);
//        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);

        var response = userService.userAuthenticationViaHttpConnection(email, password);
//        verify(outputStream).write(argumentCaptor.capture());
        assertThat(response).isInstanceOf(Boolean.class);
    }

    @Test
    @Disabled
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
            mockedUri.when(() -> new URI(USER_AUTHENTICATE)).thenReturn(uri); // Return the real URI

            // Act
            UserService userService = new UserService();
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

}
