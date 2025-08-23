package dev.shann.mcproductservice.exceptions;

public class UnAuthorizedAccessException extends IllegalArgumentException {

    public UnAuthorizedAccessException(String invalidUser) {
        super(invalidUser);
    }
}
