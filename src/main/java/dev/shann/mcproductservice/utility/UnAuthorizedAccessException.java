package dev.shann.mcproductservice.utility;

public class UnAuthorizedAccessException extends IllegalArgumentException{

    public UnAuthorizedAccessException(String invalidUser) {
        super(invalidUser);
    }
}
