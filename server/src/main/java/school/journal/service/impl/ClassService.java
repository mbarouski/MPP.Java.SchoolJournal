package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import school.journal.entity.Clazz;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.service.IClassService;
import school.journal.service.CRUDService;
import school.journal.service.exception.ServiceException;
import school.journal.utils.exception.ValidationException;

import java.util.List;

import static school.journal.utils.ValidateServiceUtils.validateId;
import static school.journal.utils.ValidateServiceUtils.validateString;


@Component
@Qualifier("ClassService")
public class ClassService extends CRUDService<Clazz> implements IClassService {

    @Autowired
    public ClassService(@Qualifier("ClazzRepository") IRepository<Clazz> repository) {
        LOGGER = Logger.getLogger(ClassService.class);
        this.repository = repository;
    }

    @Override
    public List<Clazz> read() throws ServiceException {
        return super.read();
    }

    @Override
    public Clazz create(Clazz clazz) throws ServiceException {
        validateClassNumber(clazz.getNumber());
        try {
            validateString(clazz.getLetterMark(), "Letter Mark");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            checkClassBeforeCreate(clazz);
            clazz = repository.create(clazz, session);
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
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            prepareClassBeforeUpdate(clazz, session);
            clazz = repository.update(clazz, session);
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
        try {
            validateId(id, "Class");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        Clazz clazz = new Clazz();
        clazz.setClassId(id);
        super.delete(clazz);
    }


    @Override
    public Clazz getOne(int id) throws ServiceException {
        try {
            validateId(id, "Class");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        return super.getOne(id);
    }

    private void checkClassBeforeCreate(Clazz clazz) throws ServiceException {
        try {
            validateLetterMark(clazz.getLetterMark());
            validateClassNumber(clazz.getNumber());
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
    }

    private void prepareClassBeforeUpdate(Clazz newClazz, Session session) throws ServiceException {
        Clazz clazz;
        try {
            clazz = repository.get(newClazz.getClassId(), session);
            validateClass(clazz);
            checkClassNumber(clazz, newClazz.getNumber());
            checkLetterMark(clazz, newClazz.getLetterMark());
        } catch (RepositoryException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
    }

    private void checkLetterMark(Clazz clazz, String letterMark) {
        if (letterMark == null) return;
        try {
            validateLetterMark(letterMark);
            clazz.setLetterMark(letterMark);
        } catch (ValidationException exc) {
            LOGGER.warn(exc);
        }
    }

    private void validateLetterMark(String string) throws ValidationException {
        validateString(string, "Letter Mark");
    }

    private void checkClassNumber(Clazz clazz, Integer number) {
        if (number == null) return;
        try {
            validateClassNumber(number);
            clazz.setNumber(number);
        } catch (ServiceException exc) {
            LOGGER.warn(exc);
        }
    }

    private void validateClassNumber(int number) throws ServiceException {
        if (number <= 0 || number >= 12)
            throw new ServiceException("Invalid class number");
    }

    private void validateClass(Clazz clazz) throws ServiceException {
        if (clazz == null) {
            throw new ServiceException("Class is not exists");
        }
    }
}
