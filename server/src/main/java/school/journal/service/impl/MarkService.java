package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.mark.MarkSpecificationByPupilId;
import school.journal.repository.specification.mark.MarkSpecificationByMarkId;
import school.journal.service.IMarkService;
import school.journal.service.CRUDService;
import school.journal.service.exception.ServiceException;
import school.journal.utils.exception.ValidationException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static school.journal.utils.ValidateServiceUtils.validateId;

@Component("MarkService")
public class MarkService extends CRUDService<Mark> implements IMarkService {

    @Autowired
    public MarkService(@Qualifier("MarkRepository")IRepository<Mark> repository) {
        LOGGER = Logger.getLogger(MarkService.class);
        this.repository = repository;
    }

    @Override
    public Mark create(Mark mark) throws ServiceException {
        try {
            validateId(mark.getPupilId(), "Pupil");
            validateId(mark.getSubjectId(), "Subject");
            validateId(mark.getTeacherId(), "Teacher");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        validateValue(mark.getValue());
        validateCreatingMarkDate(mark.getDate());
        return super.create(mark);
    }

    @Override
    public Mark update(Mark mark) throws ServiceException {
        try {
            validateId(mark.getMarkId(), "Mark");
            validateId(mark.getPupilId(), "Pupil");
            validateId(mark.getSubjectId(), "Subject");
            validateId(mark.getTeacherId(), "Teacher");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        validateValue(mark.getValue());
        validateCreatingMarkDate(mark.getDate());
        return super.update(mark);
    }

    @Override
    public void delete(int id) throws ServiceException {
        try {
            validateId(id, "Mark");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        Mark mark = new Mark();
        mark.setMarkId(id);
        delete(mark);
    }


    @Override
    public List<Mark> read() throws ServiceException {
        return super.read();
    }

    @Override
    public Mark getOne(int id) throws ServiceException {
        try {

            validateId(id, "Mark");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<Mark> markList = repository.query(
                    new MarkSpecificationByMarkId(id), session);
            transaction.commit();
            return markList.size() > 0 ? markList.get(0) : null;
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Mark> getMarksForSubjectInClass(int subjectId, int classId) throws ServiceException {
        try {
            validateId(subjectId, "Subject");
            validateId(classId, "Class");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        //TODO Learn how to create complex criteries
        //TODO DON'T DELETE THE CODE BELOW
        /*Criteria criteria = session.createCriteria(Mark.class);
        criteria.createAlias("school_journal_db.pupil", "pupil", INNER_JOIN).
                add(Restrictions.and(
                        new MarkSpecificationBySubjectId(subjectId).toCriteria(),
                        Restrictions.eq("pupil.class_id", classId)
                ));*/
        List<Mark> markList = null;
        markList = session.createSQLQuery(
                "SELECT  * " +
                        "FROM  mark as m " +
                        "JOIN pupil as p " +
                        "ON m.pupil_id = p.pupil_id " +
                        "WHERE p.class_id =" + classId + " " +
                        "AND m.subject_id = " + subjectId + ";").list();
        /*try {
            markList = (List<Mark>) criteria.list();
            transaction.commit();
        } finally {
            session.close();
        }*/
        transaction.commit();
        session.close();
        return markList;
    }

    @Override
    public List<Mark> getMarksForTermOrder(int classId) throws ServiceException {
        try {
            validateId(classId, "Class");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        //TODO Learn how to create complex criteries
        //TODO DON'T DELETE THE CODE BELOW
        /*Criteria criteria = session.createCriteria(Mark.class);
        criteria.createAlias("school_journal_db.pupil", "pupil", INNER_JOIN).
                add(Restrictions.and(
                        Restrictions.between("mark.date", startTerm, endTerm),
                        Restrictions.eq("pupil.class_id", classId)
                ));*/
        List<Mark> markList;
        markList = session.createSQLQuery(
                "SELECT  * " +
                        "FROM  mark as m " +
                        "JOIN pupil as p " +
                        "ON m.pupil_id = p.pupil_id " +
                        "WHERE p.class_id =" + classId + " ;").list();
        /*try {
            markList = (List<Mark>) criteria.list();
            transaction.commit();
        } finally {
            session.close();
        }
        */
        transaction.commit();
        session.close();
        return markList;
    }

    @Override
    public List<Mark> getMarksForPupil(int pupilId) throws ServiceException {
        try {
            validateId(pupilId, "Pupil");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Mark> markList;
        try {
            markList = repository.query(new MarkSpecificationByPupilId(pupilId), session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return markList;
    }

    private void validateCreatingMarkDate(Date date) throws ServiceException {
        Calendar c = new GregorianCalendar();
        c.add(Calendar.DAY_OF_YEAR, 1);
        if (date.after(c.getTime())) {
            throw new ServiceException("Invalid date");
        }
    }

    private void validateValue(int value) throws ServiceException {
        if (value <= 0 || value >= 11) {
            throw new ServiceException("Invalid value");
        }
    }

}
