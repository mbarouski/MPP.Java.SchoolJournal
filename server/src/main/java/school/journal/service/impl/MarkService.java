package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.datetime.joda.MillisecondInstantPrinter;
import org.springframework.stereotype.Service;
import school.journal.controller.util.ExceptionEnum;
import school.journal.entity.*;
import school.journal.entity.enums.MarkType;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.mark.MarkSpecificationByTerm;
import school.journal.repository.specification.pupil.PupilSpecificationByClassId;
import school.journal.repository.specification.pupil.PupilSpecificationByPupilId;
import school.journal.repository.specification.subject.SubjectSpecificationBySubjectId;
import school.journal.service.IMarkService;
import school.journal.service.CRUDService;
import school.journal.service.ITermService;
import school.journal.service.exception.ClassifiedServiceException;
import school.journal.service.exception.ServiceException;
import school.journal.utils.exception.ValidationException;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;

import static org.hibernate.criterion.CriteriaSpecification.INNER_JOIN;
import static school.journal.entity.enums.MarkType.term;
import static school.journal.entity.enums.MarkType.year;
import static school.journal.service.impl.TermService.MILLISECONDS_IN_DAY;
import static school.journal.utils.ValidateServiceUtils.validateId;

@Service("MarkService")
public class MarkService extends CRUDService<Mark> implements IMarkService {

    private ITermService termService;

    @Autowired
    public MarkService(@Qualifier("MarkRepository") IRepository<Mark> repository, @Qualifier("TermService") ITermService termService) {
        LOGGER = Logger.getLogger(MarkService.class);
        this.repository = repository;
        this.termService = termService;
    }

    @Override
    public Mark create(Mark mark) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            checkMarkBeforeCreate(mark, session);
            mark = repository.create(mark, session);
            transaction.commit();
        } catch (RepositoryException | ObjectNotFoundException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
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

    @Override
    public List<Mark> read() throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Mark> list;
        try {
            list = repository.query(new MarkSpecificationByTerm(termService.getCurrentTerm().getNumber()), session);
            transaction.commit();
        } catch (RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
        return list;
    }

    @Override
    public Mark getOne(int id) throws ServiceException {
        try {
            validateId(id, "Mark");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ClassifiedServiceException(ExceptionEnum.mark_not_found);
        }
        return super.getOne(id);
    }

    @Override
    public List<Mark> getMarksForSubjectInClass(int subjectId, int classId, int termId) throws ServiceException {
        validateSubjectAndClassBeforeSelect(subjectId, classId);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Term term = (termId != 0) ? (Term) session.get(Term.class, termId) : termService.getCurrentTerm();
        Criteria criteria = session.createCriteria(Mark.class);
        criteria.add(new MarkSpecificationByTerm(term.getNumber()).toCriteria());
        criteria.createCriteria("subject", INNER_JOIN).add(new SubjectSpecificationBySubjectId(subjectId).toCriteria());
        criteria.createCriteria("pupil", INNER_JOIN).add(new PupilSpecificationByClassId(classId).toCriteria());
        criteria.addOrder(Order.asc("pupil")).addOrder(Order.asc("date"));
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

    @Override
    public List<Mark> getMarksForTermOrderInClass(int classId, int termId) throws ServiceException {
        List<Mark> markList;
        validateTermId(termId);
        validateClassIdBeforeSelect(classId);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Term term = (termId != 0) ? (Term) session.get(Term.class, termId) : termService.getCurrentTerm();
            Criteria criteria = session.createCriteria(Mark.class);
            criteria.add(new MarkSpecificationByTerm(term.getNumber()).toCriteria());
            criteria.createCriteria("pupil", INNER_JOIN).add(new PupilSpecificationByClassId(classId).toCriteria());
            criteria.addOrder(Order.asc("classId")).addOrder(Order.asc("pupil.pupilId")).addOrder(Order.asc("date"));
            markList = (List<Mark>) criteria.list();
            transaction.commit();
        } catch (HibernateException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            session.close();
        }
        return markList;
    }

    @Override
    public List<Mark> getMarksForPupil(int pupilId, int termId) throws ServiceException {
        validateTermId(termId);
        validatePupilIdBeforeSelect(pupilId);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Mark> markList;
        try {
            Term term = (termId != 0) ? (Term) session.get(Term.class, termId) : termService.getCurrentTerm();
            Criteria criteria = session.createCriteria(Mark.class);
            criteria.add(new MarkSpecificationByTerm(term.getNumber()).toCriteria());
            criteria.createCriteria("pupil", INNER_JOIN).add(new PupilSpecificationByPupilId(pupilId).toCriteria());
            criteria.addOrder(Order.asc("pupil")).addOrder(Order.asc("date"));
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

    private void checkMarkBeforeCreate(Mark newMark, Session session) throws ServiceException {
        validateDate(newMark.getDate());
        validateValue(newMark.getValue());
        Pupil pupil = (Pupil) session.get(Pupil.class, newMark.getPupilId());
        if (pupil == null) {
            throw new ClassifiedServiceException(ExceptionEnum.mark_has_wrong_pupil);
        }
        newMark.setPupil(pupil);
        Subject subject = (Subject) session.get(Subject.class, newMark.getSubjectId());
        if (subject == null) {
            throw new ClassifiedServiceException(ExceptionEnum.mark_has_wrong_subject);
        }
        newMark.setSubject(subject);
        Teacher teacher = (Teacher) session.get(Teacher.class, newMark.getTeacherId());
        if (teacher == null) {
            throw new ClassifiedServiceException(ExceptionEnum.mark_has_wrong_teacher);
        }
        if (newMark.getType() == year) {
            newMark.setTermNumber(null);
        } else if (newMark.getTermNumber() > 4 || newMark.getTermNumber() < 0) {
            throw new ClassifiedServiceException(ExceptionEnum.mark_has_wrong_term);
        }
        newMark.setTeacher(teacher);
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
            if (newMark.getType() == year){
                mark.setTermNumber(null);
            } else if (newMark.getTermNumber() > 4 || newMark.getTermNumber() < 0) {
                throw new ClassifiedServiceException(ExceptionEnum.mark_has_wrong_term);
            }
            return mark;
        } catch (RepositoryException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
    }

    private void validateSubjectAndClassBeforeSelect(int subjectId, int classId) throws ServiceException {
        try {
            validateId(subjectId, "Subject");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ClassifiedServiceException(ExceptionEnum.mark_has_wrong_subject);
        }
        try {
            validateId(classId, "Class");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ClassifiedServiceException(ExceptionEnum.mark_has_wrong_pupil);
        }
    }

    private void validateClassIdBeforeSelect(int classId) throws ServiceException {
        try {
            validateId(classId, "Class");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ClassifiedServiceException(ExceptionEnum.mark_has_wrong_pupil);
        }
    }

    private void validatePupilIdBeforeSelect(int pupilId) throws ServiceException {
        try {
            validateId(pupilId, "Pupil");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ClassifiedServiceException(ExceptionEnum.mark_has_wrong_pupil);
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
            throw new ClassifiedServiceException(ExceptionEnum.mark_has_wrong_teacher);
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
            throw new ClassifiedServiceException(ExceptionEnum.mark_has_wrong_subject);
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
            throw new ClassifiedServiceException(ExceptionEnum.mark_has_wrong_pupil);
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

    private void validateDate(java.sql.Date date) throws ServiceException {
        Session session = sessionFactory.openSession();
        java.sql.Date start = ((Term) session.get(Term.class, 1)).getStart();
        java.sql.Date end = termService.getCurrentTerm().getEnd();
        end.setTime(end.getTime() + MILLISECONDS_IN_DAY);
        if (date.after(end) || (date.before(start))) {
            throw new ClassifiedServiceException(ExceptionEnum.mark_date_is_invalid);
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
            throw new ClassifiedServiceException(ExceptionEnum.mark_has_wrong_value);
        }
    }

    private void validateTermId(int termId) throws ServiceException {
        if (termId < 0 || termId > 4) {
            throw new ClassifiedServiceException(ExceptionEnum.mark_date_is_invalid);
        }
    }
}
