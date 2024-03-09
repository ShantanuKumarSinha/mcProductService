package dev.shann.mcproductservice.dto;


import dev.shann.mcproductservice.model.Product;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;

public record CreateProductDetailsRequestDto(@Nonnull String emailId, @Nonnull String password, @Valid Product product){ }
