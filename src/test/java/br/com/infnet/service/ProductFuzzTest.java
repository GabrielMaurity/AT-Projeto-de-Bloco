package br.com.infnet.service;

import br.com.infnet.exception.BusinessException;
import br.com.infnet.model.Category;
import br.com.infnet.model.Product; // <--- Importante: Importar o Model
import net.jqwik.api.*;
import net.jqwik.api.constraints.StringLength;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.time.Duration;

class ProductFuzzTest {

    private final ProductService service = new ProductService();

    // --- FUZZ TESTING: Injeção de Dados Maliciosos/Estranhos ---

    @Property
    void fuzzingNameInputSafety(
            @ForAll @StringLength(min = 1, max = 1000) String weirdName,
            @ForAll Category category
    ) {
        try {

            Product productToFuzz = new Product(null, weirdName, BigDecimal.TEN, 10, category, null);

            service.create(productToFuzz);

        } catch (BusinessException e) {
            // Falha graciosa é aceitável (ex: se o nome for inválido pela regra de negócio)
            Assertions.assertNotNull(e.getMessage());
        } catch (Exception e) {
            // Falha catastrófica não é aceitável (NullPointer, Crash)
            Assertions.fail("O sistema quebrou com input fuzzing: " + weirdName);
        }
    }

    @Example
    void simulateSystemOverloadAndTimeout() {
        Assertions.assertTimeout(Duration.ofSeconds(4), () -> {
            service.simulateSlowDatabase(); // Demora 3s
        }, "O sistema deveria responder em até 4 segundos mesmo sob carga");
    }
}