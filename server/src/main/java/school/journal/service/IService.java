package school.journal.service;

import school.journal.service.exception.ServiceException;

import java.util.List;

public interface IService<T> {
    T create(T obj) throws ServiceException;

    T update(T obj) throws ServiceException;

    void delete(int id) throws ServiceException;

    List<T> read() throws ServiceException;
}
