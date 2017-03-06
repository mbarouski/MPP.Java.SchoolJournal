package school.journal.service.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import school.journal.entity.SubjectInSchedule;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.service.ISubjectInScheduleService;
import school.journal.service.ServiceAbstractClass;
import school.journal.service.exception.ServiceException;
import static school.journal.utils.ValidateServiceUtils.*;

import java.sql.Time;
import java.util.List;

public class SubjectInScheduleService extends ServiceAbstractClass implements ISubjectInScheduleService {
    
    private static final Logger LOGGER = Logger.getLogger(SubjectInScheduleService.class);

    public enum Day {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
        THURSDAY, FRIDAY, SATURDAY
    }

    @Autowired
    private IRepository<SubjectInSchedule> subjectInScheduleRepository;

    private void checkTime(Time time) throws ServiceException{
        if(time.before(new Time(7,0,0)) || time.after(new Time(20,0,0)) ){
            throw new ServiceException("Invalid begin time of subject");
        }
    }

    @Override
    public SubjectInSchedule create(SubjectInSchedule subjectInSchedule) throws ServiceException {

        validateId(subjectInSchedule.getClassId(),"Class");
        validateId(subjectInSchedule.getTeacherId(),"Teacher");
        validateString(subjectInSchedule.getPlace(),"Place");
        checkTime(subjectInSchedule.getBeginTime());

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            subjectInScheduleRepository.create(subjectInSchedule, session);
            session.getTransaction().commit();
        } catch (RepositoryException exc) {
            session.getTransaction().rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }

        return subjectInSchedule;
    }

    @Override
    public SubjectInSchedule update(SubjectInSchedule subjectInSchedule) throws ServiceException {

        validateId(subjectInSchedule.getSubectInScheduleId(),"SubjectInSchedule");
        validateId(subjectInSchedule.getClassId(),"Class");
        validateId(subjectInSchedule.getTeacherId(),"Teacher");
        validateString(subjectInSchedule.getPlace(),"Place");
        checkTime(subjectInSchedule.getBeginTime());

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            subjectInScheduleRepository.update(subjectInSchedule, session);
            session.getTransaction().commit();
        } catch (RepositoryException exc) {
            session.getTransaction().rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }

        return subjectInSchedule;
    }

    @Override
    public void delete(int id) throws ServiceException {

        validateId(id, "SubjectInSchedule");

        SubjectInSchedule subject = new SubjectInSchedule();
        subject.setSubectInScheduleId(id);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            subjectInScheduleRepository.delete(subject, session);
            session.getTransaction().commit();
        } catch (RepositoryException exc) {
            session.getTransaction().rollback();
            LOGGER.error(exc);
            throw new ServiceException(exc);
        } finally {
            session.close();
        }
    }

    @Override
    public List<SubjectInSchedule> read() throws ServiceException {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            return subjectInScheduleRepository.query(null, session);
        } catch (RepositoryException exc){
            LOGGER.error(exc);
            throw new ServiceException();
        } finally {
            session.close();
        }
    }
    
}
