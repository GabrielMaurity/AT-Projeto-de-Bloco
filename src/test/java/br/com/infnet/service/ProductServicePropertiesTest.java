package br.com.infnet.service;

import br.com.infnet.exception.InvalidProductException;
import br.com.infnet.model.Category;
import br.com.infnet.model.Product;
import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

class ProductServicePropertiesTest {

    private final ProductService service = new ProductService();

    @Property
    void validProductCanBeCreatedAndRetrieved(
            @ForAll @AlphaChars @StringLength(min = 1, max = 20) String name,
            @ForAll @BigRange(min = "0.01", max = "10000.00") BigDecimal price,
            @ForAll @IntRange(min = 0, max = 1000) int stock,
            @ForAll Category category
    ) {
        UUID id = service.createProduct(name, price, stock, category);
        Product retrieved = service.getProductById(id);

        assert retrieved.name().equals(name);
        assert retrieved.price().compareTo(price) == 0;
        assert retrieved.stock() == stock;
        assert retrieved.category() == category;
        assert !retrieved.equals(Product.NULL_PRODUCT);
    }

    @Property
    void invalidPriceThrowsException(
            @ForAll @AlphaChars String name,
            @ForAll @BigRange(max = "0.00") BigDecimal invalidPrice,
            @ForAll int stock,
            @ForAll Category category
    ) {
        try {
            service.createProduct(name, invalidPrice, stock, category);
            throw new AssertionError("Deveria ter falhado com preço inválido");
        } catch (InvalidProductException e) {
            // Sucesso
        }
    }

    @Property
    void invalidStockThrowsException(
            @ForAll @AlphaChars String name,
            @ForAll @BigRange(min = "1.00") BigDecimal price,
            // CORREÇÃO AQUI: Definimos o min explicitamente para evitar erro de range
            @ForAll @IntRange(min = Integer.MIN_VALUE, max = -1) int invalidStock,
            @ForAll Category category
    ) {
        try {
            service.createProduct(name, price, invalidStock, category);
            throw new AssertionError("Deveria ter falhado com estoque negativo");
        } catch (InvalidProductException e) {
            // Sucesso
        }
    }

    // CORREÇÃO AQUI: Usamos @ForAll("validUUIDs") para chamar o método provider abaixo
    @Property
    void updatingNonExistentProductThrowsException(@ForAll("validUUIDs") UUID randomId) {
        try {
            service.updateProduct(randomId, "Nome", BigDecimal.ONE, 10, Category.FOOD);
            throw new AssertionError("Deveria falhar ao atualizar ID inexistente");
        } catch (InvalidProductException e) {
            // Sucesso
        }
    }

    // Método Provider para ensinar o Jqwik a criar UUIDs
    @Provide
    Arbitrary<UUID> validUUIDs() {
        return Arbitraries.randomValue(random -> new UUID(random.nextLong(), random.nextLong()));
    }
}