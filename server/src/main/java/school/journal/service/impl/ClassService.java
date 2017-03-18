package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.clazz.ClazzSpecificationByClazzId;
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
        return super.create(clazz);
    }

    @Override
    public Clazz update(Clazz clazz) throws ServiceException {
        try {
            validateId(clazz.getClassId(), "Class");
            validateString(clazz.getLetterMark(), "Letter Mark");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        validateClassNumber(clazz.getNumber());
        return super.update(clazz);
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
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Clazz> clazzList = repository.query(
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

    private void validateClassNumber(int number) throws ServiceException {
        if (number <= 0 || number >= 12)
            throw new ServiceException("Invalid class number");
    }

}
