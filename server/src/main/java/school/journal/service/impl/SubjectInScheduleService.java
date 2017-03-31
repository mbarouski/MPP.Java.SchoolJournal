package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import school.journal.entity.*;
import org.springframework.stereotype.Service;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.subjectInSchedule.SubjectInScheduleSpecificationByClass;
import school.journal.repository.specification.subjectInSchedule.SubjectInScheduleSpecificationByTeacher;
import school.journal.service.CRUDService;
import school.journal.service.ISubjectInScheduleService;
import school.journal.service.exception.ServiceException;
import school.journal.utils.exception.ValidationException;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static school.journal.utils.ValidateServiceUtils.validateString;

@Service("SubjectInScheduleService")
public class SubjectInScheduleService extends CRUDService<SubjectInSchedule> implements ISubjectInScheduleService {

    private static final long START_WORK_DAY_TIME_MILLIS = 28799999 - 10800000;//7h*60m*60s*1000ms+59m*60s*1000ms+59s*1000ms+999ms
    private static final long END_WORK_DAY_TIME_MILLIS = 72_000_000-10800000;//20h*60m*60s*1000ms
    @Autowired
    public SubjectInScheduleService(@Qualifier("SubjectInScheduleRepository")IRepository<SubjectInSchedule> repository) {
        LOGGER = Logger.getLogger(SubjectInSchedule.class);
        this.repository = repository;
    }

    private void checkTime(Time time) throws ServiceException {
        if (time.before(new Time(START_WORK_DAY_TIME_MILLIS))
                || time.after(new Time(END_WORK_DAY_TIME_MILLIS)))
            throw new ServiceException("Invalid begin time of subject");
    }

    @Override
    public SubjectInSchedule create(SubjectInSchedule subject) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            validateString(subject.getPlace(),"Place");
            checkTime(subject.getBeginTime());
            checkDayOfWeek(subject.getDayOfWeek());
            subject.setClazz((Clazz)session.get(Clazz.class,subject.getClazz().getClassId()));
            subject.setSubject((Subject)session.get(Subject.class,subject.getSubject().getSubjectId()));
            subject.setTeacher((Teacher)session.get(Teacher.class,subject.getTeacher().getUserId()));
            if(subject.getClazz().getClassId() == null || subject.getTeacher().getUserId() == null || subject.getSubject().getSubjectId() == null){
                throw new ServiceException("No such class,teacher or subject");
            }
            repository.create(subject, session);
            transaction.commit();
        } catch (RepositoryException | ValidationException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            if(session != null) {
                session.close();
            }
        }
        return super.create(subject);
    }

    @Override
    public SubjectInSchedule update(SubjectInSchedule newSubject) throws ServiceException {
        SubjectInSchedule subject;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            subject =repository.get(newSubject.getSubectInScheduleId(),session);
            subject.setClazz((Clazz)session.load(Clazz.class,newSubject.getClazz().getClassId()));
            subject.setSubject((Subject)session.load(Subject.class,newSubject.getSubject().getSubjectId()));
            subject.setTeacher((Teacher)session.load(Teacher.class,newSubject.getTeacher().getUserId()));
            checkPlace(newSubject,subject);
            checkTime(newSubject.getBeginTime());
            subject.setBeginTime(newSubject.getBeginTime());
            checkDayOfWeek(newSubject.getDayOfWeek());
            subject.setDayOfWeek(newSubject.getDayOfWeek());
            repository.update(subject,session);
        } catch (ValidationException | RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }

        return subject;
    }

    @Override
    public void delete(int id) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            SubjectInSchedule subject = (SubjectInSchedule) session.load(SubjectInSchedule.class, id);
            if (subject !=null){
                repository.delete(subject, session);
                transaction.commit();
            }
        } catch (ObjectNotFoundException | RepositoryException exc) {
            transaction.rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            if(session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<SubjectInSchedule> read() throws ServiceException {
        return super.read();
    }

    @Override
    public List<SubjectInSchedule> getPupilSchedule(int id) throws ServiceException{
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<SubjectInSchedule> subjects = new ArrayList<>();
        try {
            Pupil pupil =(Pupil) session.get(Pupil.class,id);
            Clazz clazz = (Clazz)session.get(Clazz.class,pupil.getClassId());
            if(clazz.getClassId() == null){
                throw new ServiceException("This pupil haven't a class");
            }
            subjects = repository.query(new SubjectInScheduleSpecificationByClass(clazz),session);
        }catch (Exception exc){
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        return subjects;
    }

    @Override
    public List<SubjectInSchedule> getTeacherSchedule(int teacherId) throws ServiceException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<SubjectInSchedule> subjects = new ArrayList<>();
        try {
            Teacher teacher = (Teacher) session.get(Teacher.class,teacherId);
            subjects = repository.query(new SubjectInScheduleSpecificationByTeacher(teacher),session);
        }catch (Exception exc){
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        return subjects;
    }


    private void checkPlace(SubjectInSchedule newSubject, SubjectInSchedule subject) throws ValidationException {
        String place = newSubject.getPlace();
        if(place == null) return;
        validateString(place, "Place");
        subject.setPlace(place);
    }

    private void checkDayOfWeek(Short day) throws ValidationException {
        if (day<1 || day>6){
            throw new ValidationException("Wrong day of week parameter");
        }
    }
    //SET('simple', 'apsent', 'control', 'self', 'term', 'year')

}
