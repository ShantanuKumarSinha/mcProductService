package dev.shann.mcproductservice.exceptions;

public class ProductNotFoundException extends IllegalArgumentException{

   public  ProductNotFoundException(){
      super("Product Not Found");
    }
}
