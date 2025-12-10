package br.com.infnet.service;

import br.com.infnet.exception.BusinessException;
import br.com.infnet.model.Product;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProductService implements CrudService<Product> { // Refatoração: Interface

    private final Map<UUID, Product> repository = new ConcurrentHashMap<>();

    @Override
    public void create(Product p) {
        if("ERROR".equalsIgnoreCase(p.name())) {
            throw new BusinessException("Simulação de erro crítico!");
        }

        UUID id = (p.id() != null) ? p.id() : UUID.randomUUID();
        // Salva com o supplierId
        Product newProduct = new Product(id, p.name(), p.price(), p.stock(), p.category(), p.supplierId());
        repository.put(id, newProduct);
    }

    @Override
    public List<Product> getAll() { return new ArrayList<>(repository.values()); }

    @Override
    public Product getById(UUID id) { return repository.get(id); }

    @Override
    public void delete(UUID id) { repository.remove(id); }

    @Override
    public void update(UUID id, Product p) {
        if (!repository.containsKey(id)) throw new BusinessException("Produto não encontrado.");
        repository.put(id, new Product(id, p.name(), p.price(), p.stock(), p.category(), p.supplierId()));
    }

    public void simulateSlowDatabase() {
        try { Thread.sleep(3000); } catch (InterruptedException e) {}
    }
}