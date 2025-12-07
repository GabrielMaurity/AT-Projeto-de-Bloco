package br.com.infnet.service;

import br.com.infnet.exception.BusinessException;
import br.com.infnet.model.Supplier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SupplierService implements CrudService<Supplier> {

    private final Map<UUID, Supplier> repository = new ConcurrentHashMap<>();

    @Override
    public List<Supplier> getAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public Supplier getById(UUID id) {
        return repository.get(id);
    }

    @Override
    public void create(Supplier supplier) {
        UUID id = UUID.randomUUID();
        // Preserva o ID se já vier (para testes) ou gera novo
        UUID finalId = (supplier.id() != null) ? supplier.id() : id;
        repository.put(finalId, new Supplier(finalId, supplier.companyName(), supplier.email()));
    }

    @Override
    public void update(UUID id, Supplier supplier) {
        if (!repository.containsKey(id)) throw new BusinessException("Fornecedor não encontrado");
        repository.put(id, new Supplier(id, supplier.companyName(), supplier.email()));
    }

    @Override
    public void delete(UUID id) {
        repository.remove(id);
    }
}