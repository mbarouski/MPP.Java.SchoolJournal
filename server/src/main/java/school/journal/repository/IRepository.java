package school.journal.repository;

import school.journal.repository.exception.RepositoryException;

import java.util.List;

public interface IRepository<T> {
    T create(T t) throws RepositoryException;
    T update(T t) throws RepositoryException;
    List<T> read() throws RepositoryException;
    T read(int id) throws RepositoryException;
    T delete(int id) throws RepositoryException;
}
