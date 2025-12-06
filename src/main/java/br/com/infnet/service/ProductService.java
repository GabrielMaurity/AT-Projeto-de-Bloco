package br.com.infnet.service;

import br.com.infnet.exception.InvalidProductException;
import br.com.infnet.model.Category;
import br.com.infnet.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ProductService {

    private final Map<UUID, Product> repository = new ConcurrentHashMap<>();

    // COMMAND: Create (Retorna ID apenas)
    public UUID createProduct(String name, BigDecimal price, int stock, Category category) {
        UUID id = UUID.randomUUID();
        Product newProduct = new Product(id, name, price, stock, category);
        repository.put(id, newProduct);
        return id;
    }

    // QUERY: Read
    public Product getProductById(UUID id) {
        return repository.getOrDefault(id, Product.NULL_PRODUCT);
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(repository.values());
    }

    // COMMAND: Update (Void - CQS)
    public void updateProduct(UUID id, String name, BigDecimal price, int stock, Category category) {
        if (!repository.containsKey(id)) {
            throw new InvalidProductException("Produto não encontrado para atualização.");
        }
        // Substitui por nova instância imutável
        Product updatedProduct = new Product(id, name, price, stock, category);
        repository.put(id, updatedProduct);
    }

    // COMMAND: Delete
    public void deleteProduct(UUID id) {
        if (repository.remove(id) == null) {
            throw new InvalidProductException("Produto não encontrado para exclusão.");
        }
    }

    // Switch Exaustivo (Java 17)
    public String getCategoryLabel(Product product) {
        if (product.equals(Product.NULL_PRODUCT)) return "Produto Inválido";

        return switch (product.category()) {
            case ELECTRONICS -> "Setor Azul: Eletrônicos";
            case CLOTHING    -> "Setor Vermelho: Vestuário";
            case BOOKS       -> "Setor Verde: Livraria";
            case FOOD        -> "Setor Amarelo: Alimentos";
        };
    }
}