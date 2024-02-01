package dev.shann.mcproductservice.utility;

public class ProductNotFoundException extends IllegalArgumentException{

    ProductNotFoundException productNotFoundException(String msg){
        throw new IllegalArgumentException(msg);
    }
}
