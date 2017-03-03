package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import school.journal.entity.Clazz;
import school.journal.entity.Pupil;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.pupil.PupilSpecification;
import school.journal.repository.specification.pupil.PupilSpecificationByClassId;
import school.journal.repository.specification.pupil.PupilSpecificationByPupilId;
import school.journal.service.IPupilService;
import school.journal.service.ServiceAbstractClass;
import school.journal.service.exception.ServiceException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PupilService extends ServiceAbstractClass implements IPupilService {
    @Autowired
    private IRepository<Pupil> pupilRepository;
    private static final Logger LOGGER = Logger.getLogger(ClassService.class);

    private static Pattern PHONE_PATTERN = Pattern.compile("\\+375[0-9]+@[0-9a-zA-Z]");

    @Override
    public List<Pupil> getListOfPupils(int id) throws ServiceException {
        validateId(id);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Pupil> pupilList;
        try {
            pupilList = pupilRepository.query(
                    new PupilSpecificationByClassId(id), session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return pupilList != null ? pupilList : new LinkedList<Pupil>();
    }

    @Override
    public Pupil movePupilToAnotherClass(Pupil pupil, Clazz clazz) throws ServiceException {
        validateId(clazz.getClassId());
        pupil.setClassId(clazz.getClassId());
        return update(pupil);
    }

    @Override
    public Pupil getOne(int pupilId) throws ServiceException {
        validateId(pupilId);
        PupilSpecification specification = new PupilSpecificationByPupilId(pupilId);
        Pupil pupil = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Pupil> pupilList = pupilRepository.query(specification, session);
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
        validateNullableId(pupil.getClassId());
        validateString(pupil.getFirstName(), "First Name");
        validateString(pupil.getLastName(), "Last Name");
        validateString(pupil.getPathronymic(), "Patronymic");
        validateStartYear(pupil.getStartYear());
        validateEndYear(pupil.getEndYear());
        validateNullablePhone(pupil.getPhoneNumber());
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            pupil = pupilRepository.create(pupil, session);
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
        validateId(pupil.getPupilId());
        validateNullableId(pupil.getClassId());
        validateString(pupil.getFirstName(), "First Name");
        validateString(pupil.getLastName(), "Last Name");
        validateString(pupil.getPathronymic(), "Patronymic");
        validateStartYear(pupil.getStartYear());
        validateEndYear(pupil.getEndYear());
        validateNullablePhone(pupil.getPhoneNumber());
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            pupil = pupilRepository.update(pupil, session);
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
        validateId(id);
        Pupil pupil = new Pupil();
        pupil.setPupilId(id);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            pupilRepository.delete(pupil, session);
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
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Pupil> pupilList = pupilRepository.query(null, session);
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

    private void validateId(int id) throws ServiceException {
        if (id <= 0) {
            throw new ServiceException("Invalid id");
        }
    }

    private void validateNullableId(Integer id) throws ServiceException {
        if (id == null) throw new ServiceException("Invalid id (null)");
        validateId(id);
    }

    private void validateString(String string, String parameterName) throws ServiceException {
        if (string.isEmpty()) {
            throw new ServiceException("Invalid " + parameterName);
        }
    }

    private void validateStartYear(Date date) throws ServiceException {
        Calendar c = new GregorianCalendar();
        c.add(Calendar.YEAR, -100);
        if (date.before(c.getTime())) {
            throw new ServiceException("Invalid date");
        }
        c.add(Calendar.YEAR,112);
        //We have no information about pupil, who hasn't entered our school
        //Plus 100year that we subtract
        if (date.after(c.getTime())) {
            throw new ServiceException("Invalid date");
        }
    }
    private void validateEndYear(Date date) throws ServiceException {
        Calendar c = new GregorianCalendar();
        c.add(Calendar.YEAR, -88);
        //plus 11 year of education
        if (date.before(c.getTime())) {
            throw new ServiceException("Invalid date of starting education");
        }
        c.add(Calendar.YEAR,123);
        //We have no information about pupil, who hasn't entered our school
        //Plus 100year that we subtract
        if (date.after(c.getTime())) {
            throw new ServiceException("Invalid date of ending education");
        }
    }

    private void validateNullablePhone(String phone) throws ServiceException {
        if (phone == null) {
            return;
        }
        if (phone.isEmpty()) {
            throw new ServiceException("Invalid Phone Number");
        }
        Matcher m = PHONE_PATTERN.matcher(phone);
        if (!m.matches()) {
            throw new ServiceException("Phone isn't correct.");
        }
    }
}
