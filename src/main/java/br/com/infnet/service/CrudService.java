package br.com.infnet.service;

import java.util.List;
import java.util.UUID;

// Interface genérica para padronizar os serviços (Refatoração/Clean Code)
public interface CrudService<T> {
    List<T> getAll();
    T getById(UUID id);
    void create(T entity);
    void update(UUID id, T entity);
    void delete(UUID id);
}