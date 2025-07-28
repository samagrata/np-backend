package org.samagrata.npbackend.service;

import java.util.List;
import java.util.Optional;

import org.samagrata.npbackend.exception.ResourceNotFoundException;

public interface CrudService<T, IDT> {
    List<T> getAll();
    Optional<T> get(IDT id);
    T create(T entity);
    T update(IDT id, T entity) throws ResourceNotFoundException;
    void delete(IDT id) throws ResourceNotFoundException;
}
