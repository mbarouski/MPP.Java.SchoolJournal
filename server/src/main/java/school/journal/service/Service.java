package school.journal.service;

import com.google.common.reflect.TypeToken;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import school.journal.entity.Clazz;
import school.journal.entity.IId;
import school.journal.entity.Mark;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.service.exception.ServiceException;

import java.lang.reflect.Type;
import java.util.List;

public class Service<T> extends ServiceAbstractClass implements IService<T>{
    @Autowired
    private IRepository<T> repository;
    private static final Logger LOGGER = Logger.getLogger(Service.class);

    private final TypeToken<T> typeToken = new TypeToken<T>(getClass()) {};
    private final Class<? super T> type = typeToken.getRawType();

    protected Class<? super T> getType(){return type;}

    @Override
    public T create(T obj) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            obj = repository.create(obj, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return obj;
    }

    @Override
    public T update(T obj) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            obj = repository.update(obj, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return obj;
    }

    @Override
    public void delete(int id) throws ServiceException {
        try {
            T t = type.newInstance();
            ty;
        }
        Mark mark = new Mark();
        mark.setMarkId(id);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            repository.delete((T)mark, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
    }

    @Override
    public List<T> read() throws ServiceException {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            List<T> list;
            try {
                list = repository.query(null, session);
                transaction.commit();
            } catch (RepositoryException exc) {
                transaction.rollback();
                LOGGER.error(exc);
                throw new ServiceException(exc);
            } finally {
                session.close();
            }
            return list;
    }
}
