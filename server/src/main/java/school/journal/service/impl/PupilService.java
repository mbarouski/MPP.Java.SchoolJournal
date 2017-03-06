package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import school.journal.entity.Pupil;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.pupil.PupilSpecificationByClassId;
import school.journal.repository.specification.pupil.PupilSpecificationByPupilId;
import school.journal.service.CRUDService;
import school.journal.service.IPupilService;
import school.journal.service.exception.ServiceException;

import java.util.*;

import static school.journal.utils.ValidateServiceUtils.*;

@Component
public class PupilService extends CRUDService<Pupil> implements IPupilService {

    public PupilService() {
        LOGGER = Logger.getLogger(PupilService.class);
    }

    @Override
    public List<Pupil> getListOfPupils(int id) throws ServiceException {
        validateId(id, "Class");
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Pupil> pupilList = repository.query(
                    new PupilSpecificationByClassId(id), session);
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
        validateId(classId, "Class");
        validateId(pupilId, "Pupil");
        Pupil pupil = new Pupil();
        pupil.setClassId(classId);
        return update(pupil);
    }

    @Override
    public Pupil getOne(int pupilId) throws ServiceException {
        validateId(pupilId, "Pupil");
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
        validateNullableId(pupil.getClassId(), "Class");
        validateString(pupil.getFirstName(), "First Name");
        validateString(pupil.getLastName(), "Last Name");
        validateNullableString(pupil.getPathronymic(), "Patronymic");
        validateStartYear(pupil.getStartYear());
        validateEndYear(pupil.getEndYear());
        validate(pupil.getPhoneNumber());
        return super.create(pupil);
    }

    @Override
    public Pupil update(Pupil pupil) throws ServiceException {
        validateId(pupil.getPupilId(), "Pupil");
        validateNullableId(pupil.getClassId(), "Class");
        validateString(pupil.getFirstName(), "First Name");
        validateString(pupil.getLastName(), "Last Name");
        validateNullableString(pupil.getPathronymic(), "Patronymic");
        validateStartYear(pupil.getStartYear());
        validateEndYear(pupil.getEndYear());
        validateEducationPeriod(pupil.getStartYear(), pupil.getEndYear());
        validate(pupil.getPhoneNumber());
        return super.update(pupil);
    }

    @Override
    public void delete(int id) throws ServiceException {
        validateId(id, "Pupil");
        Pupil pupil = new Pupil();
        pupil.setPupilId(id);
        super.delete(pupil);
    }

    @Override
    public List<Pupil> read() throws ServiceException {
        return super.read();
    }

    /**
     * Validates the year of starting education. If the year was
     * more than 100 years ago, it became invalid.
     * we'll can't have information about it
     *
     * @param date is year to validate
     */
    private void validateStartYear(Date date) throws ServiceException {
        Calendar c = new GregorianCalendar();
        c.add(Calendar.YEAR, -100);
        if (date.before(c.getTime())) {
            throw new ServiceException("Invalid date to start education");
        }
        c = new GregorianCalendar();
        c.add(Calendar.YEAR, 1);
        if (date.after(c.getTime())) {
            throw new ServiceException("Invalid date to start education");
        }
    }

    /**
     * Validates the year of finishing education. If the year was
     * more than 88 years ago
     * (-100+12), it became invalid.
     *
     * @param date is year to validate
     */
    private void validateEndYear(Date date) throws ServiceException {
        Calendar c = new GregorianCalendar();
        c.add(Calendar.YEAR, -88);
        if (date.before(c.getTime())) {
            throw new ServiceException("Invalid date to end education");
        }
        c = new GregorianCalendar();
        c.add(Calendar.YEAR, 13);
        if (date.after(c.getTime())) {
            throw new ServiceException("Invalid date to end education");
        }
    }

    private void validateEducationPeriod
            (Date startDate, Date endDate) throws ServiceException {
        if (startDate.after(endDate)) {
            throw new ServiceException("Invalid period of education");
        }
    }
}
