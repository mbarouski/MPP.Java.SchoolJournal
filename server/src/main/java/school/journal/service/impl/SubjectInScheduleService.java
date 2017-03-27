package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import school.journal.entity.Clazz;
import school.journal.entity.Subject;
import school.journal.entity.SubjectInSchedule;
import school.journal.entity.Teacher;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.service.CRUDService;
import school.journal.service.ISubjectInScheduleService;
import school.journal.service.exception.ServiceException;
import school.journal.utils.exception.ValidationException;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static school.journal.utils.ValidateServiceUtils.validateId;
import static school.journal.utils.ValidateServiceUtils.validateNullableId;
import static school.journal.utils.ValidateServiceUtils.validateString;

@Component("SubjectInScheduleService")
public class SubjectInScheduleService extends CRUDService<SubjectInSchedule> implements ISubjectInScheduleService {

    private static final long START_WORK_DAY_TIME_MILLIS = 25_200_000;//7h*60m*60s*1000ms
    private static final long END_WORK_DAY_TIME_MILLIS = 72_000_000;//20h*60m*60s*1000ms
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
            subject.setClazz((Clazz)session.load(Clazz.class,subject.getClazz().getClassId()));
            subject.setSubject((Subject)session.load(Subject.class,subject.getSubject().getSubjectId()));
            subject.setTeacher((Teacher)session.load(Teacher.class,subject.getTeacher().getUserId()));

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
    public SubjectInSchedule update(SubjectInSchedule subject) throws ServiceException {
        try {
            validateId(subject.getSubectInScheduleId(),"SubjectInSchedule");
            validateId(subject.getClazz().getClassId(),"Class");
            validateNullableId(subject.getTeacher().getUserId(),"Teacher");
            validateString(subject.getPlace(),"Place");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        checkTime(subject.getBeginTime());
        return super.update(subject);
    }

    @Override
    public void delete(int id) throws ServiceException {
        try {
            validateId(id, "SubjectInSchedule");
        } catch (ValidationException exc) {
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        SubjectInSchedule subject = new SubjectInSchedule();
        subject.setSubectInScheduleId(id);
        super.delete(subject);
    }

    @Override
    public List<SubjectInSchedule> read() throws ServiceException {
        return super.read();
    }

    public List<SubjectInSchedule> getPupilSchedule(int id) throws ServiceException{
        try {
            validateId(id,"Pupil");
        }catch (ValidationException exc){
            LOGGER.error(exc);
            throw new ServiceException(exc);
        }
        ArrayList<SubjectInSchedule> schedule = new ArrayList<>();

        return null;
    }

    //SET('simple', 'apsent', 'control', 'self', 'term', 'year')

}
