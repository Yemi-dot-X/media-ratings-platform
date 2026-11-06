package at.fhtw.mrp.repository;

import java.util.List;

public interface Repository<T, ID> {
    // Returns null when the ID does not exist.
    T create(T entity);
    T readById(ID id);

    List<T> readAll();

    // Returns false when the ID does not exist.
    boolean deleteById(ID id);

    //Returns null when the ID does not exist.
    T updateById(ID id, T entity);

}
