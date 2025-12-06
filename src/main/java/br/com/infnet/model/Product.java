package br.com.infnet.model;

import br.com.infnet.exception.InvalidProductException;
import java.math.BigDecimal;
import java.util.UUID;

// Imutabilidade (Record)
public record Product(UUID id, String name, BigDecimal price, int stock, Category category) {

    // Null Object Pattern
    public static final Product NULL_PRODUCT = new Product(
            UUID.fromString("00000000-0000-0000-0000-000000000000"),
            "N/A", new BigDecimal("0.01"), 0, Category.FOOD
    );

    // Construtor Compacto para Validação (Fail-fast)
    public Product {
        if (name == null || name.isBlank()) {
            throw new InvalidProductException("Nome não pode ser vazio.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductException("Preço deve ser maior que zero.");
        }
        if (stock < 0) {
            throw new InvalidProductException("Estoque não pode ser negativo.");
        }
        if (category == null) {
            throw new InvalidProductException("Categoria é obrigatória.");
        }
    }
}