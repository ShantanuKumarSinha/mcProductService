package dev.shann.mcproductservice.utility;

public class UnAuthorizedAccessException extends IllegalArgumentException{

    public UnAuthorizedAccessException(String invalidUser) {throw new IllegalArgumentException(invalidUser);
    }
}
