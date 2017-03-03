package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import school.journal.entity.Clazz;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.clazz.ClazzSpecificationByClazzId;
import school.journal.service.ICLassService;
import school.journal.service.ServiceAbstractClass;
import school.journal.service.exception.ServiceException;

import java.util.LinkedList;
import java.util.List;

@Component
public class ClassService extends ServiceAbstractClass implements ICLassService {
    @Autowired
    private IRepository<Clazz> clazzRepository;
    private static final Logger LOGGER = Logger.getLogger(ClassService.class);

    @Override
    public List<Clazz> read() throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Clazz> clazzList;
        try {
            clazzList = clazzRepository.query(null, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return clazzList != null ? clazzList : new LinkedList<Clazz>();
    }

    @Override
    public Clazz create(Clazz clazz) throws ServiceException {
        validateClassNumber(clazz.getNumber());
        validateLetterMark(clazz.getLetterMark());
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            clazz = clazzRepository.create(clazz, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return clazz;
    }

    @Override
    public Clazz update(Clazz clazz) throws ServiceException {
        validateId(clazz.getClassId());
        validateClassNumber(clazz.getNumber());
        validateLetterMark(clazz.getLetterMark());
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            clazz = clazzRepository.update(clazz, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return clazz;
    }

    @Override
    public void delete(int id) throws ServiceException {
        validateId(id);
        Clazz clazz = new Clazz();
        clazz.setClassId(id);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            clazzRepository.delete(clazz, session);
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
    public Clazz getOne(int id) throws ServiceException {
        validateId(id);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Clazz> clazzList = clazzRepository.query(
                    new ClazzSpecificationByClazzId(id), session);
            transaction.commit();
            return clazzList.size() > 0 ? clazzList.get(0) : null;
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
    }


    private void validateId(int id) throws ServiceException {
        if (id <= 0) throw new ServiceException("Invalid Id");
    }

    private void validateClassNumber(int number) throws ServiceException {
        if (number <= 0 || number >= 12)
            throw new ServiceException("Invalid class number");
    }

    private void validateLetterMark(String letterMark) throws ServiceException {
        if (letterMark.isEmpty())
            throw new ServiceException("Invalid letter mark");
    }

}
