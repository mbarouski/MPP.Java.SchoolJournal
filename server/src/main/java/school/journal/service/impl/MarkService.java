package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import school.journal.entity.*;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.pupil.PupilSpecificationByClassId;
import school.journal.repository.specification.pupil.PupilSpecificationByPupilId;
import school.journal.repository.specification.subject.SubjectSpecificationBySubjectId;
import school.journal.service.IMarkService;
import school.journal.service.CRUDService;
import school.journal.service.exception.ServiceException;
import school.journal.utils.exception.ValidationException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.hibernate.criterion.CriteriaSpecification.INNER_JOIN;
import static school.journal.utils.ValidateServiceUtils.validateId;
import static school.journal.utils.ValidateServiceUtils.validateNullableId;

@Component("MarkService")
public class MarkService extends CRUDService<Mark> implements IMarkService {

    @Autowired
    public MarkService(@Qualifier("MarkRepository") IRepository<Mark> repository) {
        LOGGER = Logger.getLogger(MarkService.class);
        this.repository = repository;
    }

    @Override
    public Mark create(Mark mark) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            checkMarkBeforeCreate(mark, session);
            mark = repository.create(mark, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        return mark;
    }

    @Override
    public Mark update(Mark mark) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        mark = prepareMarkBeforeUpdate(mark, session);
        try {
            mark = repository.update(mark, session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        return mark;
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
        super.delete(mark);
    }

    private void checkMarkBeforeCreate(Mark newMark, Session session) throws ServiceException {
        try {
            validateNullableId(newMark.getMarkId(), "Mark");
            validateSubject(newMark.getSubject().getSubjectId(), session);
            if (newMark.getTeacher() != null) {
                validateTeacher(newMark.getTeacher().getUser().getUserId(), session);
            }
            validateDate(newMark.getDate());
            validateValue(newMark.getValue());
            //validateType(newMark.getType());
            validatePupil(newMark.getPupil().getUser().getUserId(), session);
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
    }

    private Mark prepareMarkBeforeUpdate(Mark newMark, Session session) throws ServiceException {
        try {
            Mark mark = repository.get(newMark.getMarkId(), session);
            checkValue(mark, newMark.getValue());
            checkDate(mark, newMark.getDate());
            if (newMark.getPupil() != null) {
                checkPupil(mark, newMark.getPupil().getUser().getUserId(), session);
            }
            if (newMark.getSubject() != null) {
                checkSubject(mark, newMark.getSubject().getSubjectId(), session);
            }
            if (newMark.getTeacher() != null) {
                checkTeacher(mark, newMark.getTeacher().getUser().getUserId(), session);
            }
            if (newMark.getType() != null) {
                mark.setType(newMark.getType());
            }
            return mark;
        } catch (RepositoryException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
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
        return super.getOne(id);
    }

    @Override
    public List<Mark> getMarksForSubjectInClass(int subjectId, int classId) throws ServiceException {
        validateSubjectAndClassBeforeSelecting(subjectId, classId);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Criteria criteria = session.createCriteria(Mark.class);
        criteria.createCriteria("subject", INNER_JOIN).add(
                new SubjectSpecificationBySubjectId(subjectId).toCriteria());
        criteria.createCriteria("pupil", INNER_JOIN).add(
                new PupilSpecificationByClassId(classId).toCriteria());
        List<Mark> markList;
        try {
            markList = (List<Mark>) criteria.list();
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            session.close();
        }
        return markList;
    }

    public void validateSubjectAndClassBeforeSelecting(int subjectId, int classId) throws ServiceException {
        try {
            validateId(subjectId, "Subject");
            validateId(classId, "Class");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
    }

    @Override
    public List<Mark> getMarksForTermOrder(int classId) throws ServiceException {
        List<Mark> markList;
        validateClassIdBeforeSelect(classId);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Criteria criteria = session.createCriteria(Mark.class);
            criteria.createCriteria("pupil", INNER_JOIN).add(
                    new PupilSpecificationByClassId(classId).toCriteria());
            markList = (List<Mark>) criteria.list();
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            session.close();
        }
        return markList;
    }

    private void validateClassIdBeforeSelect(int classId) throws ServiceException {
        try {
            validateId(classId, "Class");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
    }

    @Override
    public List<Mark> getMarksForPupil(int pupilId) throws ServiceException {
        validatePupilIdBeforeSelect(pupilId);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Mark> markList;
        try {
            Criteria criteria = session.createCriteria(Mark.class);
            criteria.createCriteria("pupil", INNER_JOIN).add(
                    new PupilSpecificationByPupilId(pupilId).toCriteria());
            markList = criteria.list();
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            session.close();
        }
        return markList;
    }

    private void validatePupilIdBeforeSelect(int pupilId) throws ServiceException {
        try {
            validateId(pupilId, "Pupil");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
    }

    private void checkTeacher(Mark mark, int id, Session session) {
        Teacher teacher = (Teacher) session.get(Teacher.class, id);
        if (teacher != null) {
            mark.setTeacher(teacher);
        }
    }

    private Teacher validateTeacher(int id, Session session) throws ServiceException {
        Teacher teacher = (Teacher) session.get(Teacher.class, id);
        if (teacher == null) {
            throw new ServiceException("Error in Mark validation. Teacher is not exist");
        }
        return teacher;
    }

    private void checkSubject(Mark mark, int id, Session session) throws ServiceException {
        try {
            Subject subject = validateSubject(id, session);
            mark.setSubject(subject);
        } catch (ServiceException exc) {
            LOGGER.warn(exc);
        }
    }

    private Subject validateSubject(int id, Session session) throws ServiceException {
        Subject subject = (Subject) session.get(Subject.class, id);
        if (subject == null) {
            throw new ServiceException("Error in Mark validation. Subject is not exist");
        }
        return subject;
    }

    private void checkPupil(Mark mark, int id, Session session) {
        try {
            Pupil pupil = validatePupil(id, session);
            mark.setPupil(pupil);
        } catch (ServiceException exc) {
            LOGGER.warn(exc);
        }
    }

    private Pupil validatePupil(int id, Session session) throws ServiceException {
        Pupil pupil = (Pupil) session.get(Pupil.class, id);
        if (pupil == null) {
            throw new ServiceException("Error in Mark validation. Pupil is not exist");
        }
        return pupil;
    }

    private void checkDate(Mark mark, java.sql.Date date) throws ServiceException {
        if (date == null) return;
        try {
            validateDate(date);
            mark.setDate(date);
        } catch (ServiceException exc) {
            LOGGER.warn(exc);
        }
    }

    private void validateDate(Date date) throws ServiceException {
        Calendar c = new GregorianCalendar();
        c.add(Calendar.DAY_OF_YEAR, 1);
        if (date.after(c.getTime())) {
            throw new ServiceException("Invalid date");
        }
    }

    private void checkValue(Mark mark, Integer value) {
        if (value == null) return;
        try {
            validateValue(value);
            mark.setValue(value);
        } catch (ServiceException exc) {
            LOGGER.warn(exc);
        }
    }

    private void validateValue(Integer value) throws ServiceException {
        if (value == null) return;
        if (value <= 0 || value >= 11) {
            throw new ServiceException("Invalid value");
        }
    }

}
