package school.journal.repository;

import org.hibernate.Session;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.HibernateSpecification;

import java.util.List;

public interface IRepository<T> {
    T create(T t, Session session) throws RepositoryException;
    T update(T t, Session session) throws RepositoryException;
    T delete(T t, Session session) throws RepositoryException;

    List<T> query(HibernateSpecification specification, Session session) throws RepositoryException;
    T get(int id, Session session) throws RepositoryException;
}
