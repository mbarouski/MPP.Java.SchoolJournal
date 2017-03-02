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

import java.sql.Time;
import java.util.List;

public class SubjectInScheduleService extends ServiceAbstractClass implements ISubjectInScheduleService {
    
    private static final Logger LOGGER = Logger.getLogger(UserService.class);

    public enum Day {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
        THURSDAY, FRIDAY, SATURDAY
    }

    @Autowired
    private IRepository<SubjectInSchedule> subjectRepository;

    private void CheckId(int id, String column) throws ServiceException{
        if (id <= 0){
            throw new ServiceException("Invalid " + column + "Id");
        }
    }

    private void CheckPlace(SubjectInSchedule subject) throws ServiceException{
        if( (subject.getPlace() == null) || (subject.getPlace().isEmpty())){
            throw new ServiceException("Invalid place");
        }

    }
    private void CheckTime(Time time) throws ServiceException{
        if(time.before(new Time(7,0,0)) || time.after(new Time(20,0,0)) ){
            throw new ServiceException("Invalid begin time of subject");
        }
    }

    @Override
    public SubjectInSchedule create(SubjectInSchedule subjectInSchedule) throws ServiceException {

        CheckId(subjectInSchedule.getSubectInScheduleId(),"SubjectInSchedule");
        CheckId(subjectInSchedule.getClassId(),"Class");
        CheckId(subjectInSchedule.getTeacherId(),"Teacher");
        CheckPlace(subjectInSchedule);
        CheckTime(subjectInSchedule.getBeginTime());

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            subjectRepository.create(subjectInSchedule, session);
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

        CheckId(subjectInSchedule.getSubectInScheduleId(),"SubjectInSchedule");
        CheckId(subjectInSchedule.getClassId(),"Class");
        CheckId(subjectInSchedule.getTeacherId(),"Teacher");
        CheckPlace(subjectInSchedule);
        CheckTime(subjectInSchedule.getBeginTime());
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            subjectRepository.update(subjectInSchedule, session);
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

        SubjectInSchedule subject = new SubjectInSchedule();
        subject.setSubectInScheduleId(id);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            subjectRepository.delete(subject, session);
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
            return subjectRepository.query(null, session);
        } catch (RepositoryException exc){
            LOGGER.error(exc);
            throw new ServiceException();
        } finally {
            session.close();
        }
    }
    
}
