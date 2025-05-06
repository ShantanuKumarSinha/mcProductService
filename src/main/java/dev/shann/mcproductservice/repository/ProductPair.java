package dev.shann.mcproductservice.repository;

// This is a projection interface to extract only selected fields from JPA query without using Query Annotations
public interface ProductPair {

    String getProductName();

    Double getPrice();
}
