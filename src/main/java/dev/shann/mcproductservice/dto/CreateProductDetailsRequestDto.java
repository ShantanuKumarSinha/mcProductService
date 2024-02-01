package dev.shann.mcproductservice.dto;


import dev.shann.mcproductservice.model.Product;
import jakarta.annotation.Nonnull;

public record CreateProductDetailsRequestDto(@Nonnull String emailId, @Nonnull String password, Product product){ }
