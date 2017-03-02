package school.journal.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import school.journal.entity.Clazz;
import school.journal.entity.Mark;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.impl.MarkRepository;
import school.journal.repository.specification.Specification;
import school.journal.repository.specification.mark.MarkSpecificationByMarkId;
import school.journal.service.IMarkService;
import school.journal.service.ServiceAbstractClass;
import school.journal.service.exception.ServiceException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Component
public class MarkService extends ServiceAbstractClass implements IMarkService{
    @Autowired
    private IRepository<Mark> markIRepository;

    @Override
    public Mark create(Mark obj) throws ServiceException {
        if (obj.getValue() <= 0) {
            throw new ServiceException("Invalid value");
        } else {
            Calendar c = new GregorianCalendar();
            c.add(Calendar.DAY_OF_YEAR, 1);
            if (obj.getDate().after(c.getTime())) {
                throw new ServiceException("Invalid date");
            }
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            obj = markIRepository.create(obj, session);
            transaction.commit();
        } catch (RepositoryException e) {
            transaction.rollback();
            throw new ServiceException("Repository create exception", e);
        } finally {
            session.close();
        }
        return obj;
    }

    @Override
    public Mark update(Mark obj) throws ServiceException {
        if (obj.getMarkId() <= 0) {
            throw new ServiceException("Invalid id");
        } else if (obj.getValue() <= 0) {
            throw new ServiceException("Invalid value");
        } else {
            Calendar c = new GregorianCalendar();
            c.add(Calendar.DAY_OF_YEAR, 1);
            if (obj.getDate().after(c.getTime())) {
                throw new ServiceException("Invalid date");
            }
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            obj = markIRepository.update(obj, session);
            transaction.commit();
        } catch (RepositoryException e) {
            transaction.rollback();
            throw new ServiceException("Repository update exception", e);
        } finally {
            session.close();
        }
        return obj;
    }

    @Override
    public Mark delete(Mark obj) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            obj = markIRepository.delete(obj, session);
            transaction.commit();
        } catch (RepositoryException e) {
            transaction.rollback();
            throw new ServiceException("Repository delete exception", e);
        } finally {
            session.close();
        }
        return obj;
    }

    @Override
    public List<Mark> read() throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Mark> markList;
        try {
            markList = markIRepository.query(null, session);
            transaction.commit();
        } catch (RepositoryException e) {
            transaction.rollback();
            throw new ServiceException("Repository read exception", e);
        } finally {
            session.close();
        }
        return markList;
    }

}
