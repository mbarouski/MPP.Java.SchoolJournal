package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import school.journal.entity.Clazz;
import school.journal.entity.Pupil;
import school.journal.entity.SubjectInSchedule;
import school.journal.entity.User;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.pupil.PupilSpecificationByClassId;
import school.journal.repository.specification.pupil.PupilSpecificationByPupilId;
import school.journal.service.CRUDService;
import school.journal.service.IPupilService;
import school.journal.service.exception.ServiceException;
import school.journal.utils.exception.ValidationException;

import java.text.MessageFormat;
import java.util.*;

import static school.journal.utils.ValidateServiceUtils.*;

@Service("PupilService")
public class PupilService extends CRUDService<Pupil> implements IPupilService {

    @Autowired
    public PupilService(@Qualifier("PupilRepository")IRepository<Pupil> repository) {
        LOGGER = Logger.getLogger(PupilService.class);
        this.repository = repository;
    }

    @Override
    public List<Pupil> getListOfPupils(int clazzId) throws ServiceException {
        try {
            validateId(clazzId, "Class");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Pupil> pupilList = repository.query(
                    new PupilSpecificationByClassId(clazzId), session);
            transaction.commit();
            return pupilList;
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Pupil movePupilToAnotherClass(int pupilId, Integer classId) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Pupil pupil = (Pupil)session.get(Pupil.class, pupilId);
        if(pupil == null) throw new ServiceException("Pupil not found");
        Clazz clazz = (Clazz)session.get(Clazz.class, classId);
        if(clazz == null) throw new ServiceException("Class not found");
        pupil.setClassId(classId);
        try {
            repository.update(pupil, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return pupil;
    }

    @Override
    public Pupil removeFromClass(int pupilId) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Pupil pupil = (Pupil)session.get(Pupil.class, pupilId);
        if(pupil == null) throw new ServiceException("Pupil not found");
        pupil.setClassId(null);
        try {
            repository.update(pupil, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return pupil;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Pupil getOne(int pupilId) throws ServiceException {
        Pupil pupil = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            pupil = repository.get(pupilId, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return pupil;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Pupil create(Pupil pupil) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            checkPupilBeforeCreate(pupil,session);
            pupil = repository.create(pupil, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return pupil;
    }

    @Override
    public Pupil update(Pupil pupil) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            pupil = repository.update(prepareEntityForUpdate(pupil, session), session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return pupil;
    }

    @Override
    public void delete(int id) throws ServiceException {
        checkPupilId(id);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            repository.delete((Pupil) session.load(Pupil.class, id), session);
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
    public List<Pupil> read() throws ServiceException {
        return super.read();
    }

    @Override
    public List<Pupil> getPupilsWithoutClass() throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Pupil> pupils = Collections.EMPTY_LIST;
        try {
            pupils = session.createQuery("from Pupil as p where p.classId = null").list();
        }catch (Exception exc){
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        return pupils;
    }

    private void checkPupilId(int id) throws ServiceException {
        try {
            validateId(id, "Pupil");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
    }

    private Pupil prepareEntityForUpdate(Pupil newEntity, Session session) throws ServiceException {
        Pupil pupil;
        try {
            pupil = repository.get(newEntity.getUser().getUserId(), session);
            checkClassId(pupil, newEntity.getClassId(),session);
            checkFirstName(pupil, newEntity.getFirstName());
            checkLastName(pupil, newEntity.getLastName());
            checkPatronymic(pupil, newEntity.getPathronymic());
            checkCharacteristic(pupil, newEntity.getCharacteristic());
            checkPhoneNumber(pupil, newEntity.getPhoneNumber());
        } catch (RepositoryException | ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        return pupil;
    }

    private void checkPupilBeforeCreate(Pupil pupil,Session session) throws ServiceException {
        try {
            checkClassId(pupil, pupil.getClassId(), session);
            validateString(pupil.getFirstName(), "First Name");
            validateString(pupil.getLastName(), "Last Name");
            validateNullableString(pupil.getPathronymic(), "Patronymic");
            if (pupil.getPhoneNumber() != null) {
                validatePhone(pupil.getPhoneNumber());
            }
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
    }

    private void checkClassId(Pupil pupil, Integer classId,Session session) throws ServiceException, ValidationException {
        validateNullableId(classId, "Class");
        if(classId == null) return;
        Clazz clazz = (Clazz) session.get(Clazz.class, classId);
        if (clazz != null) {
            pupil.setClassId(classId);
        }
    }

    private void checkFirstName(Pupil oldPupil,String firstName) throws ValidationException {
        validateNullableString(firstName, "First Name");
        if (firstName != null) {
            oldPupil.setFirstName(firstName);
        }
    }

    private void checkLastName(Pupil oldPupil,String lastName) throws ValidationException {
        validateNullableString(lastName, "Last Name");
        if (lastName != null) {
            oldPupil.setLastName(lastName);
        }
    }

    private void checkPatronymic(Pupil oldPupil,String patronymic) throws ValidationException {
        validateNullableString(patronymic, "Patronymic");
        if (patronymic != null) {
            oldPupil.setPathronymic(patronymic);
        }
    }

    private void checkCharacteristic(Pupil oldPupil, String characteristic) throws ValidationException {
        validateNullableString(characteristic, "Characteristic");
        if (characteristic != null) {
            oldPupil.setCharacteristic(characteristic);
        }
    }

    private void checkPhoneNumber(Pupil oldPupil,String phone) throws ValidationException {
        validatePhone(phone);
        if (phone != null) {
            oldPupil.setPhoneNumber(phone);
        }
    }


}
