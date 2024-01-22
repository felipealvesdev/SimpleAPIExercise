package com.example.stock.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductDTO(@NotBlank String name, @NotBlank String model, @NotNull Double price, @NotNull Integer quantity) {
}
