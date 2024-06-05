package iss.group.application.Repository;

import java.util.Collection;

public interface IRepository<ID, E> {

    E findById(ID id);
    Collection<E> findAll();
    void save(E entity);
    void delete(ID id);
    void update(E entity);
}
