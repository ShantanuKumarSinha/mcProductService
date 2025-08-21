package dev.shann.mcproductservice.service;

/**
 * UserService interface for user authentication operations.
 * This interface defines methods for authenticating users using different approaches:
 * WebClient, RestTemplate, FeignClient, and HttpConnection.
 */
public interface UserService {
    /**
     * Verify user using WebClient
     *
     * @param email
     * @param password
     * @return Boolean Type
     */
    boolean userAuthentication(String email, String password);

    /**
     * Calls UserAuthentication RestTemplate
     *
     * @param email
     * @param password
     * @return Boolean type
     */
    boolean userAuthenticationViaRestTemplate(String email, String password);

    /**
     * Calls UserAuthentiactionFeignClient
     *
     * @param email
     * @param password
     * @return Boolean type
     */
    boolean userAuthenticationViaFeignClient(String email, String password);

    /**
     * Calls UserAuthentication via HttpConnection
     *
     * @param email
     * @param password
     * @return Boolean type
     */
    boolean userAuthenticationViaHttpConnection(String email, String password);
}
