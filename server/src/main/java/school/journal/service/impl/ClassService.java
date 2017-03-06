package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import school.journal.entity.Clazz;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.clazz.ClazzSpecificationByClazzId;
import school.journal.service.ICLassService;
import school.journal.service.CRUDService;
import school.journal.service.exception.ServiceException;
import java.util.List;

import static school.journal.utils.ValidateServiceUtils.validateId;
import static school.journal.utils.ValidateServiceUtils.validateString;


@Component
public class ClassService extends CRUDService<Clazz> implements ICLassService {

    public ClassService() {
        LOGGER = Logger.getLogger(ClassService.class);
    }

    @Override
    public List<Clazz> read() throws ServiceException {
        return super.read();
    }

    @Override
    public Clazz create(Clazz clazz) throws ServiceException {
        validateClassNumber(clazz.getNumber());
        validateString(clazz.getLetterMark(), "Letter Mark");
        return super.create(clazz);
    }

    @Override
    public Clazz update(Clazz clazz) throws ServiceException {
        validateId(clazz.getClassId(), "Class");
        validateClassNumber(clazz.getNumber());
        validateString(clazz.getLetterMark(), "Letter Mark");
        return super.update(clazz);
    }

    @Override
    public void delete(int id) throws ServiceException {
        validateId(id, "Class");
        Clazz clazz = new Clazz();
        clazz.setClassId(id);
        super.delete(clazz);
    }


    @Override
    public Clazz getOne(int id) throws ServiceException {
        validateId(id, "Class");
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
