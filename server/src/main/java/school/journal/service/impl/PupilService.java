package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import school.journal.entity.Pupil;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.pupil.PupilSpecificationByClassId;
import school.journal.repository.specification.pupil.PupilSpecificationByPupilId;
import school.journal.service.CRUDService;
import school.journal.service.IPupilService;
import school.journal.service.exception.ServiceException;
import school.journal.utils.exception.ValidationException;

import java.util.*;

import static school.journal.utils.ValidateServiceUtils.*;

@Component("PupilService")
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
    public Pupil movePupilToAnotherClass(int pupilId, Integer classId) throws ServiceException {
        try {
            validateId(classId, "Class");
            validateId(pupilId, "Pupil");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        Pupil pupil = new Pupil();
        pupil.setClassId(classId);
        return update(pupil);
    }

    @Override
    public Pupil getPupilInfo(int pupilId) throws ServiceException {
        try{
            validateId(pupilId, "Pupil");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        Pupil pupil = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Pupil> pupilList = repository.query(
                    new PupilSpecificationByPupilId(pupilId), session);
            if (pupilList.size() > 0) {
                pupil = pupilList.get(0);
            }
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
    public Pupil create(Pupil pupil) throws ServiceException {
        try {
            validateNullableId(pupil.getClassId(), "Class");
            validateString(pupil.getFirstName(), "First Name");
            validateString(pupil.getLastName(), "Last Name");
            validateNullableString(pupil.getPathronymic(), "Patronymic");
            validatePhone(pupil.getPhoneNumber());
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        return super.create(pupil);
    }

    @Override
    public Pupil update(Pupil pupil) throws ServiceException {
        try {
            validateId(pupil.getPupilId(), "Pupil");
            validateNullableId(pupil.getClassId(), "Class");
            validateString(pupil.getFirstName(), "First Name");
            validateString(pupil.getLastName(), "Last Name");
            validateNullableString(pupil.getPathronymic(), "Patronymic");
            validatePhone(pupil.getPhoneNumber());
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        return super.update(pupil);
    }

    @Override
    public void delete(int id) throws ServiceException {
        try{
            validateId(id, "Pupil");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        Pupil pupil = new Pupil();
        pupil.setPupilId(id);
        super.delete(pupil);
    }

    @Override
    public List<Pupil> read() throws ServiceException {
        return super.read();
    }
}
