package school.journal.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import school.journal.entity.Clazz;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.clazz.ClazzSpecification;
import school.journal.repository.specification.clazz.ClazzSpecificationByClazzId;
import school.journal.service.ICLassService;
import school.journal.service.ServiceAbstractClass;
import school.journal.service.exception.ServiceException;

import java.util.List;

@Component
public class ClassService extends ServiceAbstractClass implements ICLassService {
    @Autowired
    private IRepository<Clazz> clazzIRepository;

    @Override
    public List<Clazz> getClasses() throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Clazz> clazzList = clazzIRepository.query(null, session);
            transaction.commit();
            return clazzList;
        } catch (RepositoryException e) {
            transaction.rollback();
            throw new ServiceException("Repository query exception", e);
        } finally {
            session.close();
        }
    }

    @Override
    public Clazz createClass(Clazz clazz) throws ServiceException {
        if (clazz.getNumber() <= 0) {
            throw new ServiceException("Invalid class number");
        } else if (clazz.getLetterMark().isEmpty()) {
            throw new ServiceException("Invalid letter mark");
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            clazz = clazzIRepository.create(clazz, session);
            transaction.commit();
            return clazz;
        } catch (RepositoryException e) {
            transaction.rollback();
            throw new ServiceException("Repository create exception", e);
        } finally {
            session.close();
        }
    }

    @Override
    public Clazz updateClass(Clazz clazz) throws ServiceException {
        if (clazz.getNumber() <= 0 || clazz.getNumber() >= 12) {
            throw new ServiceException("Invalid class number");
        } else if (clazz.getLetterMark().isEmpty()) {
            throw new ServiceException("Invalid letter mark");
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            clazz = clazzIRepository.update(clazz, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            throw new ServiceException("Repository update exception", exc);
        } finally {
            session.close();
        }
        return clazz;
    }

    @Override
    public Clazz deleteClass(int classId) throws ServiceException {
        if (classId <= 0) {
            throw new ServiceException("Invalid class number");
        }
        Clazz clazz = new Clazz();
        clazz.setClassId(classId);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            clazz = clazzIRepository.delete(clazz, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            throw new ServiceException("Repository delete exception", exc);
        } finally {
            session.close();
        }
        return clazz;
    }

    @Override
    public Clazz getOne(int classId) throws ServiceException {
        if (classId <= 0) {
            throw new ServiceException("Invalid class number");
        }
        ClazzSpecification specification = new ClazzSpecificationByClazzId(classId);
        Clazz clazz = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Clazz> clazzList = clazzIRepository.query(specification, session);
            if (clazzList.size() > 0) {
                clazz = clazzList.get(0);
            }
            transaction.commit();
            return clazz;
        } catch (RepositoryException e) {
            transaction.rollback();
            throw new ServiceException("Repository query exception", e);
        } finally {
            session.close();
        }
    }
}
