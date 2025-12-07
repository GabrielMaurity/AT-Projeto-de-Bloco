package br.com.infnet.model;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record Product(
        UUID id,
        @NotBlank String name,
        @NotNull @Positive BigDecimal price,
        @Min(0) int stock,
        @NotNull Category category,
        UUID supplierId
) {
    // CORREÇÃO: Constante necessária para os testes antigos
    public static final Product NULL_PRODUCT = new Product(
            UUID.fromString("00000000-0000-0000-0000-000000000000"),
            "N/A", new BigDecimal("0.01"), 0, Category.FOOD, null
    );

    public static Product createSafeFallback() { return NULL_PRODUCT; }
}