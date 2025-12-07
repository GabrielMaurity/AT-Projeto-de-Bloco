package br.com.infnet.service;

import br.com.infnet.exception.BusinessException;
import br.com.infnet.model.Category;
import br.com.infnet.model.Product;
import net.jqwik.api.*;
import net.jqwik.api.constraints.StringLength;
import org.junit.jupiter.api.Assertions;
import java.math.BigDecimal;
import java.time.Duration;

class ProductFuzzTest {
    private final ProductService service = new ProductService();

    @Property
    void fuzzingNameInputSafety(@ForAll @StringLength(min = 1, max = 1000) String weirdName, @ForAll Category category) {
        try {
            // CORREÇÃO: Cria o objeto com os dados "estranhos" do Fuzzing
            Product p = new Product(null, weirdName, BigDecimal.TEN, 10, category, null);
            service.create(p);
        } catch (BusinessException e) {
            Assertions.assertNotNull(e.getMessage());
        } catch (Exception e) {
            Assertions.fail("O sistema quebrou com input fuzzing: " + weirdName);
        }
    }

    @Example
    void simulateSystemOverloadAndTimeout() {
        Assertions.assertTimeout(Duration.ofSeconds(4), service::simulateSlowDatabase);
    }
}