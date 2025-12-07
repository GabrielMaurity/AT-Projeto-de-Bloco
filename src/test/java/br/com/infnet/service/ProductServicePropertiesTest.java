package br.com.infnet.service;

import br.com.infnet.exception.BusinessException;
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
        // CORREÇÃO: Instancia o objeto antes de passar pro serviço
        Product p = new Product(null, name, price, stock, category, null);
        service.create(p);

        boolean exists = service.getAll().stream().anyMatch(prod -> prod.name().equals(name));
        assert exists;
    }

    @Property
    void validProductCanBeUpdated(
            @ForAll @AlphaChars @StringLength(min = 1, max = 20) String name,
            @ForAll @BigRange(min = "0.01", max = "10000.00") BigDecimal price,
            @ForAll @IntRange(min = 0, max = 1000) int stock,
            @ForAll Category category
    ) {
        UUID id = UUID.randomUUID();
        Product original = new Product(id, "Original", BigDecimal.TEN, 10, Category.FOOD, null);
        service.create(original);

        Product update = new Product(id, name, price, stock, category, null);
        service.update(id, update);

        Product retrieved = service.getById(id);
        assert retrieved != null;
        assert retrieved.name().equals(name);
    }

    @Provide
    Arbitrary<UUID> validUUIDs() {
        return Arbitraries.randomValue(random -> new UUID(random.nextLong(), random.nextLong()));
    }
}