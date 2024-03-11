package dev.shann.mcproductservice.utility;

public class ProductNotFoundException extends IllegalArgumentException{

   public  ProductNotFoundException(){
      super("Product not Found");
    }
}
