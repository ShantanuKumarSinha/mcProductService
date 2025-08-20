package dev.shann.mcproductservice.projections;

// This is a projection interface to extract only selected fields from JPA query without using Query Annotations
public interface ProductNameAndPrice {

    String getProductName();

    Double getPrice();
}
